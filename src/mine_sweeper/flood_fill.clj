(ns mine-sweeper.flood-fill
  (:require [mine-sweeper.board :as b]
            [mine-sweeper.tiles :as t]))

(defn coords-surrounding [board center-x center-y]
  (let [[w h] (:dimensions board)]
    (for [y (range (dec center-y) (+ center-y 2))
          x (range (dec center-x) (+ center-x 2))
          :when (and (not= [x y] [center-x center-y])
                     (b/inbounds? board x y))]
      [x y])))

(defn flood-fill-while [board x y fill-with pred]
  (loop [acc-board board
         frontier [[x y]]
         visited #{[x y]}]

    (if-not (empty? frontier)
      (let [[[px py :as popped] & rest-frontier] frontier
            frontier-additions (coords-surrounding acc-board px py)

            ; FIXME: Dangerously lazy?
            new-frontier (remove visited (concat rest-frontier frontier-additions))]

        (recur
          (if (and (pred (b/get-cell board px py)) (not (visited popped)))
            (b/set-cell acc-board px py fill-with)
            acc-board)

          new-frontier
          (conj visited popped)))

      board)))




