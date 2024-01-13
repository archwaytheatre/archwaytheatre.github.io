(ns io.github.archwaytheatre.data.copy-images
  (:require [clojure.java.io :as io]
            [clojure.java.shell :as sh]
            [clojure.string :as str]
            [io.github.archwaytheatre.data.core :as data]
            [io.github.archwaytheatre.data.plays :as plays])
  (:import [java.awt Image]
           [java.awt.image BufferedImage]
           [java.net URL]
           [javax.imageio IIOImage ImageIO ImageWriteParam ImageWriter]))


(defn download-image
  ([url-str] (download-image url-str (io/file "test.png")))
  ([url-str target-file]
   (io/make-parents target-file)
   (let [source-image (ImageIO/read (URL. url-str))
         image-writer ^ImageWriter (.next (ImageIO/getImageWritersByFormatName "png"))
         ios (ImageIO/createImageOutputStream (io/file target-file))
         write-param (.getDefaultWriteParam image-writer)]
     (.setOutput image-writer ios)
     (.setCompressionMode write-param ImageWriteParam/MODE_EXPLICIT)
     (.setCompressionQuality write-param 0.1)
     (.write image-writer nil (IIOImage. source-image nil nil) write-param)
     (.flush ios)
     (.dispose image-writer)
     (.close ios))))

(defn copy-to-png [source-file target-file]
  (io/make-parents target-file)
  (let [source-image (ImageIO/read (io/file source-file))
        image-writer ^ImageWriter (.next (ImageIO/getImageWritersByFormatName "png"))
        ios (ImageIO/createImageOutputStream (io/file target-file))
        write-param (.getDefaultWriteParam image-writer)]
    (.setOutput image-writer ios)
    (.setCompressionMode write-param ImageWriteParam/MODE_EXPLICIT)
    (.setCompressionQuality write-param 0.1)
    (.write image-writer nil (IIOImage. source-image nil nil) write-param)
    (.flush ios)
    (.dispose image-writer)
    (.close ios)))

(defn rescale-image [input-file output-file dimension-fn]
  (let [input-image (ImageIO/read (io/file input-file))
        [w h] [(.getWidth input-image) (.getHeight input-image)]
        [w' h'] (dimension-fn [w h])
        scaled-image (.getScaledInstance input-image w' h' Image/SCALE_SMOOTH)
        output-image (BufferedImage. w' h' BufferedImage/TYPE_INT_ARGB)
        output-graphics (.getGraphics output-image)]

    (.drawImage output-graphics scaled-image 0 0 nil)
    (.dispose output-graphics)

    (io/make-parents output-file)
    (ImageIO/write output-image "png" (io/file output-file))))

(defn close-to? [x y epsilon]
  (< (abs (- x y)) epsilon))

(defn poster-dimensions [[width height]]
  (let [ideal-width 400
        ideal-height 566]
    (cond
      (close-to? (/ width height) 1.0 0.1) [ideal-width ideal-width] ; is it meant to be square?
      (close-to? (/ width height) (/ ideal-width ideal-height) 0.1) [ideal-width ideal-height] ; is it about right?
      :else [ideal-width (* ideal-width (/ height width))])))

(defn photo-dimensions [[width height]]
  (let [max-width 1024
        max-height 1024 ;683
        width-sf (/ max-width width)
        height-sf (/ max-height height)
        max-sf (min width-sf height-sf)]
    [(* width max-sf) (* height max-sf)]))

(defn sync-to-s3 [local-site-dir]
  (let [r (sh/sh "aws"
                 "--profile" "deploy"
                 "s3" "sync" (.getAbsolutePath local-site-dir) "s3://archwaytheatre/site")]
    (println (:exit r))
    (println (:out r))))

(defn upload-poster [production-name production-year poster-file]
  (let [local-dir (io/file "local-only" "s3-sync" "site")
        prod-code (data/codify production-name)
        local-file (io/file local-dir (str production-year) prod-code "poster-full.png")
        local-scaled-file (io/file local-dir (str production-year) prod-code "poster-scaled.png")]

    (println prod-code)

    (if (and (string? poster-file) (str/starts-with? poster-file "http"))
      (download-image poster-file local-file)
      (copy-to-png poster-file local-file))

    (rescale-image local-file local-scaled-file poster-dimensions)

    (sync-to-s3 local-dir)))

(defn upload-photos [production-name production-year photo-directory photographer]
  (let [local-dir (io/file "local-only" "s3-sync" "site")
        prod-code (data/codify production-name)
        about-json (or (plays/load-production-data production-year production-name)
                       (plays/create-production production-year production-name))
        photos (->> (io/file photo-directory)
                    (file-seq)
                    (filter #(.isFile %))
                    (map-indexed #(vector (inc %1) %2)))
        about-json' (assoc about-json :photos {:count        (count photos)
                                               :photographer photographer})]
    (println prod-code)

    (doseq [[idx file] photos]
      (let [target-file (io/file local-dir (str production-year) prod-code (format "photo-%04d.png" idx))]
        (println (str target-file " <- " file))
        (rescale-image file target-file photo-dimensions)))

    (sync-to-s3 local-dir)

    (plays/save-production-data about-json')))
