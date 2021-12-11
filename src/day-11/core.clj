(ns day_11.core)

(defn sum [sequence] (reduce + sequence))

(defn slurp-strings
  [filename]
  (as-> (slurp (clojure.java.io/resource filename)) $
        (clojure.string/split $ #"\n")))

(defn is-in? [coll item] (some #{item} coll))

(defn ch->int [x] (Character/digit x 10))
(defn int->ch [x] (Character/forDigit x 10))

"Parsing"
(def coords (for [x (range 10) y (range 10)] [x y]))
(defn parse-level [lines p] (ch->int (get-in lines p)))

(defn parse []
  (let [lines (slurp-strings "day11.txt")]
    (into {} (mapv (fn [p] [p (parse-level lines p)]) coords))))

"For debugging"
(defn map->str [m] (mapv (partial apply str) (partition 10 (mapv int->ch (mapv (parse) coords)))))

"Helpers"
(defn inside? [[x y]] (and (>= x 0) (< x 10) (>= y 0) (< y 10)))

(defn grid-neighbors [p]
  (->> [[0 -1] [0 1] [-1 0] [1 0] [-1 -1] [-1 1] [1 -1] [1 1]]
       (mapv #(mapv + p %))
       (filterv inside?)))

(defn zero-level [m p] (assoc m p 0))
(defn inc-level [m p] (update m p inc))
(defn inc-neighbors [m p] (reduce inc-level m (grid-neighbors p)))
(defn inc-levels [m] (reduce inc-level m coords))
(defn flashes [m] (filterv #(> (m %) 9) coords))
(defn flash-to-zero [m] (reduce zero-level m (flashes m)))

"The meat"
(defn sim [args]
  (let [m (:map args)
        inced (inc-levels m)]
    (loop [visited []
           result inced
           flashes-count 0]
      (let [flash (first (filterv #(not (is-in? visited %)) (flashes result)))]
        (if (some? flash)
          (let [visited' (conj visited flash)
                result' (inc-neighbors result flash)]
            (recur visited' result' (inc flashes-count)))
          {:map (flash-to-zero result), :flashes flashes-count})))))

(def iterations (iterate sim {:map (parse) :flashes 0}))

"Easy"
(println (sum (mapv :flashes (take 101 iterations))))

"Hard"
(println (count (take-while #(not= (:flashes %) 100) iterations)))
