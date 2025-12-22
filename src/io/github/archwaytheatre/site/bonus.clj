(ns io.github.archwaytheatre.site.bonus
  (:require [io.github.archwaytheatre.site.core :as core]))

(core/page-2 "bonus" "Bonus Content"

  [:script {:src "./js/about-photo-data.js"}]

  [:div.content

   [:div.content__item

    [:div.audition
     "To celebrate the 250th anniversary of Jane Austen's birth, we published our audio drama of Pride & Prejudice."

     [:br]
     [:br]
     [:a.call-to-action {:href "https://www.youtube.com/watch?v=IHV1BELfxsI"} "Pride & Prejudice: Part I (YouTube)"]
     [:br]
     [:br]
     [:br]
     [:a.call-to-action {:href "https://www.youtube.com/watch?v=n17M6oZLMKg"} "Pride & Prejudice: Part II (YouTube)"]
     [:br]
     [:br]

     ]]])
