(ns mine-sweeper.flood-fill
  (:require [mine-sweeper.board :as b]
            [mine-sweeper.tiles :as t]))

(defn floodable-coords-surrounding [board center-x center-y]
  (let [[w h] (:dimensions board)]
    (for [y (range (dec center-y) (+ center-y 2))
          x (range (dec center-x) (+ center-x 2))
          :when (and (not= [x y] [center-x center-y])
                     (b/inbounds? board x y))]
      [x y])))

(defn remove-terminating-coords [board visited pred coords]
  (remove (fn [[x y :as p]]
            (or (visited p)
                (not (pred (b/get-cell board x y)))))

          coords))
#_
(defn flood-fill-while [board x y fill-with pred]
  (loop [acc-board board
         frontier [[x y]]
         visited #{}]

    (println "Popped:" (first frontier))
    (println "V:" visited)
    (println "FSe:" (into #{} frontier) "\n")

    (if-not (empty? frontier)
      (let [[[px py :as popped] & rest-frontier] frontier
            updated-visited (conj visited popped)
            frontier-additions (floodable-coords-surrounding acc-board px py)

            ; FIXME: Dangerously lazy?
            new-frontier (vec
                           (remove-terminating-coords updated-visited pred
                             (into frontier-additions rest-frontier)))]

        (recur
          (if (and (pred (b/get-cell acc-board px py)) (not (visited popped)))
            (b/set-cell acc-board px py fill-with)
            acc-board)

          new-frontier
          (conj visited popped)))

      acc-board)))

(defn flood-fill-while [board fill-x fill-y fill-contents pred]
  (let [initial-fill [fill-x fill-y]]
    (loop [frontier [initial-fill]
           flooded #{}
           acc-board board]

      (if-not (empty? frontier)
        (let [[[x y :as current-coord] & rest-frontier] frontier
              inbound-neighbors (floodable-coords-surrounding acc-board x y)

              self-flooded (conj flooded current-coord)

              updated-frontier  (remove-terminating-coords acc-board
                                                           self-flooded
                                                           pred
                                                           (into rest-frontier
                                                                 inbound-neighbors))]

          (recur
            updated-frontier
            self-flooded
            (if (pred (b/get-cell acc-board x y))
              (b/set-cell acc-board x y fill-contents)
              acc-board)))

        acc-board))))











