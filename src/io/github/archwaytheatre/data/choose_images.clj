(ns io.github.archwaytheatre.data.choose-images
  (:require [clojure.java.io :as io]
            [io.github.archwaytheatre.data.copy-images :as copy-images])
  (:import [java.awt BorderLayout Dimension GraphicsEnvironment Toolkit]
           [java.awt.event KeyListener]
           [java.io File]
           [javax.imageio ImageIO]
           [javax.swing ImageIcon JFrame JLabel WindowConstants]))


(def conj-set (fnil conj #{}))

(defn photo [data file]
  (let [id (keyword (gensym "photo-"))]
    (assoc data id {:file file})))

(defn is-better-than [data a b]
  (-> data
      (update-in [b :up] conj-set a)
      (update-in [a :down] conj-set b)))

(defn ranking [data k]
  (let [{:keys [exclude? include?]} (k data)
        include! [0 0 0 0]
        exclude! [Integer/MAX_VALUE 0 0 0]]
    (cond
      (= include? exclude?) (loop [ups (get-in data [k :up])
                                   downs (get-in data [k :down])
                                   total #{}]
                              (let [ups' (apply conj ups (mapcat #(get-in data [% :up]) ups))
                                    downs' (apply conj downs (mapcat #(get-in data [% :down]) downs))
                                    total' (apply conj total (concat ups' downs'))]
                                (if (= [ups downs]
                                       [ups' downs'])
                                  [(/ (inc (count ups)) (inc (count downs)))
                                   (count ups)
                                   (count downs)
                                   (count total)]
                                  (recur ups' downs' total'))))
      include? include!
      exclude? exclude!)))

(defn all-rankings [data]
  (sort-by (comp first second) (for [[k _] data]
                                 [k (ranking data k) (get-in data [k :file])])))

(defn all-ranked? [data]
  #_(let [rankings (all-rankings data)
        min-comparisons (dec (count data))]
    (every? #(-> % second last (>= min-comparisons)) rankings))
  (loop [touched #{(first (keys data))}]
    (let [touched' (-> touched
                       (into (mapcat #(get-in data [% :up]) touched))
                       (into (mapcat #(get-in data [% :down]) touched)))]
      (if (= touched touched')
        (= (count touched) (count data))
        (recur touched')))))

(defn least-ranked [data]
  (first (apply min-key (fn [[_ {:keys [up down]}]]
                          (+ (count up) (count down)))
                data)))

(defn least-ranked-against [data k]
  (let [{:keys [up down]} (k data)
        already-ranked-against (-> #{k} (into up) (into down))
        unranked-keys (remove already-ranked-against (keys data))]
    (cond
      (= 1 (count unranked-keys)) (first unranked-keys)
      :else (least-ranked (select-keys data unranked-keys)))))

(defn- init [dir]
  (reduce photo {} (filter #(.isFile ^File %) (.listFiles (io/file dir)))))

(defn widest-screen []
  (let [ge (GraphicsEnvironment/getLocalGraphicsEnvironment)
        gds (.getScreenDevices ge)]
    (apply max-key #(-> % .getDisplayMode .getWidth) gds)))

(def screen (delay (widest-screen)))
(def image-icon-A (ImageIcon.))
(def image-icon-B (ImageIcon.))

(defn new-frame [width height]
  {:width  width
   :height height
   :frame  (doto (JFrame.)
             (.setTitle "title")
             (.add (JLabel. ^ImageIcon image-icon-A) BorderLayout/WEST)
             (.add (JLabel. ^ImageIcon image-icon-B) BorderLayout/EAST)
             (-> .getContentPane (.setPreferredSize (Dimension. width height)))
             (.setDefaultCloseOperation WindowConstants/DISPOSE_ON_CLOSE)
             (.pack)
             (.setVisible true))})

(defn side-by-side-dimensions [[width height]]
  (let [max-width (/ (.width (.getBounds (.getDefaultConfiguration @screen))) 2)
        max-height (.height (.getBounds (.getDefaultConfiguration @screen)))
        width-sf (/ max-width width)
        height-sf (/ max-height height)
        max-sf (min 1.0 width-sf height-sf)]
    [(* width max-sf) (* height max-sf)]))

(defmacro beep [& body]
  `(do ~@body
       (.beep (Toolkit/getDefaultToolkit))))

(defn do-ui [frame !data k1 k2]
  (let [img-A (copy-images/resize-image (ImageIO/read ^File (:file (@!data k1))) side-by-side-dimensions)
        img-B (copy-images/resize-image (ImageIO/read ^File (:file (@!data k2))) side-by-side-dimensions)
        result (promise)
        listener (reify KeyListener
                   (keyTyped [_ _key-event])
                   (keyPressed [_ key-event]
                     (let [value (.getKeyCode key-event)]
                       (case value
                         87 (beep (println "definitely include the image on the left") (swap! !data assoc-in [k1 :include?] true)) ; W
                         84 (beep (println "prefer the image on the left") (swap! !data is-better-than k1 k2))         ; T
                         67 (beep (println "definitely exclude the image on the left") (swap! !data assoc-in [k1 :exclude?] true)) ; C
                         70 (beep (println "definitely include the image on the right") (swap! !data assoc-in [k2 :include?] true)) ; F
                         78 (beep (println "prefer the image on the right") (swap! !data is-better-than k2 k1))         ; N
                         76 (beep (println "definitely exclude the image on the right") (swap! !data assoc-in [k2 :exclude?] true)) ; L
                         :noop)))
                   (keyReleased [_ key-event]
                     (when (#{32 10} (.getKeyCode key-event)) ; Space, Enter
                       (println "next!")
                       (deliver result :done!))))]
    (try
      (.setImage image-icon-A img-A)
      (.setImage image-icon-B img-B)
      (.addKeyListener (:frame frame) listener)
      (.pack (:frame frame))
      @result
      (finally
        (.removeKeyListener (:frame frame) listener)))))

(defn- step [frame !data]
  (let [data @!data
        k1 (least-ranked data)
        k2 (least-ranked-against data k1)]
    (do-ui frame !data k1 k2)))

(defn go! [directory number-or-proportion]
  (let [screen @screen
        width (.x (.getBounds (.getDefaultConfiguration screen)))
        height (.y (.getBounds (.getDefaultConfiguration screen)))
        frame (new-frame width height)
        data (init directory)
        !data (atom data)
        target (min (count data)
                    (if (integer? number-or-proportion)
                      number-or-proportion
                      (Math/ceil (* number-or-proportion (count data)))))
        output-directory (->> (range)
                              (map #(io/file directory (str "whittle-" %)))
                              (drop-while #(.exists %))
                              (first))]
    (println (str "Choosing " target " out of " (count data) " photos..."))
    (println "W - definitely include the image on the left")
    (println "T - prefer the image on the left")
    (println "C - definitely exclude the image on the left")
    (println "F - definitely include the image on the right")
    (println "N - prefer the image on the right")
    (println "L - definitely exclude the image on the right")
    (println "Space / Enter - Advance to next image.")
    ;(pprint/pprint (all-rankings @!data))
    (if (.mkdir output-directory)
      (do
        (.setFullScreenWindow screen (:frame frame))
        (while (not (all-ranked? @!data))
          (step frame !data))
        (.dispose ^JFrame (:frame frame))
        ;(pprint/pprint (all-rankings @!data))
        (doseq [[idx file] (map-indexed vector (take target (map last (all-rankings @!data))))]
          (println (str (inc idx) ". " file))
          (io/copy file (io/file output-directory (.getName file)))))
      (println "cannot write to directory!"))))

(comment

  ; TODO: rescale all images upfront and load into memory?
  ; TODO: progress indicator?
  ; TODO: rejected images?
  ; TODO: automate!!!
  ;         rate the interesting-ness of the faces
  ;           size, emotional extremity, focus
  ;           color distribution in individual picture, and of the set
  ;         recognize the people and ensure 1 of each actor?

  (go! "/home/rachel/Downloads/Peter Pan 2024" 20)
  (go! "/home/rachel/Downloads/Photos Wakehurst Midsummer-20240721T122250Z-001/Photos Wakehurst Midsummer" 20)

  )
