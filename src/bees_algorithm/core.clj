(ns bees-algorithm.core
  (:require [clojure.math.numeric-tower :as math]))

(defn objective-function
  [a]
  (math/expt a 2))

(defn set-fitness
  [pop]
  (map objective-function pop))

(defn random-in-range 
  [[a b]] 
  (+ a (rand (- b a))))
  
(defn create-random-bee
  [search-space]
  (map random-in-range search-space))

(defn search
  [problem-size search-space max-gens num-bees num-sites elite-sites patch-size e-bees o-bees]
  (def best nil)
  (let [pop (create-random-bee search-space)]
    (println pop)
    (let [pop-fitness (set-fitness pop)]
      (println pop-fitness)
      (def sorted-pop-fitness (sort pop-fitness))
      (println sorted-pop-fitness))))

(defn -main
  [& args]
  (search 3 (repeat 3 [-5 5]) 500 45 3 1 3 7 2))