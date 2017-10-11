(ns mine-sweeper.tile)

(defrecord Tile [uncovered? flagged? contents]
  Object
  (toString [self] (str "+(" (if uncovered? "U " "")
                             (if flagged? "F " "")
                             contents
                        ")+")))

(defn new-tile [contents]
  (->Tile false false contents))

(defn new-hint [hint-n]
  {::hint hint-n})

(defn contains-hint? [tile]
  (contains? (:contents tile) ::hint))

(def tile-contents #{::bomb
                     ::hint
                     ::empty})
