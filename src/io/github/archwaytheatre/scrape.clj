(ns io.github.archwaytheatre.scrape
  (:require
    [clj-http.client :as client]
    [clojure.java.io :as io]
    [clojure.pprint :as pprint]
    [clojure.string :as string]
    [hickory.core :as hickory]))


(def root-url "https://www.archwaytheatre.com")
(def root-url' "http://www.archwaytheatre.com")
(def some-url "/behind-the-scenes-home/")

(defn trim-link [url]
  (cond
    (string/starts-with? url root-url) (subs url (count root-url))
    (string/starts-with? url root-url') (subs url (count root-url'))
    (string/starts-with? url "https://") nil
    (string/starts-with? url "http://") nil
    :else url))

(defn sanitize [url]
  (-> url
      (string/replace #"[^a-zA-Z0-9-/]" "")
      (string/replace #"^/" "")
      (string/replace #"/$" "")))

(def manual-redirects
  {"http://www.archwaytheatre.com/author/admin/"
   ::stop

   "http://www.archwaytheatre.com/archwaytheatre/history/how-it-all-began/"
   "http://www.archwaytheatre.com/history/how-it-all-began/"
   })

(defn resolve-redirects [url]
  (try
    (or (manual-redirects url)
        (last (:trace-redirects (client/get url)))
        url)
    (catch Exception _
      (println "bad redirect" url)
      url)))

(defn copy-page [label article]
  (let [target (io/file "src"
                       "io/github/archwaytheatre/site/content"
                       (str (string/replace label #"-" "_") ".clj"))]
    (io/make-parents target)
    (spit
      target
      (string/join "\n"
        [(str "(ns io.github.archwaytheatre.site.content." (string/replace label #"/" "."))
         "  (:require [io.github.archwaytheatre.site.core :as core]))"
         ""
         (str "(core/page \"content/" label "\" \"The Archway Theatre\"")
         "  [:div.content"
         (string/join "\n    "
           (string/split-lines
             (str "    " (with-out-str
                           (pprint/pprint article)))))
         "  ])"]))))

(def !seen? (volatile! #{::stop}))

(defn parse-article [url]
  (let [url (resolve-redirects url)]
    (when-not (@!seen? url)
      (println "slurping: " url)
      (let [text (slurp url)
            label (sanitize (trim-link url))
            nesting (count (re-seq #"/" label))
            relative-path (string/join (repeat nesting "../"))
            start (string/index-of text "<article")
            end (string/index-of text "</article>")
            content (subs text start (+ end 10))
            link-bits (->> content
                           (re-seq #"href=\"([^\"]+)\"")
                           (map second)
                           (keep
                             (fn [raw-link]
                               (let [resolved (resolve-redirects raw-link)
                                     trimmed (trim-link resolved)]
                                 (when trimmed
                                   [raw-link
                                    trimmed
                                    (str relative-path (sanitize trimmed) ".html")])))))
            raw-links (map first link-bits)
            trimmed-links (map second link-bits)
            raw->trimmed (zipmap (map first link-bits)
                                 (map last link-bits))
            content' (reduce-kv string/replace content raw->trimmed)
            article (hickory/as-hiccup
                      (first (hickory/parse-fragment content')))]
        (println (count trimmed-links) "links")
        (copy-page label article)
        (vswap! !seen? conj url)
        (mapv parse-article raw-links)
        ))))

(comment
  (parse-article "https://www.archwaytheatre.com/behind-the-scenes-home/")
  (parse-article "https://www.archwaytheatre.com/history/")
  (parse-article "https://www.archwaytheatre.com/history/central-players/")
  (parse-article "http://www.archwaytheatre.com/youth-workshops/")
  (parse-article "http://www.archwaytheatre.com/studio/")
  (parse-article "http://www.archwaytheatre.com/council-of-management/")

  )

