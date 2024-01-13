(ns io.github.archwaytheatre.data.core
  (:require
    [clojure.java.io :as io]
    [clojure.string :as str]))


(def data-dir (io/file "data"))

(defn codify [production-name]
  (-> production-name
      (str/replace #".jpg$" "")
      (str/replace #"\s" "-")
      (str/replace #"[^a-zA-Z-]" "")
      str/lower-case))
