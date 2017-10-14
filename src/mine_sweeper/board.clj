(ns mine-sweeper.board
  (:require [mine-sweeper.tile :as t]
            [mine-sweeper.helpers :as mh]
            [clojure.string :as s]))

(defrecord Board [dimensions tiles])

(defn- index-of [board x y]
  (mh/index-of x y
     (-> board :dimensions first)))

(defn inbounds? [board x y]
  (let [[w h] (:dimensions board)]
    (and (< -1 x w)
         (< -1 y h))))

(defn new-board [width height initial-board-contents]
  (->Board [width height]
           (vec (repeat (* width height) initial-board-contents))))

(defn set-tile [board x y contents]
  (assoc-in board [:tiles (index-of board x y)] contents))

(defn get-tile [board x y]
  (get-in board [:tiles (index-of board x y)]))

(defn update-tile [board x y f]
  (set-tile board x y
    (f (get-tile board x y))))

(defn pretty-format [board]
  (let [[w] (:dimensions board)
        tiles (:tiles board)]
    (s/join "\n"
       (map #(s/join " " %) (partition w tiles)))))

(defn pprint [board]
  (println (pretty-format board)))