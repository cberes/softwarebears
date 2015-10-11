(ns softwarebears.routes.blog
  (:require [clojure.java.io :as io]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [hiccup.element :refer [image link-to]]
            [softwarebears.util.markdown :as md]
            [softwarebears.views.layout :as layout]))

(def blog-path (System/getenv "SB_BLOG"))

(defn- get-file [item-name]
  (io/file (str blog-path "/" item-name ".md")))

(defn blog-item [markup]
  (layout/common
    [:div.intro.full.black
      [:p.middle [:span.name [:span "Software"] [:span "Bears"] ".com"]]]
    [:main.blog
      [:section#blog-item.white markup]]))

(defn get-blog-items []
  (filter
    (apply every-pred [#(not (.isDirectory %)) #(not= (first (.getName %)) \.) #(.endsWith (.getName %) ".md")])
    (seq (.listFiles (io/file blog-path)))))

(defn read-more-link [f]
  (let [n (.getName f)]
    (link-to (str "/blog/" (subs n 0 (- (count n) 3))) "Read more &gt;&gt;&gt;")))

(defn blog []
  (layout/common
    [:div.intro.headline.full.trnp.plax.plax5
      [:p "Writing about technology from an engineer's point of view."]]
    [:main.blog
      ; the map call returns a lazy sequence of sequences of elements
      ; so reduce to a single sequence with concat
      ; then drop the final element, which will be a divider
      (drop-last
        (reduce
          concat
          (map #(seq [[:section.white (md/preview %) [:h4 (read-more-link %)]] [:div.divider.trnp.plax.plax4]]) (get-blog-items))))]))

(defn latest-blog-item []
  (let [f (first (get-blog-items))]
    (seq [(md/preview f "#") [:h4 (read-more-link f)]])))

(defroutes blog-routes
  (GET "/blog/:f" [f] (blog-item (md/render (get-file f))))
  (GET "/blog" [] (blog))
  (route/files "/" {:root blog-path}))
