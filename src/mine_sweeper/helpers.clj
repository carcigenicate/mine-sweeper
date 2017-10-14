(ns mine-sweeper.helpers)

(defn index-of [x y width]
  (+ (* y width) x))

(defn grid-coords [width height]
  (for [y (range height)
        x (range width)]
    [x y]))