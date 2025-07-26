(ns io.github.archwaytheatre.site.photos
  (:require
    [clojure.java.io :as io]
    [clojure.string :as str]
    [io.github.archwaytheatre.data.core :as data]
    [io.github.archwaytheatre.data.plays :as plays]
    [io.github.archwaytheatre.site.core :as core])
  (:import
    [java.util ArrayList
               Collection
               Collections
               Random]))

(defn js-quote [x] (str \' (str/replace x "'" "\\'") \'))

(defn js-photo-def [def-name photos & field-fns]
  (let [photo-data-lines (for [photo photos]
                           (str "[" (str/join ", " (map #(% photo) field-fns)) "]"))]
    (str "const " def-name " = [\n" (str/join ",\n  " photo-data-lines) "\n];")))

(defn build-top-photo-data [production-lookback-count]
  (let [productions-with-photos (->> (reverse plays/year-seq)
                                     (mapcat plays/get-productions-for)
                                     (filter plays/has-photos?)
                                     (take production-lookback-count))
        photos (mapcat plays/get-photos-for productions-with-photos)
        data-file (io/file core/parent-dir "js" "top-photo-data.js")]
    (io/make-parents data-file)
    (spit data-file (js-photo-def "topPhotoData" photos
                                  :eyeline-offset
                                  (comp js-quote :image-url)
                                  (comp js-quote :photographer)
                                  (comp js-quote :production-year meta)
                                  (comp js-quote :name)))))

(defn deterministic-shuffle [^Collection coll seed]
  (let [al (ArrayList. coll)
        rng (Random. (long seed))]
    (Collections/shuffle al rng)
    (seq al)))

(defn build-about-photo-data [production-lookback-count]
  (let [productions-with-photos (->> (reverse plays/year-seq)
                                     (mapcat plays/get-productions-for)
                                     (filter plays/has-photos?)
                                     (take production-lookback-count))
        photos (mapcat plays/get-photos-for productions-with-photos)
        data-file (io/file core/parent-dir "js" "about-photo-data.js")]
    (io/make-parents data-file)
    (spit data-file (js-photo-def "photoData"
                                  (deterministic-shuffle photos (hash photos))
                                  (comp js-quote :image-url)
                                  (comp js-quote :photographer)
                                  (comp js-quote :production-year meta)
                                  (comp js-quote :name)))))

(build-top-photo-data 3)
(build-about-photo-data 3)


(when core/local?
  (core/page "testpage" "Test Page"
    (let [year "2024"
          prod-code "home-im-darling"
          production (plays/load-production-data year prod-code)
          photos (plays/get-photos-for production)]
      [:div.center
       [:script
        "
        var data = new Map();
        function style(prop) {
         return \"position:absolute;border-top:1px solid blue;width:100vw;top:\" + (prop * 100) + \"%;\";
        }
        function c(e, u, m) {
          let line = e.clientY - e.target.getBoundingClientRect().top;
          let height = e.target.getBoundingClientRect().height;
          let prop = Math.round((line / height) * 100) / 100;
          document.getElementById(m).style = style(prop);
          data.set(u, prop);
          console.log(JSON.stringify([...data]));
        }"]
       (for [{:keys [image-url eyeline-offset]} photos
             :let [marker-id (str (gensym "marker-"))
                   js-url (subs image-url (inc (str/last-index-of image-url "/")))]]
         [:div {:style "position: relative;"}
          [:div {:id    marker-id
                 :style (str "position: absolute; "
                             "border-top: 1px solid blue; "
                             "width: 100vw;"
                             "top: " (* eyeline-offset 100) "%;")}
           ""]
          [:img {:src     (str data/asset-url-prefix image-url)
                 :onClick (str "c(arguments[0], '" js-url "', '" marker-id "');")}]]

         )])))
