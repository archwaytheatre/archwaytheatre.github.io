(ns io.github.archwaytheatre.site.past.productions
  (:require [io.github.archwaytheatre.site.core :as core]))


(core/page "past-productions" "The Archway Theatre"
  [:script {:src "./js/carousel.data.js"}]
  [:script {:src "./js/carousel.js"}]
  [:br]
   [:div.center [:a {:href ""} "All years"] "|" [:a {:href ""} "All 2023 productions"]]
  [:div.content

   ; TODO: photos: bar, main house, studio, function room
   ; TODO: photo carousel
   ; TODO: link to membership page
   ; TODO: link to past productions


   [:div.year "2023"]
   ;[:div "&lt; 2022"]
   [:div.production "Colder Than Here"]
   [:div#carousel1.carousel]
   [:script {:type "text/javascript"} "startCarousel('carousel1',imagesForPlay('Colder Than Here'));"]

   [:div
    [:div
     "The Archway Theatre is a thriving “little theatre” located under the railway arches in Horley, near Gatwick."
     [:ul
      [:li "95 seats accommodation"]
      [:li "10 full productions every year"]
      [:li "10 performances of each production over a two-week run"]
      [:li "Youth Workshops and Young Adults Workshops & productions"]
      [:li "Studio Theatre and Function Room"]]]
    [:div
     "We pride ourselves on a professional approach, and we are ambitious with our productions."
     " We try all aspects of theatre, from musicals through drama to pantomime."
     " We are open to the public, but also as a club our members can buy tickets at a reduced rate, "
     "take advantage of the youth workshop facilities, and visit the members’ bar."
     " Those wishing to go on stage or backstage are members of the company, who put on all the shows."]]

   [:br]

   ;[:div#carousel1.carousel]
   ;[:script {:type "text/javascript"} "startCarousel('carousel1',recentImages);"]


   ])