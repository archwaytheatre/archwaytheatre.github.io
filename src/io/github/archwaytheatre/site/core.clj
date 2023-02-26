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
   ["Get Involved!" "getinvolved.html"]
   ["Find Us" "findus.html"]
   ["Contact" "contact.html"]
   ["☰" "everythingelse.html"]])

(defn social-media-logos [labelled?]
  (let [icon (fn [title url image]
               [:a.logo {:title title :href url}
                [:div.logoholder
                 [:div.logo
                  [:img.logo {:src image}]]
                 (when labelled? [:div title])]])]
    [:div
     (icon "Instagram" "http://instagram.com/archwaytheatre/" "/images/logos/Instagram.svg")
     (icon "Twitter" "http://twitter.com/ArchwayHorley" "/images/logos/Twitter.png")
     (icon "Facebook" "https://www.facebook.com/ArchwayTheatre/" "/images/logos/Facebook.png")
     (icon "YouTube" "https://www.youtube.com/channel/UCrbh4hS_gw0hb811tILRdBA" "/images/logos/YouTube.png")]))

(defn menu [relative-path current-page-filename]
  [:nav.dark
   (for [[label href] menu-list]
     [:a {:class (classes (when (= current-page-filename href) "selected")
                          (when (= 1 (count label)) "short"))
          :href  (str relative-path href)}
      label])])

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
           [:script {:src (str relative-path "js/popup.js")}]]
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
           [:section.container
            (hiccup.core/html content)]
           ;[:br]
           [:footer (social-media-logos false) (str "&copy; 1987-" (str (Year/now)) " The Archway Theatre Company")]])))
    (println "Wrote" filename)))

