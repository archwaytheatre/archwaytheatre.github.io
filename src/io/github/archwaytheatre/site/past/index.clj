(ns io.github.archwaytheatre.site.past.index
  (:require [cheshire.core :as json]
            [clojure.string :as str]
            [io.github.archwaytheatre.site.core :as core])
  (:import [java.time LocalDate Month Year YearMonth]))


(defn production-page [year month location name page-name prev-page-name next-page-name about]
  (core/page (str "past/" page-name) name
    [:script {:src "../js/carousel.data.js"}]
    [:script {:src "../js/carousel.js"}]
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
        [:div.center [:pre.about about]]
        [:div#carousel1.carousel]
        [:script {:type "text/javascript"} (str "startCarousel('carousel1',imagesForPlay('" name "'));")]
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

(defn index-all-years []
  (let [year->productions (->> (for [[year productions] (json/parse-string (slurp "data/plays.json") keyword)
                                     {:keys [name month] :as production} (reverse (sort-by :start productions))
                                     :when
                                     (or (< (int year) (.getValue (Year/now)))
                                         (and month
                                              (-> (YearMonth/of (int year) (int month))
                                                  (.isBefore (YearMonth/now)))))]
                                 (assoc production :year year :page-name (core/linkify name year)))
                               (daisy-chain #(assoc %2 :prev (:page-name %3)
                                                       :next (:page-name %1)))
                               (group-by :year))]
    (for [[year productions] (sort-by key > year->productions)]
      [:div.year-index
       [:div.year-background year]
       ;[:div.productions]
       (for [{:keys [name prev page-name next location trailer about month]} (sort-by :month productions)]
         (do
           (production-page year month location name page-name prev next about)
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