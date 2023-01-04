(ns io.github.archwaytheatre.build)

(defn build [& _opts]
  (println "Building...")
  (time
    (do
      (require 'io.github.archwaytheatre.index
               'io.github.archwaytheatre.getinvolved
               'io.github.archwaytheatre.find-us
               'io.github.archwaytheatre.everything-else
               :reload-all)
      (println "Built."))))
