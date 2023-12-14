(ns io.github.archwaytheatre.build
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))


(defn discover-namespaces [files]
  (sequence
    (comp (remove #(-> % .getPath (string/includes? "content")))
          (filter #(-> % .getName (string/ends-with? ".clj")))
          (map (fn [file]
                 (->> file slurp string/split-lines first
                      (re-find #"io.github.archwaytheatre.site.[\S]+")
                      symbol))))
    (or (seq (remove nil? files))
        (file-seq (io/file "./src/io/github/archwaytheatre/site")))))

(defn require-namespaces [files]
  (apply require (conj (vec (discover-namespaces files)) :reload-all)))

(defn build [& files]
  (println "Building...")
  (time
    (do
      (require-namespaces files)
      (println "Built."))))
