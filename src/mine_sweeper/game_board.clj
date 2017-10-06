(ns mine-sweeper.game-board
  (:require [mine-sweeper.board :as b]
            [mine-sweeper.tiles :as t]))

(defn new-game-board [width height]
  (b/new-board width height ::t/covered))

