(ns softwarebears.classic
  (:require [clojure.string :as string]))

(defn class-pattern [class-name]
  (re-pattern (str "(?i)\\s*\\b" class-name "\\b")))

(defn has-class? [e class-name]
  (re-matches (class-pattern class-name) (.-className e)))

(defn add! [e class-name]
  (when-not (has-class? e class-name)
    (set! (.-className e) (str (.-className e) " " class-name))))

(defn remove! [e class-name]
  (set! (.-className e) (string/replace (.-className e) (class-pattern class-name) "")))