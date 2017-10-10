(ns mine-sweeper.gui.board
  (:require [seesaw.core :as sc]))

(def board-side-length 30)

(defn tile-button [data & args]
  (let [b (apply sc/button
                   :user-data data
                 args)]

    b))

(defn coord-buttons [width height & args]
  (for [y (range height)
        x (range width)]
    (apply tile-button [x y] args)))

(defn board-panel []
  (sc/border-panel
    :north (sc/flow-panel :)
    :center (sc/grid-panel :items (coord-buttons board-side-length board-side-length)
                           :rows board-side-length)))