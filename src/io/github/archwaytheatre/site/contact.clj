(ns io.github.archwaytheatre.site.contact
  (:require
    [clojure.string :as string]
    [io.github.archwaytheatre.site.core :as core]))

(defn table [rows]
  (into [:div.table {:role "grid"}]
        (for [row rows]
          (let [[title description] row]
            [:div.row {:role "row"}
             [:div.cell {:role  "gridcell"
                         :style "font-size:larger;"}
              (string/replace title " " "&nbsp;")]
             [:div.cell.noglow {:role "gridcell"} description]]))))

(defn alternating-cells [rows]
  (into [:div.table.fullwidth {:role "grid"}]
        (for [row rows]
          (let [row row]
            [:div.row {:role "row"}
             [:div.cell {:role  "gridcell"
                         :style "font-size:larger;"}
              [:br] row [:br]]]))))

(core/page "contact" "The Archway Theatre"
  [:section.container
   [:div.content.larger
    [:div.center core/social-media-logos]
    [:br]
    [:br]
    [:hr]
    [:br]
    [:br]
    [:div "You can get in touch with us by post, email or phone."]
    [:br]
    (table
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
    [:br]
    [:hr]
    [:br]
    [:br]
    [:div "You can also get in touch with specific departments via these email addresses:"]
    [:br]
    (alternating-cells
      [[:div.center [:div "General Enquiries"] [:a.simple.delayedEmail "General Enquiries"]]
       [:div.center [:div "Box Office"] [:a.simple.delayedEmail "Box Office"]]
       [:div.center [:div "Space Hire"] [:a.simple.delayedEmail "Space Hire"]]
       [:div.center [:div "Costume Hire & Wardrobe"] [:a.simple.delayedEmail "Costume Hire & Wardrobe"]]
       [:div.center [:div "House Management"] [:a.simple.delayedEmail "House Management"]]
       [:div.center [:div "Membership"] [:a.simple.delayedEmail "Membership"]]
       [:div.center [:div "Little Theatre Guild\nRepresentative"] [:a.simple.delayedEmail "Little Theatre Guild Representative"]]
       [:div.center [:div "Repertory Committee"] [:a.simple.delayedEmail "Repertory Committee"]]
       [:div.center [:div "Safeguarding"] [:a.simple.delayedEmail "Safeguarding"]]
       [:div.center [:div "Young Adults Workshops"] [:a.simple.delayedEmail "Young Adults Workshops"]]
       [:div.center [:div "Youth Workshops"] [:a.simple.delayedEmail "Youth Workshops"]]])]

   [:script {:src "./js/delayed.js"}]

   ])
