(ns io.github.archwaytheatre.site.auditions
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

(core/page "auditions" "The Archway Theatre"
  [:section.container
   [:div.content

    [:div.getinvolved.volunteer
     "The Archway Theatre is a volunteer run organisation."
     " We rely on the generosity of our members to keep the theatre running."
     " If you'd like to get involved then " [:a.simple {:href "join.html"} "click here to become a member"] "."]

    (re-sort

      [:div.getinvolved.audition
       [:h1 "Audition: Sheila’s Island by Tim Firth"]
       [:div "Directed by Eddie Redfern"]
       [:div "Audition Date: 6th March 2023 in the Bar 19:45"]
       [:h3 "About the Play:"]
       [:div
        "It’s bonfire Night 2019, Sheila, Denise, Julie and Fay are ‘Team C’ in Pennine Mineral Water Ltd’s annual
        outward-bound team-building weekend. Somehow Sheila has been nominated team leader, where using her cryptic
        crossword solving skills has unwittingly stranded her team on Rampsholme Island on Lake Derwentwater in the
        Lake District. Our intrepid heroines find themselves manufacturing weapons from cable ties and spatulas, and
        create a rescue flag with plastic plates and a toasting fork! Questions are asked, truths are told; dirty washing
        is aired. This hilarious and darkly funny play is an updated ’female version’ of the hugely successful
        ‘Neville’s Island’ where Eddie Directed the Amateur premiere in 1996."]
       [:h3 "Characters:"]
       [:ul
        [:li "SHEILA – Hapless team leader - age 30’s to late 40’s"]
        [:li "DENISE - Sarcastic, non-team player – age 20’s to late 30’s"]
        [:li "JULIE - Always eager to help and follow her leader – age 30’s"]
        [:li "FAY - Born again Christian – Age 20’s to late 40’s"]]
       [:div "If interested, or want to read the script, please let Eddie know.
    Playing dates 6th to 17th June 2023"]
       [:div.calltoaction "Email: " [:a.simple.delayedEmail "General Enquiries"]]]

      [:div.getinvolved.audition
       [:h1 "Audition: Little Shop of Horrors"]
       [:div "Come on down to Skid Row Sunday, 26 March 2023 - 2:00pm-5:00pm, Studio"]
       [:div "Playing Dates – 18-22 & 25-29 July 2023"]
       [:div "Directed by Milly O’Ryan | Assistant Director Robert Gregory"]
       [:div "Book and Lyrics by Howard Ashman. Music by Alan Menken"]
       [:div "“I keep askin’ God what I’m for and he tells me ‘Gee, I’m not sure!’”"]
       [:div "Little Shop of Horrors is a lively and fun musical saturated with humour, sometimes a little dark!
     Blending tragic characters and bloody murder with a heartfelt romance – and songs that will GETCHA!"]
       [:h3 "Characters:"]
       [:ul
        [:li "Seymour Krelborn – Male | Age: 20+ | Strong singer."]
        [:li "Audrey – Female | Age 20+ | Strong singer."]
        [:li "Mr. Mushnik – Male | Age: 45+ | Singer."]
        [:li "The Plant (Audrey II) – Physical role / great rhythm!"]
        [:li "Voice Of The Plant – Male | Strong singer."]
        [:li "Orin Scrivello – Male | Age: 25+ | Singer."]
        [:li "Crystal, Ronnette and Chiffon – Female | Age:18+ | Strong singers | Rhythm!"]
        [:li "Ensemble – Skid Row neighbourhood. Smaller parts (can double up) include Winos, Customers, Mr Bernstein,
      Mrs Luce, Skip Snip, Patrick Martin plus x2 street urchins (early teens M/F) | Singers."]]
       [:div "Bring your New York tones along with your bouncy soles! Heads up, we can’t wait to check these numbers out..."]
       [:br]
       [:div.center "“Little Shop of Horrors” “Skid Row (Downtown)”"]
       [:div.center "“Somewhere That’s Green” “Dentist” "]
       [:div.center "“Mushnik and Son” “Feed Me (Git It)”"]
       [:div.center "“Suddenly, Seymour”"]
       [:br]
       [:div
        "Get in touch with any questions you might have or if you would like sight of the sides – please email."
        "Excited about every single aspect of this brilliant musical and look forward to seeing you on the 26th March!"]
       [:div "........................So, Go Get It!"]
       [:div "Milly & Robert"]
       [:div.calltoaction "Email: " [:a.simple.delayedEmail "General Enquiries"]]]

      )]]

  [:script {:src "./js/delayed.js"}]
  )
