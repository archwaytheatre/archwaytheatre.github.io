(ns io.github.archwaytheatre.data.plays
  (:require [cheshire.core :as json]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [io.github.archwaytheatre.data.core :as data])
  (:import [java.time Duration LocalDate Year YearMonth LocalDateTime Instant ZoneId]))


(def main-house "Main House")
(def the-studio "Studio Theatre")

(defn decade [year]
  (str (subs year 0 3) "0"))

(defn prod-dir [prod-year prod-code]
  (io/file data/event-dir (decade prod-year) (str prod-year) prod-code))

(defn parse-partial-date [date-str]
  (when date-str
    (case (count date-str)
      4 (Year/parse date-str)
      7 (YearMonth/parse date-str)
      10 (LocalDate/parse date-str))))

(defmulti is-past? (comp type :end))
(defmethod is-past? Year [production]      (-> (Year/now) (.isAfter (:end production))))
(defmethod is-past? YearMonth [production] (-> (YearMonth/now) (.isAfter (:end production))))
(defmethod is-past? LocalDate [production] (-> (LocalDate/now) (.isAfter (:end production))))

(defmulti complete-date type)
(defmethod complete-date LocalDate [x] x)
(defmethod complete-date YearMonth [x] (.atDay x 1))
(defmethod complete-date Year [x] (.atDay x 1))

(defn update-name [production]
  (let [prod-name (:name production)
        names (cond
                (string? prod-name) {:name   prod-name
                                     :name-parts {:part-2 prod-name}}
                (and (vector? prod-name)
                     (= 3 (count prod-name))) {:name   (str/join " " (remove nil? prod-name))
                                               :name-parts {:part-1 (first prod-name)
                                                            :part-2 (second prod-name)
                                                            :part-3 (last prod-name)}}
                :else (throw (ex-info "Don't understand name." {:name prod-name})))]
    (merge production names)))

(defn load-production-data [production-year production-name]
  (let [prod-code (data/codify production-name)
        play-dir (prod-dir production-year prod-code)
        about-json-file (io/file play-dir "about.json")]
    (if (.exists about-json-file)
      (let [about-data (json/parse-string (slurp about-json-file) keyword)
            about-text-file (io/file play-dir "about.txt")
            about-text (when (.exists about-text-file)
                         (str/trim (slurp (io/file play-dir "about.txt"))))]
        (with-meta (-> about-data
                       update-name
                       (assoc :about-text about-text)
                       (update :start parse-partial-date)
                       (update :end parse-partial-date)
                       (update :exclusions set))
                   {:production-year production-year
                    :production-name production-name
                    :production-code prod-code}))
      (println about-json-file "does not exist!"))))

(defn mutate-text-file [target-file transform]
  (let [file (io/file target-file)]
    (->> (slurp file)
         (transform)
         (spit target-file))))

(defn even-prettier-ify [text]
  (-> text
      (str/replace "[ [" "[\n      [")
      (str/replace "], [" "],\n      [")))

(defn save-production-data [m]
  (let [{:keys [production-year production-name production-code]} (meta m)
        about-json-file (io/file (prod-dir production-year production-code) "about.json")
        about-text-file (io/file (prod-dir production-year production-code) "about.txt")]
    (when-not (and production-name production-year)
      (throw (ex-info "No meta data!!!" {})))
    (io/make-parents about-json-file)
    (spit about-json-file (json/generate-string (-> m
                                                    (dissoc :about-text)
                                                    (update :start #(some-> % str))
                                                    (update :end #(some-> % str)))
                                                {:pretty true}))
    (some->> (:about-text m) (spit about-text-file))
    (mutate-text-file about-json-file even-prettier-ify)
    m))

(defn create-production
  ([short-name m]
   (save-production-data
     (with-meta m
                {:production-year (str (.getYear (LocalDate/parse (:start m))))
                 :production-name short-name
                 :production-code (data/codify short-name)})))
  ([year full-name short-name author director location start end]
   (save-production-data
     (with-meta {:name      full-name
                 :location  location
                 :start     start
                 :end       end
                 :author    author
                 :director  director}
                {:production-year (str year)
                 :production-name short-name
                 :production-code (data/codify short-name)}))))

(defn- parse-dates [{:keys [datetime duration] :as event}]
  (let [start (LocalDateTime/parse datetime)
        end (some->> duration Duration/parse .toSeconds (.plusSeconds start))]
    (assoc event :datetime start
                 :end-time end)))

(defn get-audition-data [production]
  (let [{:keys [production-year production-code]} (meta production)
        play-dir (prod-dir production-year production-code)
        audition-file (io/file play-dir "audition.txt")
        audition-json (io/file play-dir "audition.json")]
    (when (and (.exists audition-file)
               (.exists audition-json))
      (let [audition-data (json/parse-string (slurp audition-json) keyword)]
        (when (seq (filter (fn [event]
                             (.isAfter (.plusHours (LocalDateTime/parse (:datetime event)) 3)
                                       (LocalDateTime/ofInstant (Instant/now) (ZoneId/of "Europe/London"))))
                           (:events audition-data)))
          (let [audition-text (slurp audition-file)]
            (merge (select-keys production [:name :author :director :start])
                   (update audition-data :events #(sort-by :datetime (map parse-dates %)))
                   {:id       (.getName play-dir)
                    :year     production-year
                    :audition audition-text})))))))

(def year-seq
  (let [first-year (Year/of 1940)
        latest-year (.plusYears (Year/now) 1)]
    (->> first-year
         (iterate #(.plusYears % 1))
         (take-while #(not (.isAfter % latest-year))))))

(defn get-productions-for [year]
  (let [year-dir (io/file data/event-dir (decade (str year)) (str year))]
    (->> (for [production (vec (.list year-dir))]
           (load-production-data (str year) production))
         (remove nil?)
         (remove :cancelled))))

(defn get-all-future-productions []
  (let [years (take 3 (iterate #(.plusYears % 1) (Year/now)))]
    (remove is-past? (sort-by :start (apply concat (map get-productions-for years))))))

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
