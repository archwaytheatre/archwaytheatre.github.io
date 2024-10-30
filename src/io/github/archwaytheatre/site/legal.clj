(ns io.github.archwaytheatre.site.legal
  (:require
    [io.github.archwaytheatre.site.core :as core])
  (:import [java.time Year]))

(core/page-2 "legal" "Legal & Site Info"
  [:div.content

   [:div.content__item
    [:h1 "Legal &amp; Site Info"]]

   [:div.content__item
    [:ul
     [:li.list__item__spaced [:b "Copyright"] [:br]
      "All rights reserved."
      " These materials (including without limitation all articles, text, images, logos etc.) are"
      " Copyright " (str "&copy; 1987-" (str (Year/now)) " The Archway Theatre Company.")
      " The Archway Theatre Company endeavours to ensure that the information on this website is correct but does not"
      " accept any liability for error or omission."]
     [:li.list__item__spaced [:b "Disclaimer"] [:br]
      "Every effort has been made to ensure the accuracy of information presented."
      " However, no warranty or guarantee, expressed or implied, concerning the content or accuracy of the information"
      " stored and maintained within this information service is made. "
      "The information is presented ‘as is’. Price and availability of information is subject to change without"
      " notice."]
     [:li.list__item__spaced
      "The Archway Theatre Company will not be liable (to the fullest extent permitted at law) for any loss,"
      " damage or inconvenience arising as a consequence of any use of or the inability to use any information on this"
      " site."]
     [:li.list__item__spaced
      "The Archway Theatre Company takes no responsibility for the contents of linked websites and links should"
      " not be taken as endorsement of any kind."
      "We have no control over the availability of the linked pages"]
     [:li.list__item__spaced
      "No material from this Website may be copied, modified, republished, posted, transmitted, or distributed in"
      " any way, without the prior written permission of The Archway Theatre Company."]
     [:li.list__item__spaced
      "For further information about this site please reach out to us via our "
      [:a.normal {:href "contact.html"} "contact page"] ". Feedback is always welcome."]]]])
