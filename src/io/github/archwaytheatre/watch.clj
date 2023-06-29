(ns io.github.archwaytheatre.watch
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [io.github.archwaytheatre.build :as build]
            [juxt.dirwatch :as dirwatch])
  (:import [java.awt Desktop]
           [java.net URI]
           [java.util Timer TimerTask]))


(defn debounce
  ([f] (debounce f 1000))
  ([f timeout]
   (let [timer (Timer.)
         task (atom nil)]
     (fn [& args]
       (some-> @task .cancel)
       (let [new-task (proxy [TimerTask] []
                        (run []
                          (apply f args)
                          (reset! task nil)
                          (.purge timer)))]
         (reset! task new-task)
         (.schedule timer new-task (long timeout)))))))

(defn make-safe [f]
  (fn [& args]
    (try
      (apply f args)
      (catch Exception e
        (println "FAILURE:" (ex-message e))))))

(defn local-deploy []
  (let [local-dir (io/file "local")
        deploy-dir (io/file "docs")]
    (when (.exists local-dir)
      (doseq [source-file (file-seq deploy-dir)]
        (let [target-file (io/file (string/replace (str source-file)
                                                   (re-pattern (str "^" deploy-dir))
                                                   (str local-dir)))]
          (when (and (.isFile source-file)
                     (not (string/ends-with? (str source-file) ".html")))
            (println (str source-file " -> " target-file))
            (io/make-parents target-file)
            (io/copy source-file target-file)))))))

(defn open []
  (.browse (Desktop/getDesktop)
           (URI. "http://localhost:63342/archwaytheatre.github.io/local/index.html")))

(defn on-change [f & files]
  (let [g (debounce (make-safe f) 100)]
    (apply
      dirwatch/watch-dir
      (fn [{:keys [file]}]
        (when-not (string/ends-with? (str file) "~")
          (g)))
      (map io/file files))))

(defn watch [& _opts]
  (println "watching...")
  (build/build)
  (local-deploy)
  (open)

  (on-change #(build/build) "src" "data")
  (on-change #(local-deploy) "docs")

  (Thread/sleep (* 7 24 60 60 1000))
  (println "Got bored and gave up watching. Sorry."))
