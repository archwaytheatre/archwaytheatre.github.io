(ns io.github.archwaytheatre.site.contact
  (:require
    [clojure.string :as string]
    [io.github.archwaytheatre.site.core :as core]))

(defn table [rows]
  (into [:div.table {:role "grid"}]
        (for [row rows]
          (let [[title description] row]
            [:div.table__row {:role "row"}
             [:div.table__cell {:role  "gridcell"}
              [:h6 (string/replace title " " "&nbsp;")]]
             [:div.table__cell {:role "gridcell"} description]]))))

(defn alternating-cells [rows]
  (into [:div.table {:role "grid"}]
        (for [row rows]
          (let [row row]
            [:div.table__row {:role "row"}
             [:div.table__cell {:role "gridcell"}
              [:br]
              row
              [:br]]]))))

(core/page-2 "contact" "The Archway Theatre"
  [:div.content

   ;[:div.content__item]
   (into [:div.text-align-center] core/social-media-logos-2)

   [:div.content__item [:hr]]

   [:div.content__item
    [:div.text-align-center "You can get in touch with us by post, email or phone."]
    [:br]
      #_(table
      [["Box Office Online" [:a.simple {:href "https://www.ticketsource.co.uk/archwaytheatre/"} "ticketsource.co.uk"]]
       ["Box Office Phone" [:div
                            [:div [:a.simple {:href "tel:0333-666-3366"} "0333 666 3366"] "(handled by TicketSource)"]
                            [:div "Open: Mon-Fri 9:00-19:00, Sat 9:00-17:00"]
                            [:div "£1.75 phone booking fee. Call charges may also apply."]]]
       ["Box Office Email" [:a.simple.delayedEmail "Box Office"]]

       ["General enquiries" [:a.simple {:href "tel:01293-784-398"} "(01293) 784 398"]]
       ["Address" [:pre (string/join "\n"
                          ["Archway Theatre Company Limited"
                           "The Drive"
                           "Horley"
                           "Surrey"
                           "RH6 7NQ"])]]])

    (alternating-cells
      [[:div.text-align-center
        [:h5 "Box Office Online"]
        [:a.normal {:href "https://www.ticketsource.co.uk/archwaytheatre/"} "ticketsource.co.uk"]]
       [:div.text-align-center
        [:h5 "Box Office Phone"]
        [:div
         [:div [:a.normal {:href "tel:0333-666-3366"} "0333 666 3366"] "(handled by TicketSource)"]
         [:div "Open: Mon-Fri 9:00-19:00, Sat 9:00-17:00"]
         [:div "£1.75 phone booking fee. Call charges may also apply."]]]
       [:div.text-align-center
        [:h5 "Box Office Email"]
        [:a.normal.delayedEmail "Box Office"]]
       [:div.text-align-center
        [:h5 "General enquiries"]
        [:a.normal {:href "tel:01293-784-398"} "(01293) 784 398"]]
       [:div.text-align-center
        [:h5 "Address"]
        [:pre (string/join "\n"
                ["Archway Theatre Company Limited"
                 "The Drive"
                 "Horley"
                 "Surrey"
                 "RH6 7NQ"])]]])]

   [:div.content__item [:hr]]

   [:div.content__item
    [:div.text-align-center "You can also get in touch with specific departments via these email addresses:"]
    [:br]
    (alternating-cells
      [[:div.text-align-center [:h5 "General Enquiries"] [:a.normal.delayedEmail "General Enquiries"]]
       [:div.text-align-center [:h5 "Box Office"] [:a.normal.delayedEmail "Box Office"]]
       [:div.text-align-center [:h5 "Space Hire"] [:a.normal.delayedEmail "Space Hire"]]
       [:div.text-align-center [:h5 "Costume Hire & Wardrobe"] [:a.normal.delayedEmail "Costume Hire & Wardrobe"]]
       [:div.text-align-center [:h5 "House Management"] [:a.normal.delayedEmail "House Management"]]
       [:div.text-align-center [:h5 "Membership"] [:a.normal.delayedEmail "Membership"]]
       [:div.text-align-center [:h5 "Little Theatre Guild\nRepresentative"] [:a.normal.delayedEmail "Little Theatre Guild Representative"]]
       [:div.text-align-center [:h5 "Repertory Committee"] [:a.normal.delayedEmail "Repertory Committee"]]
       [:div.text-align-center [:h5 "Safeguarding"] [:a.normal.delayedEmail "Safeguarding"]]
       [:div.text-align-center [:h5 "Young Adults Workshops"] [:a.normal.delayedEmail "Young Adults Workshops"]]
       [:div.text-align-center [:h5 "Youth Workshops"] [:a.normal.delayedEmail "Youth Workshops"]]])]

   [:div.content__item [:hr]]
   [:div.signupform core/email-signup-form]

   [:script {:src "./js/delayed.js"}]

   ])
