(ns io.github.archwaytheatre.site.find-us
  (:require [io.github.archwaytheatre.site.core :as core]))


(core/page-2 "findus" "The Archway Theatre"
  [:div.content

   ; todo: a serious revamp
   ; a section on each entrance, complete with a photo
   ; perhaps a large SVG map of the layout of the outside of the theatre
   ; + what you'll find when you get inside

   [:div.content__item
    [:b "Our main entrance:"]
    (core/image "main-entrance.jpg"
                ;"600px"
                "Ian Capper" core/creative-commons-by-sa-2.0)

    [:br]
    [:div "Unless otherwise advertised, evening performances start at 19:45, matinees start at 14:00."]]

   [:div.content__item
    [:div [:b "Address:"]]
    [:div "Archway Theatre Company Limited"]
    [:div "The Drive"]
    [:div "Horley"]
    [:div "Surrey"]
    [:div "RH6 7NQ"]

    #_#_#_[:iframe {:width        "425"
                    :height       "350"
                    :frameborder  "0"
                    :scrolling    "no"
                    :marginheight "0"
                    :marginwidth  "0"
                    :src          "https://www.openstreetmap.org/export/embed.html?bbox=-0.16377300024032593%2C51.16787079632578%2C-0.16043901443481448%2C51.170075654621726&amp;layer=mapnik"
                    :style        "border: 1px solid black"}]
            [:br]
            [:small
             [:a {:href "https://www.openstreetmap.org/#map=19/51.16897/-0.16211"}
              "View Larger Map"]]]

   [:div.content__item
    [:div.mapouter.content-max-width ; https://google-map-generator.com/
     [:div.gmap_canvas.content-max-width
      [:iframe#gmap_canvas.content-max-width {:width        "600"
                                              :height       "500"
                                              :src          "https://maps.google.com/maps?q=archway%20theatre,%20horley&t=&z=17&ie=UTF8&iwloc=&output=embed"
                                              :frameborder  "0"
                                              :scrolling    "no"
                                              :marginheight "0"
                                              :marginwidth  "0"}]
      ;[:a {:href "https://putlocker-is.org"}]
      [:br]
      [:style ".mapouter{position:relative;text-align:right;height:500px;width:600px;}"]
      ;[:a {:href "https://www.embedgooglemap.net"} "google maps on websites"]
      [:style ".gmap_canvas {overflow:hidden;background:none!important;height:500px;width:600px;}"]]]]

   [:div.content__item
    [:iframe.content-max-width {:src "https://www.google.com/maps/d/embed?mid=1GJinThkolOI8V1FCqJDYCqTyofx22iQ&ehbc=2E312F&noprof=1"
                                :width "640"
                                :height "480"}]]

   [:div.content__item
    [:div [:b "Directions:"]]
    [:ul
     [:li.spaced "From the M23 exit at junction 9 (Gatwick), follow signs for Gatwick/Redhill (A23). Head down the A23 to the Longbridge roundabout and turn right (Redhill A23). Take the second turn on the right (Massetts Road) and then the fifth on the right (Russels Crescent) Just before the traffic lights, turn right (The Drive). The Archway Theatre is on your left (station car park) and you can park in the NCP car park. (You will also see brown council signposts to the Archway Theatre)"]
     [:li.spaced "From the B2036 turn off at mini roundabout and follow the signs for Horley Station. Go past the Station and take the fisrt left at the traffic light (Russels Crescent) and then immediately left (The Drive). The Archway Theatre is on your left and you can park in the NCP car park. (You will also see brown council signposts to the Archway Theatre.)"]]
    ]])
