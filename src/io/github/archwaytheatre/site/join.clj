(ns io.github.archwaytheatre.site.join
  (:require
    [io.github.archwaytheatre.site.core :as core]))

(defn member-table [rows]
  (loop [[row & more-rows] rows
         result [:div]
         last-label nil]
    (if-let [[cost id label benefits note] row]
      (let [el [:div.member-row
                [:div.member-price {:class id}
                 [:div.larger label]
                 [:div cost]]
                [:div.member-benefits
                 (when note [:div note])
                 (when last-label [:b (str "All " last-label " benefits, plus:")])
                 (into [:ul] (map #(vector :li %) benefits))
                 ]]]
        (recur more-rows (conj result el) label))
      result)))

(core/page "join" "The Archway Theatre"
  [:section.container
   [:div.content
    [:h1.center "Become a Member of The Archway Theatre"]
    [:br]
    [:div
     (member-table
       [["£15" "bronze" "Bronze"
         ["Annual brochure of productions"
          "Discounted tickets (@ £11.50 each)"
          "10 Newsletters per year with details of forthcoming productions and other events"
          "Become part of the Archway volunteer network to support us in the Box Office, Bar, Front of House or serving Coffee."]
         [:p "Best membership for: " [:b "Volunteering & discounted tickets"]]]
        ["£35" "silver" "Silver"
         ["Archways newsletter with audition details and play reviews"
          "Attendance at Club Night for post-production discussion"
          "Can perform in, work backstage, and be involved in production"]
         [:p "Best membership for: " [:b "Performing & working backstage"]]]
        ["£70" "gold" "Gold"
         ["Complimentary program for each production"
          "Potential Discounts for Studio Productions or access to bespoke events, for example 'reheased readings', or theatre workshops"
          "Free access to Archway digital content, past production recordings etc - more to be added from the archive and new productions"]
         [:p "Best membership for: " [:b "Supporting our theatre!"]]]])]

    [:br]
    [:br]
    [:div.center.main.vp2
     [:a.fancy {:href   "https://membermojo.co.uk/archwaytheatre/joinus"
                :target "_blank"} "I want to join!"]
     [:a.fancy {:href "https://membermojo.co.uk/archwaytheatre/signin"} "Existing member login"]]
    [:br]
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
         [:p [:b "Aged 16-18"]]]
        ["£25" "young" "Young Patron"
         ["Can perform in, work backstage, and be involved in production"]
         [:p [:b "Aged 18-22 in full time education"]]]])]

    [:br]
    [:br]
    [:div.center.main.vp2
     [:a.fancy {:href   "https://membermojo.co.uk/archwaytheatre/joinus"
                :target "_blank"} "I want to join!"]
     [:a.fancy {:href "https://membermojo.co.uk/archwaytheatre/signin"} "Existing member login"]]

    [:br]
    [:br]

    ]])
