(ns io.github.archwaytheatre.site.index
  (:require [clojure.string :as str]
            [io.github.archwaytheatre.data.core :as data]
            [io.github.archwaytheatre.data.plays :as plays]
            [io.github.archwaytheatre.site.core :as core])
  (:import [java.net HttpURLConnection URL]
           [java.time LocalDate Year ZoneOffset]
           [java.time.format DateTimeFormatter]))

(defn grab-data-from-files []
  (->> (iterate inc (dec (.getValue (Year/now))))
       (map plays/get-productions-for)
       (take-while seq)
       (mapcat identity)))

(defn get-trailer-url [id year trailer?]
  (let [trailer-url-str (str data/asset-url-prefix year "/" id "/trailer.mov")]
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
       (filter #(plays/included? % "whatson"))
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
  (when-let [bits (seq (remove nil? [(when author author)
                                     (when director (str "directed by " director))]))]
    (str/join ", " bits)))

(defn to-start-millis [start]
  (* (.toEpochSecond (.atOffset (.atStartOfDay start) ZoneOffset/UTC)) 1000))

(defn to-end-millis [end]
  (to-start-millis (.plusDays end 1)))

(defn tickets-available? [production]
  (or (:ticketurl production)
      (:unticketed production)))

(core/page-2
  "index" "What's On?"
  ; todo: check *all* links in normal text (so not menu buttons, maybe others?) on website are underlined.

  ; todo: https://stackoverflow.com/questions/37824744/on-mobile-font-size-is-different-depending-on-the-number-of-paragraphs

  (let [data (grab-data)
        soon (.plusMonths (LocalDate/now) 3) ; 2 months look ahead, plus 1 month buffer of not being updated and re-running.
        ; todo: make github actions run weekly?
        coming-soon-or-on-now (->> data
                                   (filter tickets-available?)
                                   (sort-by :start)
                                   (take-while #(-> % :start plays/complete-date (.isBefore soon))))
        soon? (set (map :id coming-soon-or-on-now))
        coming-later (->> data
                          (remove #(-> % :id soon?))
                          (map #(assoc % :poster-url
                                         (str data/asset-url-prefix (:year %) "/" (:id %) "/poster-scaled.png")))
                          ; TODO: HEAD request instead of slurp!
                          (filter #(try (slurp (:poster-url %)) (catch Exception _ nil)))
                          (sort-by :start))
        coming-soon-ids (str/join "," (map #(str \' (:id %) \') coming-soon-or-on-now))]

    ; todo: check coming-later's when building - will they all have the text, links that they need, etc.?

    (-> [:div.content]

        (conj [:script {:async "true"
                        :src "https://www.googletagmanager.com/gtag/js?id=G-L92R8E0DYG"}])
        (conj [:script "window.dataLayer = window.dataLayer || [];
  function gtag(){dataLayer.push(arguments);}
  gtag('js', new Date());
  gtag('config', 'G-L92R8E0DYG');"])

        (conj [:script {:src "./js/whatson.js"}])

        (conj [:div])
        (conj [:div.trailer__holder.disappearable {:data-id "promo"}
               [:div.trailer__wrapper
                [:video#promo-video {:controls "controls"
                                     :width    "100%"
                                     :height   "100%"
                                     :name     "Archway Promotional Video"}
                 [:source {:src (str data/asset-url-prefix "2023/Promo23.mov")}]]
                [:div#trailer__cover
                 [:div#trailer__cover-text "Welcome!"
                  [:div#trailer__cover-subtext "Click to play video."]]
                 [:div#trailer__cover-play]]]])

        (conj [:div.vertical-spacer.disappearable {:data-id "spacer"}])
        (conj [:h1.disappearable {:data-id "title"} "What's On?"])
        (conj [:div.vertical-spacer.disappearable {:data-id "spacer"}])

        (into (for [{:keys [start end location about-text ticketurl unticketed trailer-url year id author director soldout]
                     {:keys [part-1 part-2 part-3]} :name-parts}
                    coming-soon-or-on-now]
                [:div.event-holder.disappearable {:data-end (to-end-millis end) :data-id (str "event-" id)}
                 [:div.event-overview.event-focus {:id (str "event-overview-" id)}
                  [:div.event-overview__banner-wrapper
                   [:div.event-overview__banner
                    {:data-start    (to-start-millis start)
                     :data-end      (to-end-millis end)
                     :data-one-day  (if (= start end) "true" "false")
                     :data-sold-out (if soldout "true" "false")}]]

                  (if unticketed
                    [:img {:src (str data/asset-url-prefix year "/" id "/poster-scaled.png")}]
                    [:a {:href ticketurl}
                     [:img.event-overview__poster {:src (str data/asset-url-prefix year "/" id "/poster-scaled.png")}]])

                  [:div.event-overview__about
                   ; todo: add some flex-grow to the css for these maybe?
                   ; todo: remove the 'More...' button when it's not needed?
                   [:div.event-overview__details (str (date-range start end) " │ " location)]
                   [:div.event-overview__title [:a.hidden {:href (str "index.html#event-" id)} [:h3 part-1] [:h2 part-2] [:h3 part-3]]]
                   (some->> (author-line author director)
                            (vector :div.event-overview__author))
                   [:div.event-overview__text.event-overview__text__faded {:id (str "event-overview__text-" id)}
                    [:pre {:id (str "event-overview__pre-" id)} about-text]
                    #_[:br]]
                   [:div.event-overview__buttons
                    [:a.whisper-to-action {:id   (str "event-overview__more-" id)
                                           :href (str "javascript:about('" id "')")} "More…"]
                    (when trailer-url
                      [:a.whisper-to-action {:href (str "javascript:trailer('" id "')")}
                       "Trailer" [:div.play-button "▶"]])

                    (when-not unticketed
                      [:a.call-to-action {:href ticketurl} "Buy Tickets"])]]]

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
                     (when-not unticketed [:a.call-to-action {:href ticketurl} "Buy Tickets"])]])

                 [:div.event-about {:id (str "event-about-" id)}
                  [:div.event-overview__details (str (date-range start end) " │ " location)]
                  [:div.event-overview__title [:a.hidden {:href (str "index.html#event-" id)} [:h3 part-1] [:h2 part-2] [:h3 part-3]]]
                  (some->> (author-line author director)
                           (vector :div.event-overview__author))
                  [:div.event-about__text [:pre about-text]]
                  [:div.event-overview__buttons
                   [:a.whisper-to-action {:href (str "javascript:poster('" id "')")} "Back"]
                   (when-not unticketed [:a.call-to-action {:href ticketurl} "Buy Tickets"])]
                  ]]))
        (conj [:div.vertical-spacer])
        (cond->
          (seq coming-later)
          (into [[:h1 "Coming Later..."]
                 [:div.vertical-spacer]
                 [:div.later-events
                  (for [{:keys [ticketurl poster-url]} coming-later]
                    (if ticketurl
                      [:a {:href ticketurl} [:img.later-events__image {:src poster-url}]]
                      [:img.later-events__image {:src poster-url}]))]]))

        (conj [:div.whatson-misc.disappearable {:data-id "misc"}
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


        ;(conj [:script {:src "./js/whatson.js"}])
        (conj [:script {:src "./js/video.js"}])
        (conj [:script (str "considerHiding(" coming-soon-ids ");")]))))
