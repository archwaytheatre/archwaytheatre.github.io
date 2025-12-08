(ns io.github.archwaytheatre.data.core
  (:require
    [clojure.java.io :as io]
    [clojure.string :as str]))


(def data-dir (io/file "data"))
(def event-dir (io/file data-dir "events"))
(def s3-protocol "s3://")
(def s3-bucket "archwaytheatre")
(def s3-dir (str s3-protocol s3-bucket "/site"))
(def s3-temp-dir (str s3-protocol s3-bucket "/temp"))
(def _asset-url-prefix "https://archwaytheatre.s3.eu-west-2.amazonaws.com/site/")
(def asset-url-prefix "https://d35974u23x7pxe.cloudfront.net/")

(defn codify [production-name]
  (-> production-name
      (str/replace #".jpg$" "")
      (str/replace #"1984" "nineteen-eighty-four")
      (str/replace #"\s" "-")
      (str/replace #"[^a-zA-Z0-9-]" "")
      str/lower-case))
