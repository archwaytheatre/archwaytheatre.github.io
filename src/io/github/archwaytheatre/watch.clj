(ns io.github.archwaytheatre.watch
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [io.github.archwaytheatre.build :as build]
            [juxt.dirwatch :as dirwatch]))

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

(defn watch [& _opts]
  (println "watching...")
  (build/build)
  (local-deploy)

  (dirwatch/watch-dir
    (fn [{:keys [file] :as event}]
      (println event)
      (when-not (string/ends-with? (str file) "~")
        (build/build)))
    (io/file "src"))

  (dirwatch/watch-dir
    (fn [{:keys [file] :as event}]
      (println event)
      (when-not (string/ends-with? (str file) "~")
        (local-deploy)))
    (io/file "docs"))
  (Thread/sleep (* 7 24 60 60 1000))
  (println "Got bored and gave up watching. Sorry."))
