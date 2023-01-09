(ns io.github.archwaytheatre.site.everything-else
  (:require
    [clojure.string :as string]
    [io.github.archwaytheatre.site.core :as core]))


(defn link-table [rows]
  (into [:div.table {:role "grid"}]
        (for [row (sort-by second rows)]
          (let [[href label description] row
                external? (string/starts-with? href "http")]
            [:a.row {:href   href
                     :target (if external? "_blank" "_self")
                     :role   "row"}
             [:div.cell.glow {:role "gridcell"
                              :style "font-size:larger;"}
              (string/replace label " " "&nbsp;")]
             [:div.cell.noglow {:role "gridcell"} description]]))))

(core/page "everythingelse" "The Archway Theatre"
  [:script {:src "./js/search.js"}]
  [:div.container
   [:div
    [:h1 "Pages:"]
    (link-table
      [["index.html" "What's&nbsp;On?" "A list of shows on now or coming soon to the Archway Theatre."]
       ["getinvolved.html" "Get&nbsp;Involved!" "Notices about upcoming opportunities at your local theatre."]
       ["findus.html" "Find&nbsp;Us" "How to find our theatre if you're planning a visit."]
       ["contact.html" "Contact" "How to reach us."]
       ["https://www.ticketsource.co.uk/archwaytheatre/" "Box Office" "Buy tickets for any of our productions."]
       ; TODO: membership could / should be it's own page, rather than going directly to membermojo
       ["https://membermojo.co.uk/archwaytheatre" "Membership"
        (str "Join as a member to get discounted tickets or to get involved."
             " You can also manage your existing membership here.")]
       ["https://littletheatreguild.org/" "Little Theatre Guild"
        "Find out more about the Little Theatre Guild that we're part of."]])]

   [:div
    [:h1 "Search"]
    [:div
     [:input.search
      {:id       "searchterms"
       :type     "text"
       :onchange "updateLink(this, 'searchbutton');"}]]
    [:br]
    [:div.center
     [:a.button.compact.search
      {:id   "searchbutton"
       :href "#"}
      "Search"]]]

   [:h1 "Still to do:"]
   [:ul
    [:li "History"]
    [:li "Past Productions"]
    [:li "Youth Workshops"]
    [:li "Hire a space"]
    [:li "Management / Backstage ?"]
    [:li "Legal?"]
    [:li "Put social media links somewhere?"]]])
