(ns io.github.archwaytheatre.data.plays
  (:require [cheshire.core :as json]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [io.github.archwaytheatre.data.core :as data])
  (:import [java.time Year]))


(def main-house "Main House")
(def the-studio "Studio Theatre")

(defn load-production-data [production-year production-name]
  (let [prod-code (data/codify production-name)
        about-json-file (io/file data/data-dir (str production-year) prod-code "about.json")]
    (if (.exists about-json-file)
      (with-meta (json/parse-string (slurp about-json-file) keyword)
                 {:production-year production-year
                  :production-name production-name})
      (println about-json-file "does not exist!"))))

(defn save-production-data [m]
  (let [{:keys [production-year production-name]} (meta m)
        prod-code (data/codify production-name)
        about-json-file (io/file data/data-dir (str production-year) prod-code "about.json")]
    (when-not (and production-name production-year)
      (throw (ex-info "No meta data!!!" {})))
    (io/make-parents about-json-file)
    (spit about-json-file (json/generate-string m {:pretty true}))
    m))

(defn create-production
  ([year short-name]
   (create-production year nil short-name nil nil nil nil nil))
  ([year full-name short-name author director location start end]
   (save-production-data
     (with-meta {:name      full-name
                 :location  location
                 :start     start
                 :end       end
                 ;:ticketurl ticketurl
                 :author    author
                 :director  director}
                {:production-year year
                 :production-name short-name}))))

(def year-seq
  (let [first-year (Year/of 1940)
        latest-year (.plusYears (Year/now) 1)]
    (->> first-year
         (iterate #(.plusYears % 1))
         (take-while #(not (.isAfter % latest-year))))))

(defn get-productions-for [year]
  (let [year-dir (io/file data/data-dir (str year))]
    (remove nil? (for [production (vec (.list year-dir))]
                   (load-production-data (str year) production)))))

(def has-photos? :photo-sets)

(defn get-photos-for [production]
  (let [{:keys [production-year production-name]} (meta production)]
    (for [{:keys [photographer photo-offsets]} (:photo-sets production)
          [filename eyeline-offset] photo-offsets]
      (with-meta {:name           (:name production)
                  :photographer   photographer
                  :image-url      (str (io/file (str production-year) production-name filename))
                  :eyeline-offset eyeline-offset}
                 (meta production)))))
