(ns io.github.archwaytheatre.site.past.index
  (:require [cheshire.core :as json]
            [clojure.string :as str]
            [io.github.archwaytheatre.data.core :as data]
            [io.github.archwaytheatre.data.plays :as plays]
            [io.github.archwaytheatre.site.photos :as photos]
            [io.github.archwaytheatre.site.core :as core])
  (:import [java.time LocalDate Month Year YearMonth]
           [java.time.format DateTimeParseException]))


(defn production-page [year month location name page-name prev-page-name next-page-name blurb about-text photos]
  (core/page-2 (str "past/" page-name) name
    ;[:script {:src "../js/carousel.data.js"}]
    ;[:script {:src "../js/carousel.js"}]
    [:script {:type "text/javascript"} (photos/js-photo-def "photoData" photos
                                                            (comp photos/js-quote :image-url)

                                                            (comp photos/js-quote :photographer)
                                                            #_(fn [_]
                                                              (photos/js-quote (rand-nth ["Bob" "Margaret" "Sammy" "Johnstone"])))

                                                            )]
    ;[:script {:src "../js/photos.js"}]
    (let [links [:div.center
                 (if prev-page-name
                   [:a.normal {:href (str prev-page-name ".html")} "Previous"]
                   [:span.past__disabled "Previous"])
                 "|"
                 [:a.normal {:href "index.html"} "All productions"]
                 "|"
                 (if next-page-name
                   [:a.normal {:href (str next-page-name ".html")} "Next"]
                   [:span.past__disabled "Next"])]]

      [:div.content

       [:div.content__item links]

       [:div.content__item [:div.past__year year]]

       (when month [:div.content__item [:div.past__month (str "☙ " (str/capitalize month) " ❧")]])

       [:div.content__item [:div.past__production name]]

       [:div.content__item
        [:div.text-align-center [:p [:pre.about blurb]]]
        [:div.text-align-center [:p (when location (str "Performed in the " location "."))]]]

       [:div.content__item [:div#photoCarousel]]

       [:div.content__item [:p [:pre.about about-text]]]

        ;[:script {:type "text/javascript"} (str "startCarousel('carousel1',imagesForPlay('" name "'));")]
        ;[:script {:type "text/javascript"} (str "init();")]

        ;[:br]
        ;[:br]
        (for [{:keys [image-url photographer]} photos]
          [:div.content__item
           [:div.static-photo__container
            [:img.static-photo__image {:src     (str data/asset-url-prefix image-url)
                                       :alt     (str "A scene from '" name "' at The Archway Theatre, " year)
                                       :loading "lazy"}]
            [:div.static-photo__credit
             (if (= "Archway Archive" photographer)
               [:span (str "Photo from the Archway Archive*")]
               [:span (str "Photo by " photographer ". Used with permission.")])]]])

       (when (some #(= "Archway Archive" %) (map :photographer photos))
         [:div.content__item
          "* If you know how took these pictures then please " [:a.normal {:href "../contact.html"} "get in touch."]])

       [:div.content__item links]])))

(defn daisy-chain [f c]
  (for [[a b c] (partition-all 3 1 (cons nil c))
        :when b]
    (f a b c)))

(defn month-from [date-str]
  (try
    (.getMonthValue (YearMonth/parse date-str))
    (catch DateTimeParseException _)))

(defn indefinite-articlize [x]
  (if (#{\a \e \i \o \u} (first (str/lower-case x)))
    (str "An " x)
    (str "A " x)))

(defn index-all-years []
  (let [year->productions (->> (for [year (map #(.getValue ^Year %) (reverse plays/year-seq))
                                     {:keys [name] :as production} (->> (plays/get-productions-for year)
                                                                        (sort-by (comp plays/complete-date :start))
                                                                        (reverse))
                                     :when (plays/is-past? production)
                                     :when (plays/included? production "pastproductions")]
                                 (assoc production
                                   :year year
                                   :month (-> production :start plays/complete-date Month/from)
                                   :photos (plays/get-photos-for production)
                                   :page-name (core/linkify name year)))
                               (daisy-chain #(assoc %2 :prev (:page-name %3)
                                                       :next (:page-name %1)))
                               (group-by :year))]
    (for [[year productions] (sort-by key > year->productions)]
      ;[:div.content__item]
      [:div.past__year-index
       [:div.past__year-background year]
       (for [{:keys [name prev page-name next location month young-adults-youth
                     company director author photos about-text]} (sort-by #(.getValue (:month %)) > productions)]
         (let [blurb-bits [(str (indefinite-articlize (or young-adults-youth company "Archway Theatre")) " production.")
                           (when author (str "Written by " author "."))
                           (when director (str "Directed by " director "."))]
               blurb (str/join " " (filter some? blurb-bits))
               has-photos? (seq photos)]
           (production-page year month location name page-name prev next blurb about-text photos)
           [:div
            (if has-photos?
              [:a.normal {:href (str page-name ".html")} name]
              [:i [:a.normal {:href (str page-name ".html")} name]])]))])))

(core/page-2 "past/index" "The Archway Theatre - Past Productions"
  ;[:script {:src "./js/carousel.data.js"}]
  ;[:script {:src "./js/carousel.js"}]

  [:div.content

   [:div.content__item
    [:h1 "Past Productions"]]

   (into [:div.content__item]
         (index-all-years))]

  ;[:div#carousel1.carousel]
  ;[:script {:type "text/javascript"} "startCarousel('carousel1',recentImages);"]

  )