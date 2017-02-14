(ns softwarebears.generator
  (:gen-class)
  (:require [clojure.java.io :as io]
            [softwarebears.routes.home :refer [home]]
            [softwarebears.routes.portfolio :refer [portfolio]]
            [softwarebears.routes.blog :refer [blog]]
            [softwarebears.routes.contact :refer [contact]]
            [softwarebears.routes.error :refer [not-found]]))

(defn save-page [f & paths]
  (let [file (apply io/file paths)]
    (io/make-parents file)
    (spit file (f))))

(defn -main [& args]
  (let [root (first args)]
    (save-page home root "index.html")
    (save-page not-found root "404.html")
    (save-page portfolio root "portfolio" "index.html")
    (save-page contact root "contact" "index.html")
    (save-page blog root "blog" "index.html")))

