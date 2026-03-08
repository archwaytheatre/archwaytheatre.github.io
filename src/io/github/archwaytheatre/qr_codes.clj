(ns io.github.archwaytheatre.qr-codes
  (:require [clojure.java.io :as io])
  (:import [io.nayuki.qrcodegen QrCode QrCode$Ecc]
           [java.awt Color]
           [java.awt.image BufferedImage]
           [java.io File]
           [javax.imageio ImageIO]))

(defn to-image [^QrCode qr pixels-per-square border-width-squares light-color dark-color]
  (let [size (+ (.size qr) (* border-width-squares 2))
        result (BufferedImage. (* size pixels-per-square) (* size pixels-per-square) BufferedImage/TYPE_INT_RGB)
        g (.createGraphics result)]

    (.setColor g light-color)
    (.fillRect g 0 0 (.getWidth result) (.getHeight result))

    (.setColor g dark-color)
    (doseq [y (range (.size qr))
            x (range (.size qr))
            :when (.getModule qr x y)]
      (.fillRect g
                 (* (+ x border-width-squares) pixels-per-square)
                 (* (+ y border-width-squares) pixels-per-square)
                 pixels-per-square
                 pixels-per-square))
    
    (.dispose g)
    result))

(defn write-png [^BufferedImage img ^File file]
  (io/make-parents file)
  (ImageIO/write img "png" file))

(defn make-qr-code [text pixels-per-square file]
  (-> (QrCode/encodeText text QrCode$Ecc/MEDIUM)
      (to-image pixels-per-square 4 Color/WHITE (Color. 172 1 80))
      (write-png (io/file file))))

(comment

  (def output-dir (io/file "./qr-codes"))

  (make-qr-code "https://archwaytheatre.co.uk/" 10 (io/file output-dir "index-qr-code.png"))
  (make-qr-code "https://archwaytheatre.co.uk/programmes.html" 10 (io/file output-dir "programmes-qr-code.png"))

  )
