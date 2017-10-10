(ns mine-sweeper.game-board
  (:require [mine-sweeper.board :as b]
            [mine-sweeper.tiles :as t]
            [mine-sweeper.flood-fill :as ff]))

(defn new-game-board [width height]
  (b/new-board width height ::t/covered))

(defn draw-hor-divider [board x divider-contents]
  (reduce #(b/set-cell % x %2 divider-contents)
          board
          (-> board :dimensions (second) (range))))

(defn draw-vert-divider [board y divider-contents]
  (reduce #(b/set-cell % %2 y divider-contents)
          board
          (-> board :dimensions (first) (range))))

(def divided-test-board
  (-> (b/new-board 7 3 :-)
      (draw-hor-divider 2 :#)))

(def bigger-divided-test-board
  (-> (b/new-board 7 7 :-)
      (draw-hor-divider 2 :#)
      (draw-vert-divider 4 :#)))
