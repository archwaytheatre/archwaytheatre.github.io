(ns io.github.archwaytheatre.site.signup
  (:require [io.github.archwaytheatre.site.core :as core]))

(core/page-2 "signup" "The Archway Theatre"
  [:div.content
   [:div.signupform (core/email-signup-form "Subscribe to our mailing list:")]])
