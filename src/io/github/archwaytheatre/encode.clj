(ns io.github.archwaytheatre.encode
  (:require [clojure.string :as string]))


(defn encode [text]
  (doseq [p (partition-all 10
                           (map int text)
                           #_(map #(str "&#" (int %) ";") text))]
    (println (str "" (string/join ", " p)))))


(def x
  ["...email addresses go here..."])

(doseq [y x]
  (println y (str "[" (string/join ", " (map int y)) "]"))
  #_(encode y))
