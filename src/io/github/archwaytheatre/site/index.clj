(ns io.github.archwaytheatre.site.index
  (:require [cheshire.core :as json]
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

(core/page "index" "The Archway Theatre"
  [:div.content
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
               [:div.eventdatum location]
               [:div.eventdatum.bold (font-size name) name]
               [:div.eventdatum (date-range start-date end-date)]
               [:div.eventdatum [:a {:href (str "javascript:showPopup(\"popup-" idx "\");")} "About"]]
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
    [:div.larger "...or " [:a {:href "join.html"} "become a member"] " to receive our newsletter."]]
   [:br]
   [:br]
   [:br]

   [:script {:src "./js/datetime.js"}]])
