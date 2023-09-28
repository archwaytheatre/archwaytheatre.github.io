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

(defn linkify [^String text ^ String year]
  (string/replace (str text "-" year) #"[^a-zA-Z0-9]+" "-"))

(def creative-commons-by-sa-2.0
  ["CC BY-SA 2.0" "https://creativecommons.org/licenses/by-sa/2.0/"])

(defn image [src width rights-holder [license-label license-link]]
  [:div.attributed.image {:style (str "max-width: " width ";")}
   [:img {:src src}]
   [:div.attribution
    [:span rights-holder [:a {:href license-link} license-label]]]])

(defn you-tube [video-id]
  [:iframe.trailer
   {
    :width  "100%" ;"560"
    :height "100%" ;"315"
    :src    (str "https://www.youtube-nocookie.com/embed/" video-id) ; eg. "aJhZx0_hytA"
    :title "YouTube video player"
    :frameborder "0"
    :allow "accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
    :allowfullscreen "true"}])

(def void?
  #{"area" "base" "br" "col" "command" "embed" "hr" "img" "input"
    "keygen" "link" "meta" "param" "source" "track" "wbr"})

(def do-not-touch? #{"pre" "a"})

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
   ["Get Involved!" nil {:id      "get-involved"
                         :submenu [["Volunteer" "getinvolved.html"]
                                   ["Audition" "auditions.html"]
                                   ["Events" "events.html"]]}]
   ["Find Us" "findus.html"]
   ["Contact" "contact.html"]
   ["About" "about.html"]
   ["☰" "everythingelse.html"]])

(defn social-media-logo
  ([title image url]
   (social-media-logo title image url nil))
  ([title image url label]
   [:a.logo {:title (or label title) :href url :target "_blank"}
    [:div.logoholder
     [:div.logo
      [:img.logo {:src image}]]]
    (when label [:div [:span.simple label]])]))

(def logo-ig (partial social-media-logo "Instagram" "/images/logos/Instagram.svg"))
(def logo-tw (partial social-media-logo "Twitter" "/images/logos/Twitter.png"))
(def logo-fb (partial social-media-logo "Facebook" "/images/logos/Facebook.png"))
(def logo-yt (partial social-media-logo "YouTube" "/images/logos/YouTube.png"))

(def social-media-logos
  [:div
   (logo-ig "http://instagram.com/archwaytheatre/")
   (logo-tw "http://twitter.com/ArchwayHorley")
   (logo-fb "https://www.facebook.com/ArchwayTheatre/")
   (logo-yt "https://www.youtube.com/channel/UCrbh4hS_gw0hb811tILRdBA")])

(defn menu [relative-path current-page-filename]
  [:nav.dark
   (for [[label href dropdown] menu-list]
     (if dropdown
       (let [{:keys [id submenu]} dropdown
             hrefs (set (map second submenu))]
         [:div.dropDownContainer {:id id
                                  ;:onmouseout  (str "dropDownUnhover('" id "');")
                                  :onmouseleave  (str "dropDownUnhover('" id "');")
                                  :class (classes "menuItem" "topMenu"
                                                  (when (hrefs current-page-filename) "selected"))
                                  }
          [:a.dropDownButton {:role    "button"
                              :href    "#0"
                              ;:onclick (str "dropDownTap('" id "');")
                              :onmouseover (str "dropDownHover('" id "');")
                              }
           label]
          [:div.dropDownMenu
           (for [[label href] submenu]
             [:div {:class (classes "menuItem" (when (= current-page-filename href) "selected"))}
              [:a {:href (str relative-path href)} label]])]])
       [:div {:class (classes "menuItem" "topMenu" (when (= current-page-filename href) "selected"))}
        [:a.menuButton {:href (str relative-path href)}
         label]]))])

(def local-dir (io/file "local"))
(def deploy-dir (io/file "docs"))
(def local? (.exists local-dir))
(def parent-dir
  (if local?
    local-dir
    deploy-dir))

(defn page [page-name title & content]
  (let [filename (str page-name ".html")
        target-file (io/file parent-dir filename)
        nesting (+ (count (re-seq #"/" page-name))
                   (if local? 0 0))
        relative-path (string/join (repeat nesting "../"))]
    (io/make-parents target-file)
    (spit
      target-file
      (prettify
        (hiccup.page/html5
          [:head
           [:meta {:charset "utf-8"}]
           [:title title]
           [:link {:rel "stylesheet" :href (str relative-path "css/style.css")}]
           [:link {:rel "icon" :type "image/png" :href (str relative-path "favicon.png")}]
           [:script {:src (str relative-path "js/popup.js")}]
           [:script {:src (str relative-path "js/menudrop.js")}]]
          [:body
           [:div.prenav
            [:div
             [:a {:href "https://www.archwaytheatre.com/"}
              [:img {:src (str relative-path "archway_header.png")}]]]
            [:div "A thriving theatre in the heart of Horley."]]
           (menu relative-path filename)
           #_[:div.popupContainer
            [:div#popup {:onclick "javascript:hidePopups();"}
             [:div#popupText "Popup"]]]
           ;[:section.container]
           (hiccup.core/html content)
           ;[:br]
           [:footer social-media-logos
            [:a.simple {:href "legal.html"} (str "&copy; 1987-" (str (Year/now)) " The Archway Theatre Company")]]])))
    (println "Wrote" filename)))

