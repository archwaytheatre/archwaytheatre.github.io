(ns io.github.archwaytheatre.data.copy-images
  (:require [clojure.java.io :as io]
            [clojure.java.shell :as sh]
            [clojure.string :as str]
            [io.github.archwaytheatre.data.core :as data]
            [io.github.archwaytheatre.data.plays :as plays])
  (:import [java.awt Color Image]
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

(def ideal-poster-width 400)
(def ideal-poster-height 566)

(defn rescale-image-2 [input-file output-file dimension-fn]
  (let [input-image (ImageIO/read (io/file input-file))
        [w h] [(.getWidth input-image) (.getHeight input-image)]
        [w' h'] (dimension-fn [w h])
        scaled-image (.getScaledInstance input-image w' h' Image/SCALE_SMOOTH)
        output-image (BufferedImage. ideal-poster-width ideal-poster-height BufferedImage/TYPE_INT_ARGB)
        output-graphics (.getGraphics output-image)
        y-mismatch (int (/ (- ideal-poster-height h') 2))
        ]

    (.setColor output-graphics Color/GREEN)
    (.fillRect output-graphics 0 0 ideal-poster-width ideal-poster-height)
    ;(.drawImage output-graphics scaled-image 0 (int (/ (- ideal-poster-height h') 2)) nil)
    (.drawImage output-graphics scaled-image
                0 0 ideal-poster-width (inc y-mismatch)
                0 y-mismatch w' 0
                nil)
    (.drawImage output-graphics scaled-image
                0 (- ideal-poster-height (inc y-mismatch)) ideal-poster-width ideal-poster-height
                0 h' w' (- h' y-mismatch)
                nil)
    (.drawImage output-graphics scaled-image
                0 0 ideal-poster-width ideal-poster-height
                0 (- y-mismatch) w' (+ h' y-mismatch)
                nil)
    (doseq [y (range 0 (inc y-mismatch))]
      (let [p (/ y y-mismatch)
            q 120
            color (Color. 0 0 0 (min 255 (int (+ (- 255 q) (* p q)))))]
        (.setColor output-graphics color)
        (.drawLine output-graphics 0 (- y-mismatch y) ideal-poster-width (- y-mismatch y))
        (.drawLine output-graphics 0 (+ (- ideal-poster-height y-mismatch) y) ideal-poster-width (+ (- ideal-poster-height y-mismatch) y))
        ))
    (.dispose output-graphics)

    (io/make-parents output-file)
    (ImageIO/write output-image "png" (io/file output-file))))

(defn close-to? [x y epsilon]
  (< (abs (- x y)) epsilon))

(defn poster-dimensions [[width height]]
  (cond
    (close-to? (/ width height) 1.0 0.1) ; is it meant to be square?
    [ideal-poster-width ideal-poster-width]

    (close-to? (/ width height) (/ ideal-poster-width ideal-poster-height) 0.1) ; is it about right?
    [ideal-poster-width ideal-poster-height]

    :else
    [ideal-poster-width (* ideal-poster-width (/ height width))]))

(defn photo-dimensions [[width height]]
  (let [max-width 1024
        max-height 1024 ;683
        width-sf (/ max-width width)
        height-sf (/ max-height height)
        max-sf (min width-sf height-sf)]
    [(* width max-sf) (* height max-sf)]))

(defn run-proc [args]
  ;(println args)
  (let [r (apply sh/sh args)]
    (println (:exit r))
    (println (:out r))))

(defn sync-to-s3 [local-site-dir]
  (let [args ["aws"
              "--profile" "deploy"
              "s3" "sync" (.getAbsolutePath local-site-dir) data/s3-dir]]
    (run-proc args)))

(defn copy-to-s3 [local-site-dir file]
  (let [relative-path (str (.relativize (.toPath local-site-dir) (.toPath (io/file file))))
        args ["aws"
              "--profile" "deploy"
              "s3" "cp" (.getAbsolutePath file) (str data/s3-dir "/" relative-path)]]
    (run-proc args)))

(defn upload-poster [production-name production-year poster-file]
  (let [local-dir (io/file "local-only" "s3-sync" "site")
        prod-code (data/codify production-name)
        local-file (io/file local-dir (str production-year) prod-code "poster-full.png")
        local-scaled-file (io/file local-dir (str production-year) prod-code "poster-scaled.png")]

    (println prod-code)

    (if (and (string? poster-file) (str/starts-with? poster-file "http"))
      (download-image poster-file local-file)
      (copy-to-png poster-file local-file))

    ;(rescale-image local-file local-scaled-file poster-dimensions)
    (rescale-image-2 local-file local-scaled-file poster-dimensions)

    (copy-to-s3 local-dir local-file)
    (copy-to-s3 local-dir local-scaled-file)

    ))

(defn upload-photos [production-name production-year photo-directory photographer]
  (let [local-dir (io/file "local-only" "s3-sync" "site")
        prod-code (data/codify production-name)
        about-json (or (plays/load-production-data production-year production-name)
                       (plays/create-production production-year production-name))
        next-number-offset (inc (count (mapcat :photo-offsets (:photo-sets about-json))))
        photos (->> (io/file photo-directory)
                    (file-seq)
                    (filter #(.isFile %))
                    (map-indexed (fn [idx file]
                                   [file (format "photo-%04d.png" (+ idx next-number-offset)) 0.25])))
        about-json' (update about-json :photo-sets conj {:photographer  photographer
                                                         :photo-offsets (map rest photos)})]
    (println prod-code)

    (doseq [[file filename _eye-line-offset] photos]
      (let [target-file (io/file local-dir (str production-year) prod-code filename)]
        (println (str target-file " <- " file))
        (rescale-image file target-file photo-dimensions)
        (copy-to-s3 local-dir target-file)))

    (plays/save-production-data about-json')))
