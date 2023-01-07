(ns io.github.archwaytheatre.site.core
  (:require [clojure.java.io :as io]
            [hiccup.page :as hiccup.page]
            [hiccup.core :as hiccup.core]
            [clojure.string :as string])
  (:import [java.time Year]))

(defn classes [& stuffs]
  (->> stuffs
       (remove nil?)
       (string/join " ")))

(defn style [m]
  {:style (string/join ";" (map (fn [[k v]]
                                  (str (name k) ":" v))
                                m))})

(def creative-commons-by-sa-2.0
  ["CC BY-SA 2.0" "https://creativecommons.org/licenses/by-sa/2.0/"])

(defn image [src width rights-holder [license-label license-link]]
  [:div.attributed.image {:style (str "max-width: " width ";")}
   [:img {:src src}]
   [:div.attribution
    [:span rights-holder [:a {:href license-link} license-label]]]])

(def void?
  #{"area" "base" "br" "col" "command" "embed" "hr" "img" "input"
    "keygen" "link" "meta" "param" "source" "track" "wbr"})

(def do-not-touch? #{"pre"})

(defn prettify [html-string]
  (loop [[[match slash tag attrs content] & matches] (re-seq #"<(/?)([^<>\s]+)([^>]*)>([^<>]*)" html-string)
         indent ""
         output ""]
    (if match
      (let [[i1 i2] (cond
                      (= tag "!DOCTYPE") [indent indent]
                      (void? tag) [indent indent]
                      (= slash "/") [(subs indent 2) (subs indent 2)]
                      :else [indent (str indent "  ")])
            new-output (if (do-not-touch? tag)
                         (if (string/blank? slash)
                           (str output i1 match)
                           (str output "<" slash tag attrs ">\n" i2 content "\n"))
                         (str output i1 "<" slash tag attrs ">\n"
                              (when-not (string/blank? content)
                                (str i2 content "\n"))))]
        (recur matches
               i2
               new-output))
      output)))

(def menu-list
  [["What's On?" "index.html"]
   ["Get Involved!" "getinvolved.html"]
   ["Find Us" "findus.html"]
   ["Contact" "contact.html"]
   ["☰" "everythingelse.html"]])

(defn menu [current-page-filename]
  [:nav.dark
   (for [[label href] menu-list]
     [:a {:class (if (= current-page-filename href) "selected" "")
          :href  href}
      label])])

(defn page [page-name title & content]
  (let [filename (str page-name ".html")]
    (spit
      (io/file "docs" filename)
      (prettify
        (hiccup.page/html5
          [:head
           [:meta {:charset "utf-8"}]
           [:title title]
           [:link {:rel "stylesheet" :href "./css/style.css"}]
           [:link {:rel "icon" :type "image/png" :href "favicon.png"}]
           [:script {:src "./js/popup.js"}]]
          [:body
           [:div.prenav
            [:div
             [:img {:src "archway_header.png"}]]
            [:div "A thriving theatre in the heart of Horley."]]
           (menu filename)
           #_[:div.popupContainer
            [:div#popup {:onclick "javascript:hidePopups();"}
             [:div#popupText "Popup"]]]
           [:section (hiccup.core/html content)]
           [:br]
           [:footer (str "&copy; 2022-" (str (Year/now)) " The Archway Theatre")]])))
    (println "Wrote" filename)))

