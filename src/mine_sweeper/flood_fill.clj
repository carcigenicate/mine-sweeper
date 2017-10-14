(ns mine-sweeper.flood-fill
  (:require [mine-sweeper.board :as b]
            [mine-sweeper.tile :as t]))

(defn floodable-coords-surrounding [board center-x center-y]
  (let [[w h] (:dimensions board)]
    (for [y (range (dec center-y) (+ center-y 2))
          x (range (dec center-x) (+ center-x 2))
          :when (and (not= [x y] [center-x center-y])
                     (b/inbounds? board x y))]
      [x y])))

(defn- modify-tile-with? [board coord pred-map-f]
  (let [[x y] coord]
    (when-let [new-tile (pred-map-f (b/get-tile board x y))]
      (b/set-tile board x y new-tile))))

; TODO: Breakup
(defn flood-fill-while [board fill-x fill-y pred-map-f]
  (let [initial-fill [fill-x fill-y]
        modify? (fn [b c] (modify-tile-with? b c pred-map-f))]

    (if (modify? board initial-fill)
      (loop [frontier [initial-fill]
             flooded #{}
             acc-board board]

        (if-not (empty? frontier)
          (let [modify?' (partial modify? acc-board)
                [[x y :as current-coord] & rest-frontier] frontier

                inbound-neighbors (floodable-coords-surrounding acc-board x y)

                self-flooded (conj flooded current-coord)

                no-repeat-frontier (remove self-flooded (into rest-frontier inbound-neighbors))
                no-divider-frontier (filterv modify?' no-repeat-frontier)]

            (recur
              no-divider-frontier
              self-flooded
              (or (modify?' [x y]) acc-board)))

          acc-board))

      board)))











