(ns io.github.archwaytheatre.site.about
  (:require [io.github.archwaytheatre.site.core :as core]))

(core/page-2 "about" "The Archway Theatre"

  [:script {:src "./js/about-photo-data.js"}]

  [:div.content

    ; TODO: photos: bar, main house, studio, function room

   [:div.content__item
    "The Archway Theatre is a thriving “little theatre” located under the railway arches in Horley, near Gatwick."
    [:ul
     [:li "95 seats accommodation"]
     [:li "10 full productions every year"]
     [:li "10 performances of each production over a two-week run"]
     [:li "Youth Workshops and Young Adults Workshops & productions"]
     [:li "Studio Theatre and Function Room"]]]

   [:div.content__item
    "We pride ourselves on a professional approach, and we are ambitious with our productions."
    " We try all aspects of theatre, from musicals through drama to pantomime."
    " We are open to the public, but also as a club our members can buy tickets at a reduced rate, "
    "take advantage of the youth workshop facilities, and visit the members’ bar."
    " Those wishing to go on stage or backstage are members of the company, who put on all the shows."]

   [:div.content__item
    [:div#photoCarousel]]

   [:div.content__item
    [:a.normal {:href "past/index.html"} "See our past productions"]
    [:div.vertical-spacer]
    [:a.normal {:href "history/history.html"} "A History of The Archway"]]

   [:div.content__item
    ;[:div.vertical-spacer]
    [:a.call-to-action {:href "join.html"} "Become a member"]]])
