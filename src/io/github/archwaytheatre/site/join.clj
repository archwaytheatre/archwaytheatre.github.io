(ns io.github.archwaytheatre.site.join
  (:require
    [io.github.archwaytheatre.site.core :as core]))


(defn member-table [rows]
  (reduce
    (fn [table [cost id label benefits conditions]]
      (with-meta
        (conj table
              [:div.row {:role "row"}
               [:div {:class (core/classes "cell center joinlevel" id)
                      :role "gridcell"}
                [:div.larger label]
                [:div cost]]
               [:div.cell {:role "gridcell"}
                (when conditions
                  [:div conditions])
                (when-let [last-label (:last-label (meta table))]
                  [:b (str "All " last-label " benefits, plus:")])
                (into [:ul] (map #(vector :li %) benefits))]])
        {:last-label label}))
    [:div.table {:role "grid"}]
    rows))

(core/page "join" "The Archway Theatre"

  ;[:div.content]
  [:div.content
   [:h1.center "Become a Member of The Archway Theatre"]
   [:br]
   [:div
    (member-table
      [["£15" "bronze" "Bronze"
        ["Annual brochure of productions"
         "Discounted tickets (@ £11.50 each)"
         "10 Newsletters per year with details of forthcoming productions and other events"
         "Become part of the Archway volunteer network to support us in the Box Office, Bar, Front of House or serving Coffee."]]
       ["£35" "silver" "Silver"
        ["Archways newsletter with audition details and play reviews"
         "Attendance at Club Night for post-production discussion"
         "Can perform in, work backstage, and be involved in production"]]
       ["£70" "gold" "Gold"
        ["Complimentary program for each production"
         "Potential Discounts for Studio Productions or access to bespoke events, for example 'reheased readings', or theatre workshops"
         "Free access to Archway digital content, past production recordings etc - more to be added from the archive and new productions"]]])]

   [:br]
   [:br]
   [:div.center.main.vp2 [:a.button.staticButton.larger {:href   "https://membermojo.co.uk/archwaytheatre/joinus"
                                                         :target "_blank"} "I want to join!"]]
   [:br]
   [:div.center [:a.larger {:href "https://membermojo.co.uk/archwaytheatre/signin"} "Member login"]]
   [:br]

   [:br]
   [:h2.center "Youth Memberships"]
   [:div
    (member-table
      [["£10" "junior" "Junior Associate"
        ["Annual brochure of productions"
         "Discounted tickets (@ £11.50 each)"
         "10 Newsletters per year with details of forthcoming productions and other events"
         "Access to Young Adults fortnightly workshops in term time"
         "Can perform in productions"]
        "Aged 16-18"]
       ["£25" "young" "Young Patron"
        ["Can perform in, work backstage, and be involved in production"]
        "Aged 18-22 in full time education"]])]

   [:br]
   [:br]
   [:div.center.main.vp2 [:a.button.staticButton.larger {:href   "https://membermojo.co.uk/archwaytheatre/joinus"
                                                         :target "_blank"} "I want to join!"]]
   [:br]
   [:div.center [:a.larger {:href "https://membermojo.co.uk/archwaytheatre/signin"} "Member login"]]
   [:br]

   ])
