(ns softwarebears.readmore)

(defn read-more [evt]
  (-> evt
    .-currentTarget
    .-parentNode
    .-parentNode
    (.querySelector ".more")
    .-style
    .-display
    (set! "block"))
  (-> evt
    .-currentTarget
    .-parentNode
    .-style
    .-display
    (set! "none"))
  (.preventDefault evt))

(defn show-less [evt]
  (-> evt
    .-currentTarget
    .-parentNode
    .-parentNode
    .-style
    .-display
    (set! "none"))
  (-> evt
    .-currentTarget
    .-parentNode
    .-parentNode
    .-parentNode
    (.querySelector ".less")
    .-style
    .-display (set! "block")))

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
