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
    (cons
      (io/file "./src/io/github/archwaytheatre/site/index.clj")
      (cons (io/file "./src/io/github/archwaytheatre/site/whatson.clj")
            (or (seq (remove nil? files))
                (file-seq (io/file "./src/io/github/archwaytheatre/site")))))))

(defn require-namespaces [files]
  (let [nss (vec (discover-namespaces files))]
    (println nss)
    (apply require (conj nss :reload-all))))

(defn build [& files]
  (println "Building...")
  (time
    (try
      (require-namespaces files)
      (println "Built.")
      (catch Exception e
        (.printStackTrace e)
        (throw e)))))
