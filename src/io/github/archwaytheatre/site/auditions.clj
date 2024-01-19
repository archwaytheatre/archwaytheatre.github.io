(ns io.github.archwaytheatre.site.auditions
  (:require
    [clojure.java.io :as io]
    [clojure.string :as str]
    [clojure.string :as string]
    [io.github.archwaytheatre.site.core :as core]
    [cheshire.core :as json])
  (:import
    [java.time Instant LocalDateTime ZoneId]
    [java.time.format DateTimeFormatter]))


(defn interleave-all [coll1 coll2]
  (take (+ (count coll1) (count coll2))
        (remove nil? (interleave (concat coll1 (repeat nil))
                                 (concat coll2 (repeat nil))))))

(defn re-sort [& things]
  (let [volunteering (shuffle (filter #(-> % first name (string/includes? "volunteer")) things))
        other (shuffle (filter #(-> % first name (string/includes? "other")) things))
        audition (filter #(-> % first name (string/includes? "audition")) things)
        everything-else (interleave-all
                          other
                          audition)]
    (concat audition other volunteering)
    #_(interleave-all volunteering everything-else)))

(defn grab-data-from-files []
  (let [year-dirs (->> (iterate inc 2024)
                       (map #(io/file "data" (str %)))
                       (take-while #(.exists %)))]
    (for [year-dir year-dirs
          play-dir (->> (file-seq year-dir)
                        (remove #(= year-dir %))
                        (filter #(.isDirectory %)))
          :let [audition-file (io/file play-dir "audition.txt")
                audition-json (io/file play-dir "audition.json")
                about-file (io/file play-dir "about.json")]
          :when (and (.exists audition-file)
                     (.exists audition-json)
                     (.exists about-file))
          :let [audition-data (json/parse-string (slurp audition-json) keyword)]
          :when (seq (filter (fn [event]
                               (.isAfter (.plusHours (LocalDateTime/parse (:datetime event)) 3)
                                         (LocalDateTime/ofInstant (Instant/now) (ZoneId/of "Europe/London"))))
                             (:events audition-data)))]
      (let [about (json/parse-string (slurp about-file) keyword)
            audition-text (slurp audition-file)]
        (merge (select-keys about [:name :author :director :start])
               audition-data
               {:id       (.getName play-dir)
                :year     (.getName year-dir)
                :audition audition-text})))))

(def datetime-format (DateTimeFormatter/ofPattern "h:mma EEEE, dd MMMM YYYY"))

(core/page "auditions" "The Archway Theatre"
  [:section.container
   [:div.content

    [:div.getinvolved.volunteer
     "The Archway Theatre is a volunteer run organisation."
     " We rely on the generosity of our members to keep the theatre running."
     " If you'd like to get involved then " [:a.simple {:href "join.html"} "click here to become a member"] "."]

    [:h1 "Audition Notices"]

    (if-let [data (seq (grab-data-from-files))]
      (map
        (fn [{:keys [author director audition events characters] :as data}]
          [:div.getinvolved.audition
           [:h1 (:name data)]
           [:div (str "by " author)]
           [:div (str "directed by " director)]
           [:br]
           [:h3 "Auditions: "]
           (into [:div]
                 (map (fn [{:keys [datetime location description]}]
                        [:div
                         (->> [(.format (LocalDateTime/parse datetime) datetime-format)
                               location
                               description]
                              (remove nil?)
                              (str/join " "))])
                      events))
           [:br]
           [:h3 "About the Production:"]
           [:div [:pre (str/replace audition "\n" "\n\n")]]
           [:br]
           [:h3 "Characters:"]
           (into [:ul]
                 (map (fn [{:keys [age gender description] :as character}]
                        (println character)
                        [:li (str (:name character) " - "
                                  (or age "any age")
                                  ", "
                                  (or gender "any gender")
                                  (when description (str ", " description)))])
                      characters))
           [:br]
           [:div.calltoaction "Email: " [:a.simple.delayedEmail "General Enquiries"]]])
        (sort-by (juxt :start :id) data))
      [:div
       [:div "There are currently no audition notices. Check back another time."]])]]

  [:script {:src "./js/delayed.js"}]
  )
