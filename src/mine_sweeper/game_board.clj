(ns mine-sweeper.game-board
  (:require [mine-sweeper.board :as b]
            [mine-sweeper.tile :as t]
            [mine-sweeper.flood-fill :as ff]))

; TODO: Need to store hint information somewhere? Calc as needed?
; TODO: Test ->>>>

(defn new-game-board [width height]
  (b/new-board width height (t/new-tile ::t/empty)))

; ---------- Flagging ----------

(defn unflag-tile [board x y]
  (b/update-tile board x y
    #(assoc % :flagged? false)))

(defn flag-tile [board x y]
  (b/update-tile board x y
    #(assoc % :flagged? true)))

(defn toggle-flag-tile [board x y]
  (b/update-tile board x y
    #(update % :flagged? not)))

; ---------- Misc ----------

(defn uncover-tile? [board x y]
  (let [{:keys [flagged? uncovered? contents] :as tile} (b/get-tile board x y)
        uncover-tile #(assoc % :uncovered? true)]

    (cond
      uncovered?
      board

      (= contents ::t/bomb)
      nil

      :else
      (-> board
          (unflag-tile x y)
          (b/update-tile x y uncover-tile)))))
#_
(defn flood-fill-empty-tiles-at [board x y]
  ; FIXME: DA HUR DUR. We can't set a filler, since it requires modifying the uncovered? field.
  ; Also accept a function that modifies the board square? Fk.
  (ff/flood-fill-while board x y))

; ---------- Tests ----------

(defn draw-hor-divider [board x divider-contents]
  (reduce #(b/set-tile % x %2 (t/new-tile divider-contents))
          board
          (-> board :dimensions (second) (range))))

(defn draw-vert-divider [board y divider-contents]
  (reduce #(b/set-tile % %2 y (t/new-tile divider-contents))
          board
          (-> board :dimensions (first) (range))))

(def divided-test-board
  (-> (new-game-board 7 3)
      (draw-hor-divider 2 :#)))

(def bigger-divided-test-board
  (-> (b/new-board 25 25 :-)
      (draw-hor-divider 13 :#)
      (draw-vert-divider 13 :#)))

