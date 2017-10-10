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
#_ ; Unnecessary? Too many parameters?
(defn remove-terminating-coords [board visited pred coords]
  (remove (fn [[x y :as p]]
            (or (visited p)
                (not (pred (b/get-cell board x y)))))

          coords))
; TODO: Breakup
(defn flood-fill-while [board fill-x fill-y fill-contents pred]
  (let [initial-fill [fill-x fill-y]
        pred' (fn [b [cx cy]] (pred (b/get-cell b cx cy)))]

    (if (pred' board initial-fill)
      (loop [frontier [initial-fill]
             flooded #{}
             acc-board board]

        (if-not (empty? frontier)
          (let [pred'' (partial pred' acc-board)
                [[x y :as current-coord] & rest-frontier] frontier

                inbound-neighbors (floodable-coords-surrounding acc-board x y)

                self-flooded (conj flooded current-coord)

                no-repeat-frontier (remove self-flooded (into rest-frontier inbound-neighbors))
                no-divider-frontier (filterv pred'' no-repeat-frontier)]

            (recur
              no-divider-frontier
              self-flooded
              (if (pred'' [x y])
                (b/set-cell acc-board x y fill-contents)
                acc-board)))

          acc-board))

      board)))











