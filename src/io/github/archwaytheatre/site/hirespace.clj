(ns io.github.archwaytheatre.site.hirespace
  (:require [io.github.archwaytheatre.site.core :as core]))


(core/page "spaces" "The Archway Theatre"
  [:section.container
   [:div.content
    [:h1 "Hire a Space"]
    [:div "The Archway Theatre Studio Complex is a thriving “Studio Theatre and Function Room” within the Millennium Arches located under the railway arches at the Archway Theatre in Horley, near Gatwick."]

    [:br]
    [:br]

    [:div
     [:h2 "Archway Theatre Studio"]
     [:img {:src "https://i0.wp.com/www.archwaytheatre.com/wp-content/uploads/2014/09/studio1.jpg"}]
     [:ul
      [:li "Flexible staging and seating configurations."]
      [:li "Wide programme of productions and events."]
      [:li "Available to hire."]
      [:li "Classes and Clubs."]
      [:li "The studio is not just for our main theatre’s use but for any touring companies, and even students studying theatre studies."]
      [:li "A real opportunity to try out their talent in front of live audiences!"]
      [:li "Room for portable chairs in various configurations"]
      [:li "Raised stage area"]
      [:li "Portable staging"]
      [:li "Lighting Rig (chargeable extra – subject to operator availability)"]
      [:li "Sound Rig (chargeable extra – subject to operator availability)"]]
     [:div
      "To make an enquiry about hiring the Studio or Function Room, please email: "
      [:a.delayedEmail "Space Hire"]]]

    [:br]
    [:br]

    [:div
     [:h2 "Archway Theatre Function Room"]
     [:img {:src "https://i0.wp.com/www.archwaytheatre.com/wp-content/uploads/2014/09/function_room_1.jpg"}]
     [:img {:src "https://i0.wp.com/www.archwaytheatre.com/wp-content/uploads/2014/09/function_room_2.jpg"}]
     [:div
      "The Function Room is available for hire."
      [:ul
       [:li "Available during day, evening or weekends"]
       [:li "Extremely competitive prices"]
       [:li "The room can be used also for private parties for children and adults, and a small bar can be made available."]]]
     [:div
      "Function Room Facilities:"
      [:ul
       [:li "Kitchen  – A well-appointed kitchen. For an additional hire charge, hirers may have access to an eight burner, dual oven, electric cooker; a domestic size fridge; a freezer; a domestic microwave; a dishwasher; hot water boiler; stainless steel preparation surfaces; and a hand wash basin."]
       [:li "Toilets  – Male, Female and Disabled toilets are all within close proximity to the Function Room and Studio Theatre"]
       [:li "Bar – (available for an additional hire charge)"]
       [:li "Car Parking –  A large public car park is located immediately outside. Limited free private parking is available to hirers"]
       [:li "Centrally Heated"]]]
     [:div
      "To make an enquiry about hiring the Studio or Function Room, please email: "
      [:a.delayedEmail "Space Hire"]]]

    [:br]
    [:br]]]
  [:script {:src "./js/delayed.js"}])
