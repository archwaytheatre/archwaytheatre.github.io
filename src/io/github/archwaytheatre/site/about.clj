(ns io.github.archwaytheatre.site.about
  (:require [io.github.archwaytheatre.site.core :as core]))

(core/page "about" "The Archway Theatre"
  [:script {:src "./js/carousel.data.js"}]
  [:script {:src "./js/carousel.js"}]
  [:section.container
   [:div.content

    ; TODO: photos: bar, main house, studio, function room
    ; TODO: photo carousel
    ; TODO: link to membership page
    ; TODO: link to past productions

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

    [:div#carousel1.carousel.labelled]
    [:script {:type "text/javascript"} "startCarousel('carousel1',recentImages,true);"]

    [:p [:a {:href "past/index.html"} "See our past productions"]]
    [:p [:a {:href "join.html"} "Become a member"]]

    ]])