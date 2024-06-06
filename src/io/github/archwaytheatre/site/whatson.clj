(ns io.github.archwaytheatre.site.whatson
  (:require [clojure.string :as str]
            [io.github.archwaytheatre.data.plays :as plays]
            [io.github.archwaytheatre.site.core :as core])
  (:import [java.net HttpURLConnection URL]
           [java.time.format DateTimeFormatter]))


(defn grab-data-from-files []
  (->> (iterate inc 2024)
       (map plays/get-productions-for)
       (take-while seq)
       (mapcat identity)))

(defn get-trailer-url [id year trailer?]
  (let [trailer-url-str (str "https://archwaytheatre.s3.eu-west-2.amazonaws.com/site/" year "/" id "/trailer.mov")]
    (if trailer?
      trailer-url-str
      (let [url (URL. trailer-url-str)
            huc ^HttpURLConnection (.openConnection url)]
        (.setRequestMethod huc "HEAD")
        (when (= 200 (.getResponseCode huc))
          url)))))

(defn grab-data []
  (->> (grab-data-from-files)
       (remove plays/is-past?)
       (map (fn [production]
              (let [id (:production-code (meta production))
                    year (:production-year (meta production))
                    trailer? (:trailer production)]
                (assoc production :id id
                                  :year year
                                  :trailer-url (get-trailer-url id year trailer?)))))))

(def date-format (DateTimeFormatter/ofPattern "d MMM"))

(defn date-range [start-date end-date]
  (if (= start-date end-date)
    (.format start-date date-format)
    (str (.format start-date date-format) " - " (.format end-date date-format))))

(defn author-line [author director]
  (when-let [bits (seq (remove nil? [(when author (str "by " author))
                                     (when director (str "directed by " director))]))]
    (str/join ", " bits)))

(core/page-2
  "whatson" "What's On?"

  (let [data (grab-data)
        coming-soon-or-on-now (take 4 (sort-by :start (filter :ticketurl data)))
        soon? (set (map :id coming-soon-or-on-now))
        coming-later (sort-by :start (remove #(-> % :id soon?) data))]

    (-> [:div.content]
        (conj [:div])
        (conj [:div.trailer__holder
               [:video.trailer__video {:controls "controls"
                                       :width    "100%"
                                       :height   "100%"
                                       :name     "Archway Promotional Video"}
                [:source {:src "https://archwaytheatre.s3.eu-west-2.amazonaws.com/site/2023/Promo23.mov"}]]])
        (conj [:div.vertical-spacer])
        (conj [:h1 "What's On?"])
        (conj [:div.vertical-spacer])
        (into (for [{:keys [start end location name about-text ticketurl trailer-url year id author director] :as event}
                    coming-soon-or-on-now]
                [:div.event-holder
                 [:div.event-overview.event-focus {:id (str "event-overview-" id)}
                  [:a {:href ticketurl}
                   [:img.event-overview__poster {:src (str "https://archwaytheatre.s3.eu-west-2.amazonaws.com/"
                                                           "site/" year "/" id "/poster-scaled.png")}]]
                  [:div.event-overview__about
                   [:div.event-overview__details (str (date-range start end) " │ " location)]
                   [:div.event-overview__title [:h2 name]]
                   (some-> (author-line author director)
                           (vector :div.event-overview__author))
                   [:div.event-overview__text [:pre about-text]]
                   [:div.event-overview__buttons
                    [:a.whisper-to-action {:href (str "javascript:about('" id "')")} "More…"]
                    (when trailer-url [:a.whisper-to-action {:href (str "javascript:trailer('" id "')")} "Trailer"])
                    [:a.call-to-action {:href ticketurl} "Buy Tickets"]]]]

                 (when trailer-url
                   [:div.event-trailer {:id (str "event-trailer-" id)}
                    [:video.event-trailer__video {:id       (str "event-trailer__video-" id)
                                                  :controls "controls"
                                                  :width    "100%"
                                                  :height   "100%"
                                                  :name     "Archway Promotional Video"}
                     [:source {:src trailer-url}]]
                    [:div.event-trailer__buttons
                     [:a.whisper-to-action {:href (str "javascript:poster('" id "')")} "Back"]
                     [:a.call-to-action {:href ticketurl} "Buy Tickets"]]])

                 [:div.event-about {:id (str "event-about-" id)}
                  [:div.event-overview__details (str (date-range start end) " │ " location)]
                  [:div.event-overview__title [:h2 name]]
                  (some-> (author-line author director)
                          (vector :div.event-overview__author))
                  [:div.event-about__text [:pre about-text]]
                  [:div.event-overview__buttons
                   [:a.whisper-to-action {:href (str "javascript:poster('" id "')")} "Back"]
                   [:a.call-to-action {:href ticketurl} "Buy Tickets"]]
                  ]]))
        (conj [:div.vertical-spacer])
        (conj [:h1 "Coming Later..."])
        (conj [:div.vertical-spacer])
        (conj [:div.later-events
               (for [{:keys [id year ticketurl]} coming-later
                     :let [url (str "https://archwaytheatre.s3.eu-west-2.amazonaws.com/"
                                    "site/" year "/" id "/poster-scaled.png")]
                     :when (try (slurp url) (catch Exception _ nil))]
                 (if (< (rand) 0.5) ;ticketurl
                   [:a {:href ticketurl} [:img.later-events__image {:src url}]]
                   [:img.later-events__image {:src url}]))])

        (conj [:div.whatson-misc
               [:hr]
               [:div.vertical-spacer]
               [:div.text-align-center
                [:p "Check out our " [:a.normal {:href "events.html"} "events page"] " to see what else is going on."]
                [:p [:a.normal {:href "join.html"} "Become a member"] " to receive our newsletter."]
                [:p "Take a look at our " [:a.normal {:href "past/index.html"} "past productions."]]
                [:p "Check out our " [:a.normal {:href "https://www.ticketsource.co.uk/archwaytheatredigital/"} "digital content."]]]
               [:div.vertical-spacer]
               [:hr]
               [:div.vertical-spacer]
               [:div.whatson-misc__signup-form core/email-signup-form]])


        (conj [:script {:src "./js/whatson.js"}]))

    ))
