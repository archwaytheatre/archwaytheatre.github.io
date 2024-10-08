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
  (remove nil? (map plays/get-audition-data (plays/get-all-future-productions)))
  ; todo: first filter out the past events, and then sort auditions by the soonest future event
  ;; todo bonus: do this dynamically in js!!!
  )

(def datetime-format (DateTimeFormatter/ofPattern "h:mma'END_TIME' EEEE, dd MMMM YYYY"))
(def end-time-format (DateTimeFormatter/ofPattern "' – 'h:mma','"))

; TODO: check for email addresses in the audition text!!! blow up if there are any!!!

(defn add-end-time [datetime-string end-time]
  (str/replace datetime-string
               "END_TIME"
               (if end-time
                 (.format end-time end-time-format)
                 "")))

(defn last-relevant-time [events]
  (let [lrt (reduce (fn [lrt {:keys [datetime end-time]}]
                      (let [time (or end-time (.plusHours datetime 3))]
                        (if (-> time (.isAfter lrt))
                          time
                          lrt)))
                    (LocalDateTime/now)
                    events)]
    (-> lrt
        (.atZone (ZoneId/of "Europe/London"))
        (.toInstant)
        (.toEpochMilli)
        str)))

(defn describe-character [{:keys [age gender description] :as character}]
  (str (:name character) " - "
       (str/join ", "
         (remove nil?
                 [(when (contains? character :age) (or age "any age"))
                  (when (contains? character :gender) (or gender "any gender"))
                  description]))))

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
          [:div.getinvolved.audition.disappearable {:data-end (last-relevant-time events)}
           [:h1 (:name data)]
           (when author [:div (str "by " author)])
           [:div (str "directed by " director)]
           [:br]
           [:h3 "Auditions: "]
           (->> events
                (filter #(-> % :datetime (.isAfter (LocalDateTime/ofInstant (Instant/now) (ZoneId/of "Europe/London")))))
                (map (fn [{:keys [datetime end-time location description]}]
                       [:div
                        (->> [(add-end-time (.format datetime datetime-format) end-time)
                              location
                              description]
                             (remove nil?)
                             (str/join ", "))]))
                (into [:div]))
           [:br]
           [:h3 "About the Production:"]
           [:div [:pre audition]]
           [:br]
           (when-let [play->characters (some->> (seq characters) (group-by :play))]
             (if (= 1 (count play->characters))
               [:div
                [:h3 "Roles:"]
                (into [:ul]
                      (map (fn [character]
                             (if (vector? character)
                               [:li {:style "border-left:1px solid white;border-radius:0.5em;padding-left:5px;"}
                                (interpose [:span.center #_#_[:br] "&nbsp;&nbsp;&nbsp;doubled with" [:br]]
                                           (map describe-character character))]
                               [:li (describe-character character)]))
                           characters))
                [:span "Ages are a rough guide."]
                [:br]]
               [:div
                [:h3 "Roles:"]
                (into [:div]
                      (map (fn [[play characters]]
                             [:div
                              [:h4 play]
                              (into [:ul]
                                    (map (fn [character]
                                           (if (vector? character)
                                             [:li {:style "border-left:1px solid white;border-radius:0.5em;padding-left:5px;"}
                                              (interpose [:span.center #_#_[:br] "&nbsp;&nbsp;&nbsp;doubled with" [:br]]
                                                         (map describe-character character))]
                                             [:li (describe-character character)]))
                                         characters))])
                           play->characters))
                [:span "Ages are a rough guide."]
                [:br]]))
           [:div.calltoaction "Email: " [:a.simple.delayedEmail "General Enquiries"]]])
        (sort-by (juxt #(-> % :events first :datetime) :start :id) data))
      [:div
       [:div "There are currently no audition notices. Check back another time."]])]
   [:script {:src "./js/datetime.js"}]]

  [:script {:src "./js/delayed.js"}]
  )
