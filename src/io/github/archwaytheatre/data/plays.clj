(ns io.github.archwaytheatre.data.plays
  (:require [cheshire.core :as json]
            [clojure.java.io :as io]
            [io.github.archwaytheatre.data.core :as data]))


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
   (create-production year nil short-name nil nil nil nil nil nil))
  ([year full-name short-name author director location start end ticketurl]
   (save-production-data
     (with-meta {:name      full-name
                 :location  location
                 :start     start
                 :end       end
                 :ticketurl ticketurl
                 :author    author
                 :director  director}
                {:production-year year
                 :production-name short-name}))))
