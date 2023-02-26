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
  [:script {:src "./js/delayed.js"}]
  [:div
   [:div.content
    [:div "You can get in touch with us by post, email or phone."]
    [:br]
    (table
      [["Box Office Online" [:a {:href "https://www.ticketsource.co.uk/archwaytheatre/"} "ticketsource.co.uk"]]
       ["Box Office Phone" [:div
                            [:div [:a {:href "tel:0333-666-3366"} "0333 666 3366"] "(handled by TicketSource)"]
                            [:div "Open: Mon-Fri 9:00-19:00, Sat 9:00-17:00"]
                            [:div "£1.75 phone booking fee. Call charges may also apply."]]]
       ["Box Office Email" [:a.delayedEmail "Box Office"]]

       ["General enquiries" [:a {:href "tel:01293-784-398"} "(01293) 784 398"]]
       ["Address" [:pre (string/join "\n"
                          ["Archway Theatre Company Limited"
                           "The Drive"
                           "Horley"
                           "Surrey"
                           "RH6 7NQ"])]]])
    [:hr]
    [:br]
    [:div "You can also get in touch with specific departments via these email addresses:"]
    [:br]
    (alternating-cells
      [[:div.center [:div "General Enquiries"] [:a.delayedEmail "General Enquiries"]]
       [:div.center [:div "Box Office"] [:a.delayedEmail "Box Office"]]
       [:div.center [:div "Space Hire"] [:a.delayedEmail "Space Hire"]]
       [:div.center [:div "Costume Hire & Wardrobe"] [:a.delayedEmail "Costume Hire & Wardrobe"]]
       [:div.center [:div "House Management"] [:a.delayedEmail "House Management"]]
       [:div.center [:div "Membership"] [:a.delayedEmail "Membership"]]
       [:div.center [:div "Little Theatre Guild\nRepresentative"] [:a.delayedEmail "Little Theatre Guild Representative"]]
       [:div.center [:div "Repertory Committee"] [:a.delayedEmail "Repertory Committee"]]
       [:div.center [:div "Safeguarding"] [:a.delayedEmail "Safeguarding"]]
       [:div.center [:div "Young Adults Workshops"] [:a.delayedEmail "Young Adults Workshops"]]
       [:div.center [:div "Youth Workshops"] [:a.delayedEmail "Youth Workshops"]]])]

   [:script {:type "text/javascript"} "addEmails()"]

   ])
