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

(def email-signup-form
  [:div#mc_embed_shell
   [:link {:href "//cdn-images.mailchimp.com/embedcode/classic-061523.css" :rel "stylesheet" :type "text/css"}]
   [:style {:type "text/css"} "#mc_embed_signup{width: 600px;}
        /* Add your own Mailchimp form style overrides in your site stylesheet or in this style block.
           We recommend moving this block and the preceding CSS link to the HEAD of your HTML file. */"]
   [:style {:type "text/css"} "#mc-embedded-subscribe-form input[type=checkbox]{display: inline; width: auto;margin-right: 10px;}
#mergeRow-gdpr {margin-top: 20px;}
#mergeRow-gdpr fieldset label {font-weight: normal;}
#mc-embedded-subscribe-form .mc_fieldset{border:none;min-height: 0px;padding-bottom:0px;}"]
   [:div#mc_embed_signup
    [:form#mc-embedded-subscribe-form.validate
     {:action "https://archwaytheatre.us7.list-manage.com/subscribe/post?u=66a3a3c2457ac9ffd280cf3c3&amp;id=96bd00fc78&amp;v_id=4765&amp;f_id=00ded6e4f0"
      :method "post"
      :name "mc-embedded-subscribe-form"
      :target "_blank"}
     [:div#mc_embed_signup_scroll [:h2 "Or subscribe to our mailing list:"]
      [:div.indicates-required [:span.asterisk "*"] "indicates required"]
      [:div.mc-field-group [:label {:for "mce-EMAIL"} "Email Address" [:span.asterisk "*"]]
       [:input#mce-EMAIL.required.email {:type "email" :name "EMAIL" :required "" :value ""}]]
      [:div.mc-field-group [:label {:for "mce-FNAME"} "First Name"]
       [:input#mce-FNAME.text {:type "text" :name "FNAME" :value ""}]]
      [:div.mc-field-group [:label {:for "mce-LNAME"} "Last Name"]
       [:input#mce-LNAME.text {:type "text" :name "LNAME" :value ""}]]
      [:div.mc-field-group [:label {:for "mce-MMERGE3"} "What area of the Archway interests you? (optional)"]
       [:select#mce-MMERGE3 {:name "MMERGE3" :class ""}
        [:option {:value ""}]
        [:option {:value "Audience Member"} "Audience Member"]
        [:option {:value "Performing"} "Performing"]
        [:option {:value "Volunteering"} "Volunteering"]
        [:option {:value "Archway Theatre Youth Workshop (ages 9-16)"} "Archway Theatre Youth Workshop (ages 9-16)"]]]
      [:div#mergeRow-gdpr.mergeRow.gdpr-mergeRow.content_gdprBlock.mc-field-group
       [:div.contentgdpr [:label "Marketing Permissions"]
        [:p "The Archway Theatre will use the information you provide on this form to keep you informed about productions, news, events, and activities at the The Archway Theatre"]
        [:fieldset.mc_fieldset.gdprRequired.mc-field-group {:name "interestgroup_field"}
         [:label.checkbox.subfield {:for "gdpr4141"}
          [:input#gdpr_4141.gdpr {:type "checkbox" :name "gdpr[4141]" :value "Y"}]
          [:span "Email"]]]
        [:p "You can change your mind at any time by clicking the unsubscribe link in the footer of any email you receive from us, or by contacting us at arch@archwaytheatre.com. We will treat your information with respect. For more information about our privacy practices please visit our website. By clicking below, you agree that we may process your information in accordance with these terms."]]
       [:div.content_gdprLegal
        [:p "We use Mailchimp as our marketing platform. By clicking below to subscribe, you acknowledge that your information will be transferred to Mailchimp for processing."
         [:a {:href "https://mailchimp.com/legal/terms"} "Learn more"] "about Mailchimp's privacy practices."]]]
      [:div {:hidden ""} [:input {:type "hidden" :name "tags" :value "7748193"}]]
      [:div#mce-responses.clear
       [:div#mce-error-response.response {:style "display: none;"}]
       [:div#mce-success-response.response {:style "display: none;"}]]
      [:div {:aria-hidden "true" :style "position: absolute; left: -5000px;"}
       [:input {:type "text" :name "b_66a3a3c2457ac9ffd280cf3c3_96bd00fc78" :tabindex "-1" :value ""}]]
      [:div.clear [:input#mc-embedded-subscribe.button {:type "submit" :name "subscribe" :value "Subscribe"}]]]]]
   [:script {:type "text/javascript" :src "//s3.amazonaws.com/downloads.mailchimp.com/js/mc-validate.js"}]
   [:script {:type "text/javascript"} "(function($) {window.fnames = new Array(); window.ftypes = new Array();fnames[0]='EMAIL';ftypes[0]='email';fnames[1]='FNAME';ftypes[1]='text';fnames[2]='LNAME';ftypes[2]='text';fnames[3]='MMERGE3';ftypes[3]='dropdown';}(jQuery));var $mcj = jQuery.noConflict(true);"]]

  )

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
  (try
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
      (println "Wrote" filename))
    (catch Exception e
      (.printStackTrace e)
      (throw e))))

