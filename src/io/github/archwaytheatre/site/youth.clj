(ns io.github.archwaytheatre.site.youth
  (:require [io.github.archwaytheatre.site.core :as core]))


(core/page "youth" "The Archway Theatre"
  [:section.container
   [:div.content.larger

    [:div.getinvolved.audition
     [:h1 "Young Adults Group"]
     [:br]

     [:div "For ages 16-21. Everyone welcome!"]
     [:div "£1 per session, pay on arrival."]
     [:br]
     [:div "Come and join our Young Adult's Theatre Group and explore all things drama and theatre in a creative, safe and fun space!"]
     [:br]
     [:div "To attend you must fill out "
      [:a.simple {:href "https://forms.microsoft.com/pages/responsepage.aspx?id=E_9VFcpfrkughlqDJKbNqVtaoTLA5bJBhZl78R9NZw9UNDg4WjhMU1VLVjVRRkpEVUxGNk40Wkk2OC4u"} "the online sign up form."]]
     [:br]
     [:div.calltoaction "For more info, contact " [:a.simple.delayedEmail "Young Adults Workshops"]]]

     [:br]
     [:br]

    [:div.getinvolved.audition
     [:h1 "Youth Workshops"]
     [:br]
     [:div
      "Archway run Workshops for 9 to 16 year old fortnightly on Saturdays.
      All aspects of theatre and drama are explored in an inclusive, fun environment.
      A Workshop production is staged in June."]
     [:br]
     [:div.calltoaction "For more info, contact " [:a.simple.delayedEmail "Youth Workshops"]]]



    [:script {:src "./js/delayed.js"}]]

   ])