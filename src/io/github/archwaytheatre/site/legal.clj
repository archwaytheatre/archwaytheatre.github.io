(ns io.github.archwaytheatre.site.legal
  (:require
    [io.github.archwaytheatre.site.core :as core])
  (:import [java.time Year]))

(core/page "legal" "Legal & Site Info"
  [:section.container
   [:div.content
    [:h1 "Legal &amp; Site Info"]
    [:div
     [:ul
      [:li.spaced [:b "Copyright"] [:br]
       "All rights reserved."
       " These materials (including without limitation all articles, text, images, logos etc.) are"
       " Copyright " (str "&copy; 1987-" (str (Year/now)) " The Archway Theatre Company.")
       " The Archway Theatre Company endeavours to ensure that the information on this website is correct but does not"
       " accept any liability for error or omission."]
      [:li.spaced [:b "Disclaimer"] [:br]
       "Every effort has been made to ensure the accuracy of information presented."
       " However, no warranty or guarantee, expressed or implied, concerning the content or accuracy of the information"
       " stored and maintained within this information service is made. "
       "The information is presented ‘as is’. Price and availability of information is subject to change without notice."]
      [:li.spaced "The Archway Theatre Company will not be liable (to the fullest extent permitted at law) for any loss,"
       " damage or inconvenience arising as a consequence of any use of or the inability to use any information on this site."]
      [:li.spaced "The Archway Theatre Company takes no responsibility for the contents of linked websites and links should"
       " not be taken as endorsement of any kind."
       "We have no control over the availability of the linked pages"]
      [:li.spaced "No material from this Website may be copied, modified, republished, posted, transmitted, or distributed in"
       " any way, without the prior written permission of The Archway Theatre Company."]
      [:li.spaced "For further information about this site please reach out to us via our "
       [:a.simple {:href "contact.html"} "contact page"] ". Feedback is always welcome."]]]]])
