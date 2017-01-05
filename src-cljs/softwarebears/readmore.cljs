(ns softwarebears.readmore)

(defn parent [elem n]
  (if (<= n 0)
    elem
    (recur (.-parentNode elem) (dec n))))

(defn set-display [elem display]
  (-> elem
    .-style
    .-display
    (set! display)))

(defn toggle [evt css-class parent-levels]
  (-> evt
    .-currentTarget
    (parent parent-levels)
    (.querySelector css-class)
    (set-display "block"))
  (-> evt
    .-currentTarget
    (parent (dec parent-levels))
    (set-display "none")))

(defn read-more [evt]
  (toggle evt ".more" 2)
  (.preventDefault evt))

(defn show-less [evt]
  (toggle evt ".less" 3))

(.addEventListener
  js/document
  "DOMContentLoaded"
  (fn [_]
    (.addEventListener (.querySelector js/document "#alsi-more h4 a") "click" show-less)
    (.addEventListener (.querySelector js/document "h4#alsi-less a") "click" read-more)
    (.addEventListener (.querySelector js/document "#siol-more h4 a") "click" show-less)
    (.addEventListener (.querySelector js/document "h4#siol-less a") "click" read-more)
    (.addEventListener (.querySelector js/document "#fs-more h4 a") "click" show-less)
    (.addEventListener (.querySelector js/document "h4#fs-less a") "click" read-more)))
