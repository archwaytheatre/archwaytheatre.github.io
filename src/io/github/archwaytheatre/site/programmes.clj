(ns io.github.archwaytheatre.site.programmes
  (:require ;[io.github.archwaytheatre.data.plays :as plays]
            [io.github.archwaytheatre.site.core :as core]))


;(defonce plays (plays/get-productions-for ...))
; todo: plays/get-current-and-past-productions? - then filter current? or maybe separate fns for current and past!
; todo: lose the words 'digital programme' from every link?
; todo: extract PDF cover image from PDFs...?

(defn programme-link [plays production-name]
  ; todo: should derive path and text from production
  [:a.normal {:href "https://d35974u23x7pxe.cloudfront.net/2025/nellgwynn/NellGwynnProgramme.pdf"}
   [:div
    [:div.text-align-center "Nell Gwynn Digital Programme"]
    [:br]
    [:img.programme-link__poster {:src "https://d35974u23x7pxe.cloudfront.net/2025/nellgwynn/poster-scaled.png"}]]])


(core/page-2 "programmes" "Digital Programmes"
  [:div.content

   [:div.content__item
    [:h1.text-align-center "Digital Programmes"]
    [:p "Click / Tap to download the digital PDF."]
    [:p "Note that programmes are free to download, but that there may be a charge when purchasing physical copies."]]

   [:div.content__item
     ; Just the one latest / current production goes here:

    [:div.current-programme
     (programme-link "" "")]

    ]

   [:div.content__item
    [:h5 "Previous Productions"]
    [:ul

     ;; All past productions go here...
     ; todo: should have separate helper function to derive path from production name and create previous links
     [:li [:a.normal {:href "https://d35974u23x7pxe.cloudfront.net/2025/heartburn/HeartburnProgramme.pdf"}
     "Heartburn Digital Programme"]]

     [:li [:a.normal {:href "https://d35974u23x7pxe.cloudfront.net/2025/theit/The+IT+final.pdf"}
     "The IT Digital Programme"]]

     ]]])
