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
  (remove nil? (map plays/get-future-audition-data (plays/get-all-future-productions)))
  ; todo: first filter out the past events, and then sort auditions by the soonest future event
  ;; todo bonus: do this dynamically in js!!!
  )

(def datetime-format (DateTimeFormatter/ofPattern "h:mma'END_TIME' EEEE, dd MMMM YYYY"))
(def date-format (DateTimeFormatter/ofPattern "EEEE, dd MMMM YYYY"))
(def end-time-format (DateTimeFormatter/ofPattern "' – 'h:mma','"))

; TODO: check for email addresses in the audition text!!! blow up if there are any!!!

(defn add-end-time [datetime-string end-time]
  (str/replace datetime-string
               "END_TIME"
               (if end-time
                 (.format end-time end-time-format)
                 "")))

(defn last-relevant-time [events]
  (let [lrt (reduce (fn [lrt {:keys [estimated-end-time]}]
                      (if (or (nil? lrt)
                              (-> estimated-end-time (.isAfter lrt)))
                        estimated-end-time
                        lrt))
                    nil
                    events)]
    (some-> lrt
            (.atZone (ZoneId/of "Europe/London"))
            (.toInstant)
            (.toEpochMilli)
            str)))

(defn parse-links [description]
  (when description
    (let [links (map (fn [[_ text url]]
                       [:a.normal {:href url :target "_blank"} text])
                     (re-seq #"\[([^\]]*)\]\(([^)]*)\)" description))
          bits (str/split description #"\[([^\]]*)\]\(([^)]*)\)")]
      [:span (interleave bits (concat links [""]))])))

(defn describe-character [{:keys [age gender description] :as character}]
  (into
    [:span
     [:b (str (:name character))]
     (when (or (contains? character :description)
               (contains? character :age)
               (contains? character :gender))
       " - ")]
    (interpose ", " (remove nil? [(when (contains? character :age) (or age "any age"))
                                  (when (contains? character :gender) (or gender "any gender"))
                                  (parse-links description)]))))

(defn add-footnotes [footnotes]
  (when (seq footnotes)
    [:div
     [:h5 "N.B."]
     (into [:ul]
           (for [footnote footnotes]
             [:li footnote]))]))

(core/page-2 "auditions" "The Archway Theatre"
  [:div.content

   [:div.content__item
    [:div.volunteer
     "The Archway Theatre is a volunteer run organisation."
     " We rely on the generosity of our members to keep the theatre running."
     " If you'd like to get involved then " [:a.normal {:href "join.html"} "click here to become a member"] "."]]

   [:div.content__item
    [:h1 "Audition Notices"]]

   (if-let [data (seq (grab-data-from-files))]
     (map
       (fn [{:keys [author director audition events characters footnotes start end contact] :as data}]
         (when-let [lrt (last-relevant-time events)]
           [:div.content__item
            [:div.audition.disappearable {:data-end lrt}
             [:h1 (:name data)]
             (when author [:div author])
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
             [:div (str "Playing dates: " (.format start date-format) " until " (.format end date-format))]
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
                  (add-footnotes footnotes)
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
                  (add-footnotes footnotes)
                  [:br]]))

             (let [{:keys [facebook-event]} contact]
               (cond
                 facebook-event [:div.audition__call-to-action [:a.normal
                                                                {:href facebook-event}
                                                                "Respond to the Facebook event"]]
                 :else [:div.audition__call-to-action "Email: " [:a.normal.delayedEmail "General Enquiries"]]))]]))
       (sort-by (juxt #(-> % :events first :datetime) :start :id) data))
     [:div.content__item
      [:div "There are currently no audition notices. Check back another time."]])]

  [:script {:src "./js/delayed.js"}])
