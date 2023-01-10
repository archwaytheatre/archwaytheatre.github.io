(ns io.github.archwaytheatre.build
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))


(defn discover-namespaces []
  (sequence
    (comp (filter #(-> % .getName (string/ends-with? ".clj")))
          (map (fn [file]
                 (->> file slurp string/split-lines first
                      (re-find #"io.github.archwaytheatre.site.[\S]+")
                      symbol))))
    (file-seq (io/file "./src/io/github/archwaytheatre/site"))))

(defn require-namespaces []
  (apply require (conj (vec (discover-namespaces)) :reload-all)))


(defn build [& _opts]
  (println "Building...")
  (time
    (do
      (require-namespaces)
      (println "Built."))))
