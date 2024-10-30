(ns io.github.archwaytheatre.site.join
  (:require
    [io.github.archwaytheatre.site.core :as core]))

(defn member-table [rows]
  (loop [[row & more-rows] rows
         result [:div]
         last-label nil]
    (if-let [[cost id label benefits note] row]
      (let [el [:div.membership__row
                [:div.membership__price {:class id}
                 [:div.membership__label label]
                 [:div cost]]
                [:div.membership__benefits
                 (when note [:div note])
                 (when last-label [:b (str "All " last-label " benefits, plus:")])
                 (into [:ul] (map #(vector :li %) benefits))
                 ]]]
        (recur more-rows (conj result el) label))
      result)))

(core/page-2 "join" "The Archway Theatre"
  [:div.content

   [:div.content__item
    [:h1.text-align-center "Join The Archway Theatre Company"]]

   [:div.content__item
    [:a.call-to-action {:href "https://membermojo.co.uk/archwaytheatre/signin"} "Renew membership / login"]]

   ;TODO: tablets should pop on top of descriptions on small screens?
   [:div.content__item
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

   [:div.content__item
    [:a.call-to-action {:href   "https://membermojo.co.uk/archwaytheatre/joinus"
                        :target "_blank"} "I want to join!"]]

   [:div.content__item
    [:h2 ;.center
     "Youth Memberships"]]

   [:div.content__item
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

   [:div.content__item
    [:a.call-to-action {:href   "https://membermojo.co.uk/archwaytheatre/joinus"
                        :target "_blank"} "I want to join!"]]])
