(ns io.github.archwaytheatre.site.getinvolved
  (:require
    [clojure.string :as string]
    [io.github.archwaytheatre.site.core :as core]
    [cheshire.core :as json]))


(defn interleave-all [coll1 coll2]
  (take (+ (count coll1) (count coll2))
        (remove nil? (interleave (concat coll1 (repeat nil))
                                 (concat coll2 (repeat nil))))))

(defn re-sort [& things]
  (let [volunteering (shuffle (filter #(-> % first name (string/includes? "volunteer")) things))
        other (shuffle (filter #(-> % first name (string/includes? "other")) things))
        audition (filter #(-> % first name (string/includes? "audition")) things)
        everything-else (interleave-all
                          other
                          audition)]
    (concat audition other volunteering)
    #_(interleave-all volunteering everything-else)))

(core/page "getinvolved" "The Archway Theatre"
  [:script {:src "./js/involvement.js"}]

  [:div.content

   [:div.getinvolved.volunteer
    "The Archway Theatre is a volunteer run organisation."
    " We rely on the generosity of our members to keep the theatre running."
    " If you'd like to get involved then " [:a {:href "join.html"} "click here to become a member"] "."]

   (re-sort

     [:div.getinvolved.volunteer
      "Run the bar at a current or future production."
      [:div.calltoaction "Email: " [:a.delayedEmail "House Management"]]]

     [:div.getinvolved.volunteer
      "Staff the box office at a current or future production."
      [:div.calltoaction "Email: " [:a.delayedEmail "House Management"]]]

     [:div.getinvolved.volunteer
      "Perform Front of House duties for a current or future production."
      [:div.calltoaction "Email: " [:a.delayedEmail "House Management"]]]

     [:div.getinvolved.volunteer
      "Help build the set for a future production."
      [:div.calltoaction "Email: " [:a.delayedEmail "General Enquiries"]]]

     [:div.getinvolved.volunteer
      "Help paint the set for a future production."
      [:div.calltoaction "Email: " [:a.delayedEmail "General Enquiries"]]]

     )]

  [:script {:src "./js/delayed.js"}]
  )
