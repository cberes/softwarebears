(ns softwarebears.routes.blog
  (:require [clojure.java.io :as io]
            [compojure.core :refer :all]
            [hiccup.element :refer [image link-to]]
            [softwarebears.util.markdown :as md]
            [softwarebears.views.layout :as layout]))

(defn- get-file [item-name]
  (io/file (io/resource (str "blog/" item-name ".md"))))

(defn blog-item [markup]
  (layout/common
    [:div.intro.full.black
      [:p.middle [:span.name [:span "Software"] [:span "Bears"] ".com"]]]
    [:main.blog
      [:section#blog-item.white markup]]))

(defn get-blog-items []
  (remove #(.isDirectory %) (file-seq (io/file (io/resource "blog")))))

(defn read-more-link [f]
  (let [n (.getName f)]
    (link-to (str "/blog/" (subs n 0 (- (count n) 3))) "Read more &gt;&gt;&gt;")))

(defn blog []
  (layout/common
    [:div.intro.headline.full.trnp.plax.plax1
      [:p "Welcome to my sweet blog."]]
    [:main.blog
      ; the map call returns a lazy sequence of sequences of elements
      ; so reduce to a single sequence with concat
      ; then drop the final element, which will be a divider
      (drop-last
        (reduce
          concat
          (map #(seq [[:section.white (md/preview %) [:h4 (read-more-link %)]] [:div.divider.trnp.plax.plax2]]) (get-blog-items))))]))

(defn latest-blog-item []
  (let [f (first (get-blog-items))]
    (seq [(md/preview f "#") [:h4 (read-more-link f)]])))

(defroutes blog-routes
  (GET "/blog/:f" [f] (blog-item (md/render (get-file f))))
  (GET "/blog" [] (blog)))
