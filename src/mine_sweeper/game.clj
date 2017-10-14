(ns mine-sweeper.game
  (:require [mine-sweeper.game-board :as gb]
            [mine-sweeper.flood-fill :as ff]))

; TODO: Add start-time field
(defrecord Game [board score])

(defn new-game [board-dimensions]
  (let [[w h] board-dimensions]
    (->Game (gb/new-game-board w h) 0)))

#_ ; TODO: Finish
(defn uncover-cell? [game x y]
  (let [b (:board game)]
    (when-let [uncovered-board (gb/uncover-tile? b x y)]
      (ff/flood-fill-while uncovered-board))))
