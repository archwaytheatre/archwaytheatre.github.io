(ns io.github.archwaytheatre.site.auditions
  (:require
    [clojure.java.io :as io]
    [clojure.string :as str]
    [clojure.string :as string]
    [io.github.archwaytheatre.data.plays :as plays]
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
  (remove nil? (map plays/get-audition-data (plays/get-all-future-productions))))

(def datetime-format (DateTimeFormatter/ofPattern "h:mma EEEE, dd MMMM YYYY"))

; TODO: check for email addresses in the audition text!!! blow up if there are any!!!

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
           (->> events
                (filter #(-> % :datetime (.isAfter (LocalDateTime/ofInstant (Instant/now) (ZoneId/of "Europe/London")))))
                (map (fn [{:keys [datetime location description]}]
                       [:div
                        (->> [(.format datetime datetime-format)
                              location
                              description]
                             (remove nil?)
                             (str/join " "))]))
                (into [:div]))
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
        (sort-by (juxt #(-> % :events first :datetime) :start :id) data))
      [:div
       [:div "There are currently no audition notices. Check back another time."]])]]

  [:script {:src "./js/delayed.js"}]
  )
