(ns io.github.archwaytheatre.site.everything-else
  (:require [io.github.archwaytheatre.site.core :as core]))


(core/page "everythingelse" "The Archway Theatre"
  [:div
   [:br]
   [:div [:a {:href "contact.html"} "Contact"]]
   [:div "How to find us"]
   [:div "Membership"]
   [:div "History"]
   [:div "Past Productions"]
   [:div "Youth Workshops"]
   [:div "Hire a space"]
   [:div "Management / Backstage ?"]
   [:div "Legal?"]
   [:div "LTG?"]
   [:div "Put social media links somewhere?"]])
