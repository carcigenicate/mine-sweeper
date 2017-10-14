(ns mine-sweeper.gui.event-test
  (:require [seesaw.core :as sc]
            [seesaw.dev :as sd]))

(defn grid-panel [w]
  (let [l #(sc/button :text (str %))]
    (sc/grid-panel :columns w
                   :items (for [n (range (* w w))] (l n)))))

(defn test-frame [width]
  (let [p (grid-panel width)
        f (sc/frame :content p
                    :size [1000 :by 1000])]

    (sc/listen p
       :mouse
       (fn [e]
         (clojure.pprint/pprint e)))

    f))


