(ns bees-algorithm.core
  (:require [clojure.math.numeric-tower :as math]))

(defn objective-function
  [a]
  (math/expt a 2))

; changing fitness to be a function from R^n -> R rather than R^n -> R^n
(defn set-fitness
  [bee]
  (reduce + (map objective-function bee)))

(defn random-in-range
  [[a b]]
  (+ a (rand (- b a))))

(defn truncate
  [search-space bee]
  (let [max-space (map (fn [x] (apply max x)) search-space)]
  (let [min-space (map (fn [x] (apply min x)) search-space)]
  (let [truncate-max (map min max-space bee)]
    (map max min-space truncate-max)))))

(defn random-bee
  [search-space]
  (map random-in-range search-space))

(defn killer-bee
  [search-space dancing-bee radius]
  (let [dimension (count search-space)]
  (let [unrestricted-bee (map + dancing-bee (repeat dimension (random-in-range [(- radius), radius])))]
    (truncate search-space unrestricted-bee))))

(defn create-swarm
  [num-bees bee-creator & bee-args]
  (map (fn [x] (apply bee-creator x)) (repeat num-bees bee-args)))

(defn uniform-partition 
  [num-fields num-bees]
  (let [bees-per-field (int (/ num-bees num-fields))] 
  (let [tail-distribution (repeat (- num-fields 1) bees-per-field)]
    (cons (- num-bees (reduce + tail-distribution)) tail-distribution))))

; { (x, f(x) | x in swarm)}, f(x) is in R^1 and x is in R^n, sorted from greatest to least by f(x)
(defn evaluate-swarm
  [swarm]
  (let [honey-collected (map set-fitness swarm)]
  (let [swarm-with-honey (map vector swarm honey-collected)] ; zip swarm with honey
    (sort-by last > swarm-with-honey))))

; uniformly disperse bees across search-space
(defn deploy-random-bees
  [search-space num-bees]
  (create-swarm num-bees random-bee search-space))

; partition killer bees into (count dancing-bees) distinct neighborhoods
(defn deploy-killer-bees
  [search-space num-bees partitioner dancing-bees search-radius ]
  (let [bee-distribution (partitioner (count dancing-bees) num-bees)]
  (let [bee-creator (repeat (count dancing-bees) killer-bee)]
  (let [space-vector (repeat (count dancing-bees) search-space)]
  (let [radius-vector (repeat (count dancing-bees) search-radius)]
  (let [bee-args (map vector bee-distribution bee-creator space-vector dancing-bees radius-vector)]
    (map (fn [x] (apply create-swarm x)) bee-args)))))))

(defn refine-search
  [search-space num-dancing-bees num-killer-bees search-radius results]
  (let [dancing-bees (map first (take num-dancing-bees results))]
  (let [killer-swarm (deploy-killer-bees search-space num-killer-bees uniform-partition dancing-bees search-radius)]
  (let [random-swarm (deploy-random-bees search-space (- (count results) num-killer-bees))]
  (let [unified-swarm (apply concat (cons random-swarm killer-swarm))]
    (evaluate-swarm unified-swarm))))))

; the first bee in the swarm
(defn queen-bee
  [bees-with-honey]
  (first (first bees-with-honey)))

(defn better-bee
  [bee-1 bee-2]
  (queen-bee (evaluate-swarm (concat [bee-1] [bee-2]))))

(defn search
  ; should probably use named arguments
  [total-gen search-space num-dancing-bees num-killer-bees num-total-bees decay search-radius & [current-gen queen results]]
  (if (nil? current-gen)
    (let [random-results (evaluate-swarm (deploy-random-bees search-space num-total-bees))]
      (let [first-queen (queen-bee random-results)]
      (search total-gen search-space num-dancing-bees num-killer-bees num-total-bees decay search-radius 1 first-queen random-results)))
    (if (< current-gen total-gen)
      (let [refined-results (refine-search search-space num-dancing-bees num-killer-bees search-radius results)]
        (let [killer-queen (better-bee queen (queen-bee refined-results))]
        (search total-gen search-space num-dancing-bees num-killer-bees num-total-bees decay (* decay search-radius) (+ 1 current-gen) killer-queen refined-results)))
      queen)))

(defn -main
  [& args]
  (let [optimal-solution (search 10 (repeat 1 [-5 5]) 5 10 20 0.95 1)]
  (let [optimal-value (set-fitness optimal-solution)]
    (println optimal-solution)
    (println optimal-value))))