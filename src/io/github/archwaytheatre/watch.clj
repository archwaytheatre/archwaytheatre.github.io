(ns io.github.archwaytheatre.watch
  (:require [clojure.java.io :as io]
            [io.github.archwaytheatre.build :as build]
            [juxt.dirwatch :as dirwatch]))

(defn watch [& _opts]
  (println "watching...")
  (build/build)
  (dirwatch/watch-dir
    (fn [& args]
      (println args)
      (build/build))
    (io/file "src"))
  (Thread/sleep (* 7 24 60 60 1000))
  (println "Got bored and gave up watching. Sorry."))
