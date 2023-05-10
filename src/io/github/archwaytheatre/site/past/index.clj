(ns io.github.archwaytheatre.site.past.index
  (:require [cheshire.core :as json]
            [io.github.archwaytheatre.site.core :as core])
  (:import [java.time LocalDate Year YearMonth]))


(defn production-page [year name page-name prev-page-name next-page-name about]
  (core/page page-name name
    [:script {:src "./js/carousel.data.js"}]
    [:script {:src "./js/carousel.js"}]
    (let [links [:div.center
                 (if prev-page-name
                   [:a {:href (str prev-page-name ".html")} "Previous"]
                   [:span.disabled "Previous"])
                 "|"
                 [:a {:href "past-index.html"} "All productions"]
                 "|"
                 (if next-page-name
                   [:a {:href (str next-page-name ".html")} "Next"]
                   [:span.disabled "Next"])]]
      [:section.container
       [:div.content
        links
        [:div.year year]
        [:div.production name]
        [:div [:pre.about about]]
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
  (let [year->productions (->> (for [[year productions] (json/parse-string (slurp "data/productions.json") keyword)
                                     {:keys [name] :as production} (reverse (sort-by :start productions))]
                                 (assoc production :year year :page-name (core/linkify name)))
                               (daisy-chain #(assoc %2 :prev (:page-name %3)
                                                       :next (:page-name %1)))
                               (group-by :year))]
    (for [[year productions] year->productions]
      [:div.year-index
       [:div.year-background year]
       ;[:div.productions]
       (for [{:keys [name prev page-name next location trailer about start end]} productions]
         (let [start-date (some-> start parse-partial-date)
               end-date (some-> end parse-partial-date)]
           (production-page year name page-name prev next about)

           [:div [:a {:href (str page-name ".html")} name]

            ]))])))

(core/page "past-index" "The Archway Theatre - Past Productions"
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