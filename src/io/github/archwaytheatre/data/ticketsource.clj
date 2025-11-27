(ns io.github.archwaytheatre.data.ticketsource
  (:require [clojure.java.io :as io]
            [clojure.pprint :as pp]
            [clojure.string :as str]
            [io.github.archwaytheatre.data.copy-images :as copy-images]
            [io.github.archwaytheatre.data.plays :as plays])
  (:import [java.net URI]
           [java.net.http HttpClient HttpRequest HttpResponse HttpResponse$BodyHandlers]
           [java.time Duration LocalDate]
           [java.util.zip GZIPInputStream]
           [org.jsoup Jsoup]
           [org.jsoup.nodes Attributes Document Element]))


(defonce client ^HttpClient (HttpClient/newHttpClient))

(defn ungzip-bytes-to-string [gzipped-byte-array]
  (with-open [reader (-> gzipped-byte-array
                         (io/input-stream)
                         (GZIPInputStream.)
                         (io/reader :encoding "UTF-8"))]
    (slurp reader)))

(defn bytes-to-string [byte-array]
  (with-open [reader (-> byte-array
                         (io/input-stream)
                         (io/reader :encoding "UTF-8"))]
    (slurp reader)))

(def root-url "https://www.ticketsource.co.uk")
(def index-page "/archwaytheatre")

(defn fetch-raw-html ^String [page-url]
  (let [user-agent "ATWebBuilder/1.0 (https://archwaytheatre.co.uk/; arch@archwaytheatre.co.uk)"
        request ^HttpRequest (-> (HttpRequest/newBuilder)
                                 (.uri (URI/create (str root-url page-url)))
                                 (.header "User-Agent" user-agent)
                                 (.header "Accept-Encoding" "gzip")
                                 (.header "Referrer" "https://archwaytheatre.co.uk/")
                                 (.timeout (Duration/ofMinutes 1))
                                 (.build))
        response ^HttpResponse (.send client request (HttpResponse$BodyHandlers/ofByteArray))]
    (println "GET:" page-url)
    (println "HTTP:" (.statusCode response))
    (println (.map (.headers response)))

    (or
      (when-let [encoding (.get (.map (.headers response)) "content-encoding")]
        (when (some #{"gzip"} encoding)
          (-> response
              (.body)
              (ungzip-bytes-to-string))))
      (-> response
          (.body)
          (bytes-to-string)))))

(defn fetch-doc ^Document [page]
  (Jsoup/parse (fetch-raw-html page)))

(defn select-first [element css-selector]
  (first (.select element css-selector)))

(defn extract-data [^Element result-card]
  (let [get-attr (fn [^Element el attr-name]
                   (let [attrs ^Attributes (.attributes el)]
                     (when (.hasKey attrs attr-name)
                       (.get attrs attr-name))))
        get-prop (fn [prop-name]
                   (when-let [el (select-first result-card (str "span[itemprop=\"" prop-name "\"]"))]
                     (or (get-attr el "content")
                         (.text el))))]
    {:event-name (get-prop "name")
     :start-date (get-prop "startDate")
     :end-date (get-prop "endDate")
     :image-url (get-prop "image")
     :location  (-> result-card
                    (select-first "div[class=\"event-location\"]")
                    (select-first "a[itemprop=\"name\"]")
                    .text)
     :link       (or (some-> result-card
                             (select-first "div[class=\"event-btn\"]")
                             (select-first "a[class=\"button\"]")
                             (get-attr "href"))
                     (some-> result-card
                             (select-first "a[class=\"card-link\"]")
                             (get-attr "href")))}))

(def replacements
  {"Hogfather" "Terry Pratchett's Hogfather"})

(def to-ignore
  #{"Archway Gift Card / Membership"})

(defn sanitize-name [x]
  (str/lower-case
    (if-let [r (get replacements x)]
      r
      (-> x
          (str/replace " - " " ")
          (str/replace "," " ")
          (str/replace #"\s+" " ")))))

(defn ingest-from [doc]
  (let [events (map extract-data (.select doc "div[class*=\"result-card\"]"))
        ;known-events (set (map str/lower-case (concat (map :name (plays/get-all-future-productions)) to-ignore)))
        ;unknown-events (remove #(-> % :event-name sanitize-name known-events) events)
        ]
    ;(println known-events)
    ;(println (map #(-> % :event-name sanitize-name) events))
    ;unknown-events
    (remove #(-> % :event-name to-ignore) events)))

(defn get-known-event-codes []
  (set (concat ["rulesliving"
                "ahplmata"
                "goodnight"
                "wonderland"
                "zimbabwean"
                "educatingrita"
                "tciotditnt"
                "ahoft"]
               (map str/lower-case (map (comp :production-code meta) (plays/get-all-future-productions))))))

(defn split-name [text regex offset?]
  (when-let [bits (re-matches regex text)]
    (vec (take 3 (concat (if offset? [nil] []) (rest bits) [nil nil nil])))))

(defn make-name [title]
  (if (< (count title) 25)
    [nil title nil]
    (let [clean-title (-> title
                          (str/replace #"[:_-]" " ")
                          (str/replace #"\s+" " "))]
      (println 'clean-title clean-title)
      (or
        (split-name clean-title #"(?i)(Dingbats Improv Jam) (.+)" true)
        (split-name clean-title #"(?i)(.* presents) (.*)" false)
        (split-name clean-title #"(?i)(.*'s) (.*)" false)
        (split-name clean-title #"(?i)(the .* of) (.*)" false)
        (split-name clean-title #"(?i)(the) (.*)" false)
        [nil title nil]))))

(defonce word->score
  (delay
    (into {}
          (map-indexed (fn [idx line]
                         (let [i (str/index-of line ",")]
                           [(subs line 0 i) idx]))
                       (rest (str/split-lines (slurp "./dev/ngram_freq.csv")))))))

(defn word-frequency-score [word]
  (@word->score (str/lower-case word) Long/MAX_VALUE))

(defn wordify-numbers [word]
  (-> word
      (str/replace "0" "oh")
      (str/replace "1" "one")
      (str/replace "2" "two")
      (str/replace "3" "three")
      (str/replace "4" "four")
      (str/replace "5" "five")
      (str/replace "6" "six")
      (str/replace "7" "seven")
      (str/replace "8" "eight")
      (str/replace "9" "nine")))

(defn sanitize [text]
  (when text
    (str/replace text #"[^ a-zA-Z-]" "")))

(defn make-short-code [[part-1 part-2 part-3] start-date]
  (let [part-1s (sanitize part-1)
        part-2s (sanitize part-2)
        part-3s (sanitize part-3)
        words (str/split (str/join " " [part-1s part-2s part-3s]) #"\s+")]
    ; if more than 5 words
    (cond
      (< 5 (count words))
      (str/lower-case (str/join (map (comp first wordify-numbers) words)))

      (= [nil "Dingbats Improv Jam"] [part-1 part-2])
      (str "improvjam" (str/lower-case (subs part-3 0 3)))

      (= [nil "Comedy Cottage" nil] [part-1 part-2 part-3])
      (str "comedycottage" (str/lower-case (subs (str (LocalDate/.getMonth (LocalDate/parse start-date))) 0 3)))

      :else
      (let [word-data (map-indexed (fn [idx [priority word]]
                                     (let [wordified (wordify-numbers word)]
                                       {:position idx
                                        :score    (word-frequency-score word)
                                        :word     wordified
                                        :priority (if (not= word wordified)
                                                    2
                                                    priority)}))
                                   (concat
                                     (when part-1s (map (juxt (constantly 1) identity) (str/split part-1s #"\s+")))
                                     (when part-2s (map (juxt (constantly 0) identity) (str/split part-2s #"\s+")))
                                     (when part-3s (map (juxt (constantly 1) identity) (str/split part-3s #"\s+")))))
            short-names (for [word-count (range (count words))]
                          (->> word-data
                               (sort-by :score >)
                               (sort-by :priority)
                               (take (inc word-count))
                               (sort-by :position)
                               (map :word)
                               (str/join)
                               (str/lower-case)))
            best (first (sort-by (fn [candidate] (abs (- 12 (count candidate)))) short-names))]
        (println word-data)
        best))))

(defonce !templates (volatile! []))

(defn reset-templates! []
  (vreset! !templates []))

(defn create-template-event! [{:keys [event-name location start-date end-date image-url link]} known-event-code?]
  (let [name-parts (make-name event-name)
        short-code (make-short-code name-parts start-date)]
    (when (not (known-event-code? short-code))
      (let [d (fetch-doc link)
            title-div (select-first d "div[class=\"eventTitle\"]")
            title-text (.text title-div)
            about-div (select-first d "div[class=\"eventText\"]")
            about-text (or (some-> about-div (.text)) "")
            [part-1 part-2 part-3 :as name-parts] (make-name title-text)
            short-code (make-short-code name-parts start-date)
            template {:code       short-code
                      :image      image-url
                      :about-data {:name-parts {:part-1 part-1
                                                :part-2 part-2
                                                :part-3 part-3}
                                   :director   nil
                                   :start      start-date
                                   :end        (or end-date start-date)
                                   :exclusions []
                                   :author     nil
                                   :ticketurl  (str root-url link)
                                   :location   (case location
                                                 "The Archway Theatre" "Main House"
                                                 "Archway Studio Theatre" "Studio Theatre"
                                                 "The Archway Theatre")
                                   :about-text about-text}}]

        (when (or (nil? short-code)
                  (= name-parts [nil nil nil]))
          (println ">>>" (pr-str title-text)))

        (when (not (known-event-code? short-code))
          (vswap! !templates conj template))

        ))))

(defn offer-templates []
  (doseq [[idx template] (map-indexed vector @!templates)]
    (println (str "\n==================> Template " (inc idx) " <=================="))
    (pp/pprint template)))

(defn use-template! [template-number]
  (let [{:keys [code image about-data]} (nth @!templates (dec template-number))
        prod (plays/create-production code about-data)
        {:keys [production-code production-year]} (meta prod)]
    (copy-images/upload-poster production-code production-year image)))

(defn check-for-new-events []
  (let [index-doc (fetch-doc index-page)
        current-events (ingest-from index-doc)
        known-event-code? (get-known-event-codes)]
    (reset-templates!)
    (doseq [event current-events]
      (create-template-event! event known-event-code?))
    (offer-templates)))

(comment

  (def x (fetch-doc index-page))
  (def xs (ingest-from x))
  (def f (first xs))

  (check-for-new-events)
  (use-template! 1)

  )
