(ns io.github.archwaytheatre.site.history.history
  (:require [io.github.archwaytheatre.site.core :as core]))

(core/page "history/history" "The Archway Theatre"
  [:section.container
   [:div.content
    [:article
     [:div
      [:h1 "Archway History"]
      [:p "The Archway Theatre’s origins began in 1939 in much the same way as many other amateur theatre companies did – as a small group of enthusiasts using local halls as a venue."]
      [:p "During the war years and after it grew, and in 1952 established itself “underneath the arches”. These had been created in 1909 when a road bridge was built, crossing the railway line at Horley Station."
       #_[:a
          {:href
           "https://i0.wp.com/www.archwaytheatre.com/archwaytheatre/wp-content/uploads/2014/08/50th_cake.jpg"}
          [:img
           {:class            "alignright size-full wp-image-95",
            :src
            "https://i0.wp.com/www.archwaytheatre.com/archwaytheatre/wp-content/uploads/2014/08/50th_cake.jpg?resize=300%2C225",
            :alt              "50th_cake",
            :width            "300",
            :height           "225",
            :data-recalc-dims "1"}]]]
      [:p "At first the stage and auditorium were fully contained within the space determined by the width of the road, in the taller of the arches. (“Space” may be a somewhat misleading term, as actors had to crouch in the low jack arches preparatory to a dramatic entrance. No members were really accepted as true actors until they had knocked themselves semi-conscious on exiting the stage!)"]
      [:p "We pride ourselves on a professional approach, and we are rather ambitious with some of our productions. We try all aspects of theatre, from musicals through drama to pantomime. We are a club, so members can buy tickets, take advantage of the youth workshop facilities, and visit the members’ bar. Those wishing to go on stage or backstage are members of the company, who put on all the shows."]
      [:p "There was only room for 65 seats, and set building was a work of art in miniature. However, care was taken to provide excellent bar and foyer facilities for the audience, the seats came from a top London Theatre with a good rake to provide visibility, and the company strove to develop and maintain a varied, extensive programme of quality productions."]
      [:p "This paid off – a regular membership filled the seats and clamoured for more. In time our success also became our problem – people were disappointed when demand regularly exceeded supply. So we bit the bullet, raised funds, took out a bank loan, leased more land behind the arches, built a new stage area, green room and scenery dock, and refurbished the auditorium to provide 95 seats in 1989."]
      [:p "Then we prayed! All was well, we have continued our output of 10 varied plays a year (each for 10 performances – sometimes more) and maintained an average attendance of over 95%"]]
     [:p [:a {:href "central-players.html"} "Life Before The Archway"]]
     [:p [:a {:href "how-it-all-began/act-1.html"} "Read more of our history with some lovely stories written by Bette Bunkell"]]
     ]]])
