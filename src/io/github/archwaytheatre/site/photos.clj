(ns io.github.archwaytheatre.site.photos
  (:require
    [clojure.java.io :as io]
    [clojure.string :as str]
    [io.github.archwaytheatre.data.plays :as plays]
    [io.github.archwaytheatre.site.core :as core]))

(defn js-quote [x] (str \' x \'))

(defn js-photo-def [def-name photos & field-fns]
  (let [photo-data-lines (for [photo photos]
                           (str "[" (str/join ", " (map #(% photo) field-fns)) "]"))]
    (str "const " def-name " = [\n" (str/join ",\n  " photo-data-lines) "\n];")))

(defn build-top-photo-data [production-lookback-count]
  (let [productions-with-photos (->> (reverse plays/year-seq)
                                     (mapcat plays/get-productions-for)
                                     (filter plays/has-photos?)
                                     (take production-lookback-count))
        photos (mapcat plays/get-photos-for productions-with-photos)
        data-file (io/file core/parent-dir "js" "top-photo-data.js")]
    (io/make-parents data-file)
    (spit data-file (js-photo-def "topPhotoData" photos
                                  :eyeline-offset
                                  (comp js-quote :image-url)
                                  (comp js-quote :photographer)
                                  (comp js-quote :production-year meta)
                                  (comp js-quote :name)))))

(defn build-about-photo-data [production-lookback-count]
  (let [productions-with-photos (->> (reverse plays/year-seq)
                                     (mapcat plays/get-productions-for)
                                     (filter plays/has-photos?)
                                     (take production-lookback-count))
        photos (mapcat plays/get-photos-for productions-with-photos)
        data-file (io/file core/parent-dir "js" "about-photo-data.js")]
    (io/make-parents data-file)
    (spit data-file (js-photo-def "photoData" (shuffle photos)
                                  (comp js-quote :image-url)
                                  (comp js-quote :photographer)
                                  (comp js-quote :production-year meta)
                                  (comp js-quote :name)))))

(build-top-photo-data 3)
(build-about-photo-data 3)
