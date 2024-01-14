(ns io.github.archwaytheatre.site.past.index
  (:require [cheshire.core :as json]
            [clojure.string :as str]
            [io.github.archwaytheatre.data.core :as data]
            [io.github.archwaytheatre.data.plays :as plays]
            [io.github.archwaytheatre.site.photos :as photos]
            [io.github.archwaytheatre.site.core :as core])
  (:import [java.time LocalDate Month Year YearMonth]
           [java.time.format DateTimeParseException]))


(defn production-page [year month location name page-name prev-page-name next-page-name about photos]
  (core/page (str "past/" page-name) name
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
                   [:a.simple {:href (str prev-page-name ".html")} "Previous"]
                   [:span.disabled "Previous"])
                 "|"
                 [:a.simple {:href "index.html"} "All productions"]
                 "|"
                 (if next-page-name
                   [:a.simple {:href (str next-page-name ".html")} "Next"]
                   [:span.disabled "Next"])]]
      [:section.container
       [:div.content
        links
        [:div.year year]
        (when month
          [:div.month (str "☙ " (str/capitalize (Month/of month)) " ❧")])
        [:div.production name]
        [:div.center [:p [:pre.about about]]]
        [:div.center [:p (when location (str "Performed in the " location "."))]]
        [:div#photoCarousel]
        ;[:script {:type "text/javascript"} (str "startCarousel('carousel1',imagesForPlay('" name "'));")]
        ;[:script {:type "text/javascript"} (str "init();")]
        [:br]
        links]])))

(defn daisy-chain [f c]
  (for [[a b c] (partition-all 3 1 (cons nil c))
        :when b]
    (f a b c)))

(defn parse-partial-date [date-str]
  (case (count date-str)
    4 (Year/parse date-str)
    7 (YearMonth/parse date-str)
    10 (LocalDate/parse date-str)))

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
                                     {:keys [name start] :as production} (->> (plays/get-productions-for year)
                                                                              (sort-by :start)
                                                                              (reverse))
                                     :let [month (month-from start)]
                                     :when (or (< (int year) (.getValue (Year/now)))
                                               (and month
                                                    (-> (YearMonth/of (int year) (int month))
                                                        (.isBefore (YearMonth/now)))))]
                                 (assoc production
                                   :year year
                                   :photos (plays/get-photos-for production)
                                   :page-name (core/linkify name year)))
                               (daisy-chain #(assoc %2 :prev (:page-name %3)
                                                       :next (:page-name %1)))
                               (group-by :year))]
    (for [[year productions] (sort-by key > year->productions)]
      [:div.year-index
       [:div.year-background year]
       (for [{:keys [name prev page-name next location month young-adults-youth
                     company director author photos]} (sort-by :month productions)]
         (let [blurb-bits [(str (indefinite-articlize (or young-adults-youth company "Archway Theatre")) " production.")
                           (when author (str "Written by " author "."))
                           (when director (str "Directed by " director "."))]
               blurb (str/join " " (filter some? blurb-bits))]
           (production-page year month location name page-name prev next blurb photos)
           [:div [:a.simple {:href (str page-name ".html")} name]]))])))

(core/page "past/index" "The Archway Theatre - Past Productions"
  ;[:script {:src "./js/carousel.data.js"}]
  ;[:script {:src "./js/carousel.js"}]

  [:section.container
   [:div.content

    [:h1.center "Past Productions"]
    [:br]
    [:br]
    [:br]
    [:br]

    (index-all-years)


    ;[:div#carousel1.carousel]
    ;[:script {:type "text/javascript"} "startCarousel('carousel1',recentImages);"]

    ]])