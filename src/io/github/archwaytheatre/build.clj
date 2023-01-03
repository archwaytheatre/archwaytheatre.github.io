(ns io.github.archwaytheatre.build)

(defn build [& _opts]
  (require 'io.github.archwaytheatre.index
           'io.github.archwaytheatre.getinvolved
           'io.github.archwaytheatre.everything-else
           :reload-all))
