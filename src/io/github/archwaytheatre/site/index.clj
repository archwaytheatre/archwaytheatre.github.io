(ns io.github.archwaytheatre.site.index
  (:require [cheshire.core :as json]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [io.github.archwaytheatre.data.plays :as plays]
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

(defn event-image [{:keys [id imageurl start ticketurl image-no-stretch year]}]
  [:div.eventimage {:id (str "eventimage-" id)}
   [:a {:href ticketurl}
    [:img {:class (core/classes (when image-no-stretch "unstretched"))
           :src   (or imageurl
                      (str "https://archwaytheatre.s3.eu-west-2.amazonaws.com/"
                           "site/" year "/" id "/poster-scaled.png"))}]]])

(def fallback-ticket-url "https://www.ticketsource.co.uk/whats-on/surrey/the-archway-theatre")

(defn event-data [{:keys [id name location soldout about-text ticketurl]} start-date end-date]
  [:div.eventdata {:id (str "eventdata-" id)}
   [:div.eventdatum.deets (str (date-range start-date end-date) " │ " location)]
   [:div.eventdatum.archwaytitle name]
   [:div.eventdatum.about
    [:div.fadeable {:id (str "overflow-a-" id)}
     ; todo: grab the director and author from the about.json, don't duplicate in the about text.
     [:pre {:id (str "overflow-b-" id)} about-text]]
    [:div.aboutmore {:id (str "aboutmore-" id)} [:a {:href (str "javascript:openAbout('" id "');")} "Read more..."]]]
   [:div.eventdatum.action
    (cond
      soldout [:a.fancy.soldout {:href ticketurl :title "This production is sold out."} "Join Waiting List"]
      ticketurl [:a.fancy {:href ticketurl} "Buy Tickets"]
      :else [:a.fancy.comingsoon {:title "Tickets are not yet on sale."} "Coming Soon!"])]
   [:div.timelabel
    {:data-start    (to-start-millis start-date)
     :data-end      (to-end-millis end-date)
     :data-one-day  (if (= start-date end-date) "true" "false")
     :data-sold-out (if soldout "true" "false")}]])

(defn event-about [{:keys [id name location soldout about-text ticketurl]} start-date end-date]
  [:div.eventabout {:id (str "eventabout-" id)}
   [:div.eventdatum.deets (str (date-range start-date end-date) " │ " location)]
   [:div.eventdatum.title name]
   [:div.eventdatum.about [:div [:pre about-text]]]
   [:div.eventdatum.aboutback [:a {:href (str "javascript:closeAbout('" id "');")} "Back"]]
   [:div.eventdatum.action
    (cond
      soldout [:a.fancy.soldout {:href ticketurl :title "This production is sold out."} "Join Waiting List"]
      ticketurl [:a.fancy {:href ticketurl} "Buy Tickets"]
      :else [:a.fancy.comingsoon {:title "Tickets are not yet on sale."} "Coming Soon!"])]])

(defn event-trailer [{:keys [name start id trailer year]}]
  (when (and start id trailer)
    (let [trailer-url (str "https://archwaytheatre.s3.eu-west-2.amazonaws.com/site/" year "/" id "/trailer.mov")]
      [:div.trailer-container
       [:div.trailer
        [:video {:controls "controls"
                 :width    "100%"
                 :height   "100%"
                 :name     name}
         [:source {:src trailer-url}]]]])))

(defn grab-data-from-files []
  (->> (iterate inc 2024)
       (map plays/get-productions-for)
       (take-while seq)
       (mapcat identity)))

(defn grab-data []
  (->> (grab-data-from-files)
       (remove plays/is-past?)
       (map (fn [production]
              (assoc production :id (:production-code (meta production))
                                :year (:production-year (meta production)))))))

(core/page "index" "The Archway Theatre"
  (let [data (grab-data)
        coming-soon-or-on-now (take 6 (sort-by :start (filter :ticketurl data)))
        soon? (set (map :id coming-soon-or-on-now))
        coming-later (sort-by :start (remove #(-> % :id soon?) data))]

    [:div.content

     [:script {:async "true"
               :src "https://www.googletagmanager.com/gtag/js?id=G-L92R8E0DYG"}]
     [:script "window.dataLayer = window.dataLayer || [];
  function gtag(){dataLayer.push(arguments);}
  gtag('js', new Date());
  gtag('config', 'G-L92R8E0DYG');"]

     #_[:div.trailer-container
      [:div.trailer
       (core/you-tube "ph-pvXha6z4")]]

     [:div.trailer-container
      [:div.trailer
       [:video {:controls "controls"
                ;:width "800"
                ;:height "600"
                :width "100%"
                :height "100%"
                :name "Video Name"}
        [:source {:src "https://archwaytheatre.s3.eu-west-2.amazonaws.com/site/2024/much-ado/trailer.mov"}]]]]
     ; todo: (event-trailer event') for most imminent event with a trailer, else archway promo video


     [:div.vspace]
     [:div.center.archwaytitle [:span.larger "Coming Up"]] ; todo: js to hoist 'On Now' productions above 'coming up'
     [:div.vspace]
     [:div.events
      (for [{:keys [trailer start end] :as event} coming-soon-or-on-now]
        ; todo trailer!
        (let [event' event ;(update event :ticketurl #(or % fallback-ticket-url))
              ]
          [:div.event.disappearable {:data-end (to-end-millis end)}
           (event-image event')
           (event-data event' start end)
           (event-about event' start end)]))


      [:div.center.archwaytitle [:span.larger "Coming Later..."]]
      [:div.vspace]

      [:div.laterevents
       (for [{:keys [id year]} coming-later
             :let [url (str "https://archwaytheatre.s3.eu-west-2.amazonaws.com/"
                            "site/" year "/" id "/poster-scaled.png")]
             :when (try (slurp url) (catch Exception e nil))]
         [:img.latereventimage {:src url}])]]

     [:div.vspace]

     [:section.container
      [:hr]
      [:div.vspace]
      [:div.center
       [:div.larger
        "Check out our " [:a.simple {:href "events.html"} "events page"] " to see what else is going on."]
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
      [:div.vspace]
      [:hr]
      [:div.vspace]
      [:div.signupform core/email-signup-form]]

     [:script {:src "./js/popup.js"}]

     [:script (str/join ";" (map (fn [{:keys [id]}]
                                   (str "hideMore('" id "')"))
                                 (map second data)))]
     [:script {:src "./js/datetime.js"}]])
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
