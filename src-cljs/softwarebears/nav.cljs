(ns softwarebears.nav
  (:require [clojure.string :refer [blank?]]
            [softwarebears.classic :as classes]))

(def class-name "expanded")

(defn nav-element []
  (.querySelector js/document "body > nav"))

(defn show [element]
  (classes/add! element class-name))

(defn hide [element]
  (classes/remove! element class-name))

(defn ^:export toggle []
  (let [element (nav-element)]
    (if (classes/has-class? element class-name)
      (hide element)
      (show element)))
  false)