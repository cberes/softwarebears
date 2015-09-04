(ns softwarebears.util.markdown
  (:require [clojure.java.io :as io]
            [markdown.core :refer [md-to-html-string]]))

(defn- read-file [f preview?]
  (clojure.string/replace (slurp f) (if preview? #"%%read_more%%.*$" "%%read_more%%") ""))

(defn- get-file [item-name]
  (io/file (io/resource (str "blog/" item-name ".md"))))

(defn render [item-name]
  (md-to-html-string (read-file (get-file item-name) false)))

(defn preview [item-name]
  (md-to-html-string (read-file (get-file item-name) true)))
