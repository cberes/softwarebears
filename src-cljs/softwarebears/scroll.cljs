(ns softwarebears.scroll
  (:require [softwarebears.classic :as classes]))

(def detach-header-px 300)
(def detach-class-name "detached")

(defn current-scroll []
  (.-pageYOffset js/window))

(defn animate-header [evt]
  (if (>= (current-scroll) detach-header-px)
    (do
      (-> (.querySelector js/document "body > header") (classes/add! detach-class-name))
      (-> (.querySelector js/document "body > nav") (classes/add! detach-class-name)))
    (do
      (-> (.querySelector js/document "body > header") (classes/remove! detach-class-name))
      (-> (.querySelector js/document "body > nav") (classes/remove! detach-class-name)))))

(.addEventListener js/window "scroll" animate-header)