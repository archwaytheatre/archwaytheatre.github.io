(ns io.github.archwaytheatre.color
  (:require [clojure.string :as string])
  (:import [java.awt Color]))


(defn color-range [prefix dark-color-str light-color-str]
  (let [dark-color (Color. (Integer/decode dark-color-str))
        light-color (Color. (Integer/decode light-color-str))
        lerp (fn [a p b]
               (+ (* a (- 1 p)) (* b p)))
        colerp (fn [color-1 proportion color-2]
                 (let [[r1 g1 b1] (.getRGBColorComponents color-1 nil)
                       [r2 g2 b2] (.getRGBColorComponents color-2 nil)]
                   (Color. (float (lerp r1 proportion r2))
                           (float (lerp g1 proportion g2))
                           (float (lerp b1 proportion b2)))))
        new-color (fn [label a prop b]
                    (str "--" prefix "-" label ": #"
                         (Integer/toHexString
                           (bit-and 0xffffff
                                    (.getRGB (colerp a prop b)))) ";"))]
    (println
      (string/join "\n"
        [(new-color "shadow" dark-color 0.425 Color/BLACK)
         (new-color "1" dark-color 0 light-color)
         (new-color "2" dark-color 0.25 light-color)
         (new-color "3" dark-color 0.5 light-color)
         (new-color "4" dark-color 0.75 light-color)
         (new-color "5" dark-color 1 light-color)
         (new-color "light" light-color 0.5 Color/WHITE)]))))
