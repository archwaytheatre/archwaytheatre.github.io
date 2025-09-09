(ns io.github.archwaytheatre.site.programmes
  (:require [io.github.archwaytheatre.site.core :as core]))


(core/page-2 "programmes" "Digital Programmes"
  [:div.content

   [:div.content__item
    [:h1 "Digital Programmes"]
    [:div.center "Click / Tap to download the digital PDF."]]

   [:div.content__item
     ; Just the one latest / current production goes here:
    [:div.current-programme
     [:a.normal {:href "https://d35974u23x7pxe.cloudfront.net/2025/heartburn/HeartburnProgramme.pdf"}
      "Heartburn Digital Programme"]]

    ]

   [:div.content__item
    [:h5 "Previous Productions"]
    [:ul

     ;; All past productions go here:
     #_[:li [:a.normal {:href "https://d35974u23x7pxe.cloudfront.net/2025/heartburn/HeartburnProgramme.pdf"}
     "Heartburn Digital Programme"]]

     [:li [:a.normal {:href "https://d35974u23x7pxe.cloudfront.net/2025/theit/The+IT+final.pdf"}
     "The IT Digital Programme"]]

     ]]])
