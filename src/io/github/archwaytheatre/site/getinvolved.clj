(ns io.github.archwaytheatre.site.getinvolved
  (:require [io.github.archwaytheatre.site.core :as core]
            [cheshire.core :as json]))


(def location->class {"Main House" "main"
                      "The Studio" "studio"
                      "Digital"    "digital"})

(core/page "getinvolved" "The Archway Theatre"
  [:div.events
   (for [{:keys [location name type when where performances contact about imageurl]}
         (json/parse-string (slurp "data/getinvolved.json") keyword)]
     [:div {:class (core/classes "event" (location->class location "unknown"))}
      [:div.eventimage
       [:img {:src imageurl}]]
      [:div.eventdata
       [:div.eventdatum.fade1 location]
       [:div.eventdatum.fade2.bold name]
       [:div.eventdatum.fade3 [:a {:href (str "javascript:showPopup(\"" about "\");")}] "About"]
       [:div.eventdatum.fade4 when]
       [:div.eventdatum.fade5.bold
        [:a.button.fade1 {:href (if (= location "Digital")
                                  "https://www.ticketsource.co.uk/archwaytheatredigital/"
                                  "https://www.ticketsource.co.uk/archwaytheatre/")}
         "Buy Tickets"]]]])])
