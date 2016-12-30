(ns softwarebears.routes.blog
  (:require [clojure.java.io :as io]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [hiccup.element :refer [image link-to]]
            [softwarebears.util.markdown :as md]
            [softwarebears.views.layout :as layout]))

(def blog-path (System/getenv "SB_BLOG"))

(def markdown-file?
  (every-pred #(not (.isDirectory %))
              #(not= (first (.getName %)) \.)
              #(.endsWith (.getName %) ".md")))

(defn- get-file [item-name]
  (io/file (str blog-path "/" item-name ".md")))

(defn- blog-item [markup]
  (layout/common
    [:div.intro.full.black
      [:p.middle [:span.name [:span "Software"] [:span "Bears"] ".com"]]]
    [:main.blog
      [:section#blog-item.white markup]]))

(defn- get-blog-items []
  (->> blog-path
    (io/file)
    (.listFiles)
    (seq)
    (filter markdown-file?)
    (sort)
    (reverse)))

(defn- trim-end [s suffix]
  (subs s 0 (- (count s) (count suffix))))

(defn- read-more-link [f]
  (let [file-name (.getName f)]
    (link-to (str "/blog/" (trim-end file-name ".md")) "Read more &gt;&gt;&gt;")))

(defn- preview-single-blog-item [f & [prefix]]
  [(md/preview f prefix) [:h4 (read-more-link f)]])

(defn latest-blog-item []
  ; hiccup needs this function to return a seq (not a vector)
  (seq (preview-single-blog-item (first (get-blog-items)) "#")))

(defn- preview-blog-item [f & [prefix]]
  [(into [:section.white] (preview-single-blog-item f prefix))
   [:div.divider.trnp.plax.plax4]])

(defn blog []
  (layout/common
    [:div.intro.headline.full.trnp.plax.plax5
      [:p "Writing about technology from an engineer's point of view."]]
    [:main.blog
      (->> (get-blog-items)
        (map preview-blog-item)
        (reduce concat) ; reduce to a single sequence
        (drop-last))])) ; drop the last element, which is a divider

(defroutes blog-routes
  (GET "/blog/:f" [f] (blog-item (md/render (get-file f))))
  (GET "/blog" [] (blog))
  (route/files "/" {:root blog-path}))
