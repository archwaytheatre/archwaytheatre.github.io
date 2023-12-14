(ns io.github.archwaytheatre.site.index
  (:require [cheshire.core :as json]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [io.github.archwaytheatre.site.core :as core])
  (:import [java.time LocalDate ZoneOffset]
           [java.time.format DateTimeFormatter]))


(def location->class {"Main House" "main"
                      "The Studio" "studio"
                      "Digital"    "digital"})

(defn font-size [text]
  (core/style
    {:font-size (str "calc(" (/ 0.2 (Math/sqrt (count text))) " * var(--my-vw))")}))

(defn sort-order [order content-item]
  (with-meta content-item {:order order}))

(defn sorted [content]
  (sort-by (comp :order meta) content))

(def date-format (DateTimeFormatter/ofPattern "d MMM"))

(defn date-range [start-date end-date]
  (if (= start-date end-date)
    (.format start-date date-format)
    (str (.format start-date date-format) " - " (.format end-date date-format))))

(defn to-start-millis [start]
  (* (.toEpochSecond (.atOffset (.atStartOfDay start) ZoneOffset/UTC)) 1000))

(defn to-end-millis [end]
  (to-start-millis (.plusDays end 1)))

(defn load-event-data []
  (->> (json/parse-string (slurp "data/whatson.json") keyword)
       (sort-by :start)
       (map-indexed vector)))

(defn event-image [{:keys [id imageurl start ticketurl image-no-stretch]}]
  (let [year (.getYear start)]
    [:div.eventimage
     [:a {:href ticketurl}
      [:img {:class (core/classes (when image-no-stretch "unstretched"))
             :src   (or imageurl
                        (str "https://archwaytheatre.s3.eu-west-2.amazonaws.com/"
                             "site/" year "/" id "/poster-scaled.png"))}]]]))

(def fallback-ticket-url "https://www.ticketsource.co.uk/whats-on/surrey/the-archway-theatre")

(defn event-data [{:keys [name location soldout about ticketurl]} start-date end-date]
  [:div.eventdata
   [:div.eventdatum.deets (str (date-range start-date end-date) " │ " location)]
   [:div.eventdatum.title name]
   [:div.eventdatum.about [:div [:pre about]]]
   [:div.eventdatum.action
    (cond
      soldout [:a.fancy.soldout {:href ticketurl :title "This production is sold out."} "Join Waiting List"]
      ticketurl [:a.fancy {:href ticketurl} "Buy Tickets"]
      :else [:a.fancy.comingsoon {:title "Tickets are not yet on sale."} "Coming Soon!"])]
   [:div.timelabel
    {:data-start    (to-start-millis start-date)
     :data-end      (to-end-millis end-date)
     :data-sold-out (if soldout "true" "false")}]])

(defn grab-data-from-files []
  (let [year-dirs (->> (iterate inc 2023)
                       (map #(io/file "data" (str %)))
                       (take-while #(.exists %)))]

    (for [year-dir year-dirs
          play-dir (->> (file-seq year-dir)
                        (remove #(= year-dir %))
                        (filter #(.isDirectory %)))]
      (let [about-data (-> (io/file play-dir "about.json")
                           (slurp)
                           (json/parse-string keyword))
            about-text (-> (io/file play-dir "about.txt")
                           (slurp)
                           (str/trim))]
        (assoc about-data
          :id (.getName play-dir)
          :year (.getName year-dir)
          :about about-text)))))

(defn grab-data []
  (let [today (LocalDate/now)]
    (->> (grab-data-from-files)
         (map (fn [event]
                (-> event
                    (update :start #(LocalDate/parse %))
                    (update :end #(LocalDate/parse %)))))
         (sort-by :end)
         (drop-while #(.isBefore (:end %) today))
         (sort-by :start)
         (take 4)
         (map-indexed vector))))

(core/page "index" "The Archway Theatre"
  [:div.content
   [:div.trailer-container
    [:div.trailer
     (core/you-tube "ph-pvXha6z4")]]
   [:div.events
    (for [[idx {:keys [trailer start end] :as event}] (grab-data)]
      ; todo trailer!
      (let [event' event ;(update event :ticketurl #(or % fallback-ticket-url))
            ]
        [:div.event.disappearable {:id       (str "event-" idx)
                                   :data-end (to-end-millis end)}
         (event-image event')
         (event-data event' start end)]))]

   [:div.center
    [:div.larger "That's all for now! Check back soon for more..."]
    [:br]
    [:br]
    [:div.larger
     [:a.simple {:href "join.html"} "Become a member"] " to receive our newsletter."]
    [:br]
    [:div.larger
     "Take a look at our " [:a.simple {:href "past/index.html"} "past productions."]]
    [:br]
    [:div.larger
     "Check out our " [:a.simple {:href "https://www.ticketsource.co.uk/archwaytheatredigital/"} "digital content."]]
    ]
   [:br]
   [:br]
   [:br]

   [:script {:src "./js/datetime.js"}]]
  #_[:div.content
   [:div.events.dark {:onload "composeLabels()"}
    (sorted
      (for [[idx {:keys [name location soldout trailer about imageurl image-no-stretch start end]}]
            (map-indexed vector (json/parse-string (slurp "data/whatson.json") keyword))]
        (let [link-href (if (= location "Digital")
                          "https://www.ticketsource.co.uk/archwaytheatredigital/"
                          "https://www.ticketsource.co.uk/archwaytheatre/")
              start-date (LocalDate/parse start)
              end-date (LocalDate/parse end)]
          (sort-order start
            [:div {:id       (str "event-" idx)
                   :class    "disappearable"
                   :data-end (to-end-millis end-date)}
             (if trailer
               [:div.trailer
                (core/you-tube trailer)]
               [:br])
             [:div {:class (core/classes "event" (location->class location "unknown"))}
              [:div.eventimage
               [:a {:href link-href}
                [:img {:class (core/classes (when image-no-stretch "unstretched"))
                       :src imageurl}]]]
              [:div.eventdata
               [:div.timelabel
                {:data-start (to-start-millis start-date)
                 :data-end   (to-end-millis end-date)}]
               [:div.eventdatum (str (date-range start-date end-date) " │ " location " │ 7:45pm" )]
               [:div.eventdatum.bold (font-size name) name]
               [:div.eventdatum ""]
               [:div.eventdatum [:a {:href (str "javascript:showPopup(\"popup-" idx "\");")} about]]
               [:div.eventdatum.bold
                (if soldout
                  [:div.soldout {:title "There are no tickets left."} "Sold Out!"]
                  [:a.button {:href link-href} "Buy Tickets"])]]
              [:div.popup {:id (str "popup-" idx)}
               [:div.alignright.x
                [:a.short
                 {:href "javascript:hidePopups();"}
                 "✕"]]
               [:pre.about about]
               [:div.controls
                [:a.staticButton
                 {:href "javascript:hidePopups();"}
                 [:div "Back"]]
                (if soldout
                  [:div.soldout.compact {:title "There are no tickets left."} "Sold Out!"]
                  [:a.button.staticButton
                   {:href    link-href
                    :onclick "event.stopPropagation();"}
                   "Buy Tickets"])]]]]))))]

   [:div.center
    [:div.larger "That's all for now! Check back soon for more..."]
    [:br]
    [:br]
    [:div.larger
     [:a {:href "join.html"} "Become a member"] " to receive our newsletter."]
    [:br]
    [:div.larger
     "Take a look at our " [:a {:href "past/index.html"} "past productions."]]
    [:br]
    [:div.larger
     "Check out our " [:a {:href "https://www.ticketsource.co.uk/archwaytheatredigital/"} "digital content."]]
    ]
   [:br]
   [:br]
   [:br]

   [:script {:src "./js/datetime.js"}]])
