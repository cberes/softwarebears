(ns softwarebears.scroll
  (:require [clojure.string :as string]))

(def detach-header-px 300)
(def detach-class-name "detached")

(defn current-scroll []
  (.-pageYOffset js/window))

(defn add-class! [e class-name]
  (let [class-list (.-className e)]
    (set! (.-className e) (str class-list " " class-name))))

(defn del-class! [e class-name]
  (let [class-list (.-className e) pattern (re-pattern (str "(?i)\\s*\\b" class-name "\\b"))]
    (set! (.-className e) (string/replace class-list pattern ""))))

(defn animate-header [evt]
  (if (>= (current-scroll) detach-header-px)
    (do
      (-> (.querySelector js/document "body > header") (add-class! detach-class-name))
      (-> (.querySelector js/document "body > nav") (add-class! detach-class-name)))
    (do
      (-> (.querySelector js/document "body > header") (del-class! detach-class-name))
      (-> (.querySelector js/document "body > nav") (del-class! detach-class-name)))))

(.addEventListener js/window "scroll" animate-header)