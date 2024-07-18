(ns io.github.archwaytheatre.data.core
  (:require
    [clojure.java.io :as io]
    [clojure.string :as str]))


(def data-dir (io/file "data"))
(def event-dir (io/file data-dir "events"))
(def s3-dir "s3://archwaytheatre/site")
(def _asset-url-prefix "https://archwaytheatre.s3.eu-west-2.amazonaws.com/site/")
(def asset-url-prefix "https://d35974u23x7pxe.cloudfront.net/")

(defn codify [production-name]
  (-> production-name
      (str/replace #".jpg$" "")
      (str/replace #"1984" "nineteen-eighty-four")
      (str/replace #"\s" "-")
      (str/replace #"[^a-zA-Z-]" "")
      str/lower-case))
