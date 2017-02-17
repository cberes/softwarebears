(ns softwarebears.generator
  (:gen-class)
  (:require [clojure.java.io :as io]
            [softwarebears.routes.home :refer [home]]
            [softwarebears.routes.portfolio :refer [portfolio]]
            [softwarebears.routes.blog :refer [blog render-blog-item get-blog-item-ids]]
            [softwarebears.routes.contact :refer [contact]]
            [softwarebears.routes.rss :refer [rss-xml]]
            [softwarebears.routes.error :refer [not-found]]))

(defn save-page [content & paths]
  (let [file (apply io/file paths)]
    (io/make-parents file)
    (spit file content)))

(defn save-blog-entries [root blog-root file-name]
  (doseq [id (get-blog-item-ids)]
    (save-page (render-blog-item id) root blog-root id file-name)))

(defn -main [& args]
  (let [root (first args)]
    (save-page (home) root "index.html")
    (save-page (not-found) root "404.html")
    (save-page (portfolio) root "portfolio" "index.html")
    (save-page (contact) root "contact" "index.html")
    (save-page (rss-xml) root "rss.xml")
    (save-page (blog) root "blog" "index.html")
    (save-blog-entries root "blog" "index.html")))

