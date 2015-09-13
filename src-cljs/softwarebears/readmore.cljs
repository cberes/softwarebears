(ns softwarebears.readmore)

(defn ^:export read-more [element, id-more]
  (-> (.getElementById js/document id-more) .-style .-display (set! "block"))
  (-> (.-parentNode element) .-style .-display (set! "none"))
  false)

(defn ^:export show-less [id-more, id-less]
  (-> (.getElementById js/document id-more) .-style .-display (set! "none"))
  (-> (.getElementById js/document id-less) .-style .-display (set! "block"))
  false)