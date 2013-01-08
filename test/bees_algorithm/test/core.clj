(ns bees-algorithm.test.core
  (:use [bees-algorithm.core])
  (:use [clojure.test]))
  
(deftest test-create-swarm-creates-bees-from-bee-creator
  (is (= (repeat 10 [-1]) 
         (create-swarm 10 (fn [x] [x]) -1))))

(deftest test-evaluate-swarm
  (is (= '([[5] 25] [[0] 0]) 
         (evaluate-swarm [[5] [0]]))))

(deftest test-queen-bee
  (is (= [5]) '([[5] 25] [[0] 0])))

(deftest test-better-bee
  (is (= [5]) (better-bee [5] [0])))

(deftest test-killer-bee-is-in-an-open-box-when-unrestricted-by-space
  ; create 2 vectors of length 100, assert [1 ...] > [bee1 bee2 ...]
  (is (every? true? 
    (map > 
      (repeat 100 1)
      (map first (create-swarm 100 killer-bee [[-2 2]] [0] 1)))))
  (is (every? true? 
    (map < 
      (repeat 100 -1)
      (map first (create-swarm 100 killer-bee [[-2 2]] [0] 1))))))

(deftest test-killer-bee-is-truncated-by-search-space
  (is (every? true? 
    (map >= 
      (repeat 100 1)
      (map first (create-swarm 100 killer-bee [[-1 1]] [0.95] 0.1)))))
  (is (every? true? 
    (map <= 
      (repeat 100 -1)
      (map first (create-swarm 100 killer-bee [[-1 1]] [-0.95] 0.1))))))

(deftest test-uniform-partition
  (is (= '(4 3 3 3 3 3 3 3 3 3)
         (uniform-partition 10 31))))


