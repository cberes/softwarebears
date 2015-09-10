(ns softwarebears.util.markdown
  (:require [clojure.java.io :as io]
            [markdown.core :refer [md-to-html-string]]))

(defn- read-file [f prefix preview?]
  (clojure.string/replace
    (str (or prefix "") (slurp f))
    (if preview? #"(?s)%%read_more%%.*$" "%%read_more%%") ""))

(defn render [f & [prefix]]
  (md-to-html-string (read-file f prefix false)))

(defn preview [f & [prefix]]
  (md-to-html-string (read-file f prefix true)))
