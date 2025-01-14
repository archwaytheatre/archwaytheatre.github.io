(ns io.github.archwaytheatre.site.events
  (:require
    [clojure.string :as string]
    [io.github.archwaytheatre.site.core :as core]
    [cheshire.core :as json])
  (:import [java.time LocalDateTime ZoneId]))


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

(defn data-end-for [local-date-time-str]
  (-> (LocalDateTime/parse local-date-time-str)
      (.atZone (ZoneId/of "Europe/London"))
      (.toInstant)
      (.toEpochMilli)
      str))

(core/page-2 "events" "The Archway Theatre"
  [:div.content

   [:div.content__item
    [:div.volunteer
     "The Archway Theatre is a volunteer run organisation."
     " We rely on the generosity of our members to keep the theatre running."
     " If you'd like to get involved then " [:a.normal {:href "join.html"} "click here to become a member"] "."]]

   [:div.content__item
    [:div.other.disappearable {:data-end (data-end-for "2024-03-25T23:59:59")}
     [:h1 "Singing for the Soul"]
     [:p "Fortnightly singing, on Monday evenings 8pm - 9:30pm in the Function Room."]

     [:p [:span "No dates currently announced -" [:a.normal {:href "https://www.facebook.com/groups/621824429716899"} "check the Facebook Group"]]]

     #_[:p
        [:span "Currently announced dates:"]
        [:ul
         [:li "4th February"]
         [:li "19th February"]
         [:li "~"]
         [:li "11th March"]
         [:li "25th March"]]]
     #_[:p "Please note that there is a small gap between Feb and March sessions which do not fall fortnightly from one another. "]

     [:p "£4 per session (£5 for non members)"]

     ; TODO: "Add to Calendar"
     [:div.other__call-to-action
      [:div "Just turn up or"]
      (core/logo-fb-2 "https://www.facebook.com/groups/621824429716899" "Join the Facebook Group")]]]

   [:div.content__item
    [:div.other
     [:h1 "Archway Karaoke"]

     [:p "Karaoke is back!"]
     [:p "Do your warm-ups, choose your songs and prepare yourself for the Archway’s Karaoke with DJ Thom."]
     [:p "Children under 16 must be accompanied by an adult."]
     [:p "Tickets must be bought in advance and will not be available on the door."]

     [:div.other__call-to-action [:a.normal {:href   "https://www.ticketsource.co.uk/archwaytheatre/archway-karaoke/e-zvvvka"
                                             :target "_blank"} "Buy tickets"]]]]

   [:div.content__item
    [:div.other
     [:h1 "Dingbats Improv Classes"]
     [:p "Learn how to improvise in a friendly and supportive environment."]
     [:p "Our classes are where we have the most fun. You don’t have to be an actor or a comedian to attend – just a genuine human being who can listen and laugh."]
     [:p "Regardless of the level, all the classes consist of silly ice-breaker warm ups, exercises to learn new skills and lots of time to play scenes and practise what we’ve learnt."]
     [:p "Improv is the perfect skill to develop if you are an actor looking to enhance your skill set, a writer looking for inspiration or anyone looking to unwind after work and gain a better understanding of creative collaboration."]
     [:p "We run improv courses and drop-in workshops which each focus on different types of improvisation and are suitable for different levels of experience. Some take place online, but most are either at The Hawth Theatre in Crawley or here at The Archway Theatre in Horley."]
     [:p "Dingbats is also available for hire to run a workshop for your team, whether that be a team within a business or an improv group looking for an experienced coach."]
     [:div.other__call-to-action [:a.normal {:href   "https://www.dingbatsimprov.com/improv-classes"
                                             :target "_blank"} "Browse classes"]]]]



   [:script {:src "./js/datetime.js"}]
   [:script {:src "./js/delayed.js"}]])
