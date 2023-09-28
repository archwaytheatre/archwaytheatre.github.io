(ns io.github.archwaytheatre.site.getinvolved
  (:require
    [io.github.archwaytheatre.site.core :as core]))


(core/page "getinvolved" "The Archway Theatre"
  [:section.container
   [:div.content

    ; todo a picture of some people getting involved?

    [:h1 "Get Involved"]
    [:p "Every production we put on requires many people, both on stage and off, to volunteer their time and their skills to ensure that it runs smoothly. Here is a run-down of the different ways you could contribute to the success of our little theatre:"]

    [:br]

    [:h2 "Acting"]
    [:p "Directors hold auditions a few weeks prior to the start of rehearsals for each show. You don’t have to have any prior experience or track record with us – each audition is a meritocracy. Some people like to go for the big, meaty, principal roles, but there are also many opportunities for smaller parts and even silent walk-on parts for those who prefer them."]

    [:h2 "Backstage"]
    [:p "The backstage team is a vital part of any production. The Stage Manager is responsible for the smooth running of the performances, ensuring the props are in the right place, the scenes are changed rapidly, the actors are ready for their on-stage entrances and there is no undue noise backstage. The stage crew work in the dark, unseen but still appreciated by those in the spotlights, making sure that the stage set and the props are in exactly the right place for each scene. Also in the shadows (with a little reading light) is the prompt, seldom needed on show night but a huge help during the rehearsal period."]

    [:h2 "Lighting and Sound"]
    [:p "The techies (often operating both lighting and sound but sometimes two different people) are key members of production staff, adding visual and aural drama to the power of the on-stage performances. They are located in a booth at the back of the auditorium, delivering their cues with pinpoint accuracy. Some of them will have been responsible for designing the lighting and sound for the production in question, while others concentrate on implementing the plans that have been prepared."]

    [:h2 "Set Design, Construction and Painting"]
    [:p "The set designers works with the director to bring their vision of where the action will take place to reality, producing drawings and, sometimes, a model of the desired design."]
    [:p "The baton is then handed to the master carpenter (who may be responsible for producing the scale model instead of the designer) and they ensure that all materials are available for the build, ordering in any that are required. They explain the design to their fellow builders and the process of set construction begins on the Monday before show week, finishing in time for the painters to complete their work on the Saturday before the run starts on the Tuesday. After the run of that particular show ends, the construction team comes in again on the Sunday morning to strike the set and return the materials to the stores. Throughout the process, the master carpenter is responsible for final construction details and any safety concerns, liaising with both the designer and the director to ensure that the set is fit for purpose."]
    [:p "The set painting team ensures that the set lives up to the vision of the designer, using a combination of broadbrush and detailed work. The painting team’s task ranges from giving the set a general covering colour to a range of painting effects (woodgrain, ageing, wallpaper etc) and usually takes a couple of Saturday mornings to complete."]

    [:h2 "Front of House, Box Office, Bar and Coffee"]
    [:p "On each production night our aim is to give our audience the most enjoyable and comfortable experience possible and a team of volunteers is always on hand to ensure that people can buy tickets and programmes, find their seats and enjoy the ambience of our recently re-designed bar before and after the show and in the interval."]

    [:br]

    [:div.center.larger
     [:span [:a.simple {:href "join.html"} "Become a member"]]
     "&nbsp;&nbsp;"
     [:span [:a.simple {:href "contact.html"} "Contact us"]]]]]

  )
