(ns io.github.archwaytheatre.site.youth
  (:require [io.github.archwaytheatre.site.core :as core]))


(core/page-2 "youth" "The Archway Theatre"
  [:div.content

   [:div.content__item
    [:div.volunteer
     "The Archway Theatre is a volunteer run organisation."
     " We rely on the generosity of our members to keep the theatre running."
     " If you'd like to get involved then " [:a.normal {:href "join.html"} "click here to become a member"] "."]]

   [:div.content__item
    [:div.audition
     [:h1 "Young Adults Group"]
     [:br]

     [:div "For ages 16-21. Everyone welcome!"]
     [:div "£1 per session, pay on arrival."]
     [:br]
     [:div "Come and join our Young Adult's Theatre Group and explore all things drama and theatre in a creative, safe and fun space!"]
     [:br]
     [:div "To attend you must fill out "
      [:a.normal {:href "https://forms.microsoft.com/pages/responsepage.aspx?id=E_9VFcpfrkughlqDJKbNqVtaoTLA5bJBhZl78R9NZw9UNDg4WjhMU1VLVjVRRkpEVUxGNk40Wkk2OC4u"} "the online sign up form."]]
     [:br]
     [:div.other__call-to-action "For more info, contact " [:a.normal.delayedEmail "Young Adults Workshops"]]]]

   [:div.content__item
    [:div.audition
     [:h1 "Youth Workshops"]
     [:br]
     [:div
      "Archway run Workshops for 9 to 16 year old fortnightly on Saturdays.
      All aspects of theatre and drama are explored in an inclusive, fun environment.
      A Workshop production is staged in June."]
     [:br]
     [:div.other__call-to-action "For more info, contact " [:a.normal.delayedEmail "Youth Workshops"]]]]

   [:script {:src "./js/delayed.js"}]])
