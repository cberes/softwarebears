(ns softwarebears.util.markdown
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [markdown.core :refer [md-to-html-string]]))

(defn- parse-meta [s]
  (let [[k v] (string/split s #"=" 2)]
    {(keyword k) v}))

(defn- remove-metadata [s]
  (string/replace s #"(?s)%%begin-meta%%.*%%end-meta%%" ""))

(defn- get-preview-or-whole [preview? s]
  (string/replace s (if preview? #"(?s)%%read-more%%.*$" "%%read-more%%") ""))

(defn- read-file [f prefix preview?]
  (->> f
    (slurp)
    (str (or prefix ""))
    (remove-metadata)
    (get-preview-or-whole preview?)))

(defn render [f & [prefix]]
  (md-to-html-string (read-file f prefix false)))

(defn preview [f & [prefix]]
  (md-to-html-string (read-file f prefix true)))

(defn metadata [f]
  (->> f
    (slurp)
    (re-find #"(?s)%%begin-meta%%(.*)%%end-meta%%")
    (second)
    (string/split-lines)
    (map parse-meta)
    (apply merge)))

