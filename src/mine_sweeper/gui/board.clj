(ns mine-sweeper.gui.board
  (:require [seesaw.core :as sc]
            [seesaw.dev :as sd]
            [mine-sweeper.game-board :as gb]
            [mine-sweeper.helpers :as mh]
            [mine-sweeper.tile :as t]
            [mine-sweeper.board :as b]))


; TODO: Use a single click handler for every tile button. Each tile contains its own coord, which the handler can read.
; The problem with the current code is after a button click, all the buttons are remade, and the handler is dropped.
; After remaking the buttons, give sc/listen the buttons and reset the handler.
; Too expensive? Find a way to modify the individual tiles as needed?

(def board-side-length 30)

; TODO: "data" necessary?
(defn tile-button [data & args]
  (let [b (apply sc/button
                   :user-data data
                 args)]

    b))

(defn update-grid! [grid-panel f]
  (sc/config! grid-panel
     :items (f (sc/config grid-panel :items))))

(defn tile-representation [tile]
  (cond
    (not (:uncovered? tile))
    (tile-button tile)

    (t/contains-hint? tile)
    (sc/label :text (::t/hint (:contents tile)))

    :else
    (tile-button tile)))

(defn redraw-button-grid [grid-panel board]
  (let [[w h] (:dimensions board)
        i-of #(mh/index-of % %2 w)]

    (update-grid! grid-panel
      (fn [items]
        (reduce (fn [acc-items [x y]]
                  (assoc acc-items (i-of x y)
                         (tile-representation (b/get-tile board x y))))
                (vec items)
                (mh/grid-coords w h))))))

(defn tile-click-handler [grid-panel board x y]
  (when-let [uncovered-board (gb/uncover-tile? board x y)]
    (redraw-button-grid grid-panel uncovered-board)
    (b/pprint uncovered-board)
    uncovered-board))

(defn coord-buttons
  "handler should be a function recieving [board x y], and returning a modified board."
  [width height board-atom handler & args]
  (map
    (fn [[x y]]
      (apply tile-button [x y]
               :listen [:action
                        (fn [_]
                          (swap! board-atom #(handler % x y)))]
             args))
    (mh/grid-coords width height)))

(defn base-board-panel [board-atom]
  (let [grid-panel (sc/grid-panel :items [], :rows board-side-length)
        grid-buttons (coord-buttons board-side-length board-side-length board-atom
                       (partial tile-click-handler grid-panel))]

    (sc/config! grid-panel :items grid-buttons)

    (sc/border-panel
      :north (sc/flow-panel :items [])
      :center grid-panel)))

(defn new-board-panel [board]
  (let [board-atom (atom board)
        board-panel (base-board-panel board-atom)]
    board-panel))
