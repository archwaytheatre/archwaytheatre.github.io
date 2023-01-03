(ns io.github.archwaytheatre.core
  (:require [clojure.java.io :as io]
            [hiccup.page :as hiccup.page]
            [hiccup.core :as hiccup.core]
            [clojure.string :as string]))

(defn classes [& stuffs]
  (->> stuffs
       (remove nil?)
       (string/join " ")))

(defn menu [menu-items]
  [:nav
   (for [{:keys [label selected? href]} menu-items]
     [:a {:class (if selected? "selected" "") :href href} label])])

(defn menu-items [index]
  (assoc-in [{:href "./index.html" :label "What's On?"}
             {:href "./getinvolved.html" :label "Get Involved!"}
             {:href "./everythingelse.html" :label "Everything Else"}]
            [index :selected?]
            true))

(def void-tag?
  #{"area" "base" "br" "col" "command" "embed" "hr" "img" "input"
    "keygen" "link" "meta" "param" "source" "track" "wbr"})

(defn prettify [html-string]
  (loop [[[match tag slash content] & matches] (re-seq #"(<(/?)[^<>]+>)([^<>]*)" html-string)
         indent ""
         output ""]
    (if match
      (let [[i1 i2] (cond
                      (= tag "<!DOCTYPE html>") [indent indent]
                      (void-tag? (re-find #"[^\s>]*" (subs tag 1))) [indent indent]
                      (= slash "/") [(subs indent 2) (subs indent 2)]
                      :else [indent (str indent "  ")])
            new-output (str output i1 tag "\n"
                            (when-not (string/blank? content)
                              (str i2 content "\n")))]
        (recur matches
               i2
               new-output))
      output)))

(defn page [page-name title menu-items & content]
  (spit
    (io/file "docs" (str page-name ".html"))
    (prettify
      (hiccup.page/html5
        [:head
         [:meta {:charset "utf-8"}]
         [:title title]
         [:link {:rel "stylesheet" :href "./css/style.css"}]
         [:script {:src "./js/popup.js"}]]
        [:body
         [:div.prenav
          [:div
           [:img {:src "archway_header.png"}]]
          [:div "A thriving theatre in the heart of Horley."]]
         (menu menu-items)
         [:div.popupContainer
          [:div#popup {:onclick "javascript:hidePopup();"}
           [:div#popupText "Popup"]]]
         [:section (hiccup.core/html content)]
         [:footer "&copy; to me"]]))))

