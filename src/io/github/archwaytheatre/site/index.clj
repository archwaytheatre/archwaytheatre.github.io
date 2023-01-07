(ns io.github.archwaytheatre.site.index
  (:require [io.github.archwaytheatre.site.core :as core]
            [cheshire.core :as json]))


(def location->class {"Main House" "main"
                      "The Studio" "studio"
                      "Digital"    "digital"})

(core/page "index" "The Archway Theatre"
  [:div.events.dark
   (for [[idx {:keys [name location dates trailer about imageurl]}]
         (map-indexed vector (json/parse-string (slurp "data/whatson.json") keyword))]
     (let [link-href (if (= location "Digital")
                       "https://www.ticketsource.co.uk/archwaytheatredigital/"
                       "https://www.ticketsource.co.uk/archwaytheatre/")]
       [:div {:class (core/classes "event" (location->class location "unknown"))}
        [:div.eventimage
         [:a {:href link-href}
          [:img {:src imageurl}]]]
        [:div.eventdata
         [:div.eventdatum location]
         [:div.eventdatum.bold name]
         [:div.eventdatum dates]
         [:div.eventdatum [:a {:href (str "javascript:showPopup(\"popup-" idx "\");")} "About"]]
         [:div.eventdatum.bold
          [:a.button {:href link-href}
           "Buy Tickets"]]
         [:div.popup {:id      (str "popup-" idx)
                      :onclick "javascript:hidePopups();"}
          [:pre about]
          [:div.controls
           [:a {:href (str "javascript:hidePopups();")} [:pre "Back"]]
           [:a.button.compact {:href link-href} "Buy Tickets"]]]]]))])
