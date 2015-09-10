(ns softwarebears.util.markdown
  (:require [clojure.java.io :as io]
            [markdown.core :refer [md-to-html-string]]))

(defn- read-file [f preview?]
  (clojure.string/replace (slurp f) (if preview? #"(?s)%%read_more%%.*$" "%%read_more%%") ""))

(defn render [f]
  (md-to-html-string (read-file f false)))

(defn preview [f]
  (md-to-html-string (read-file f true)))
