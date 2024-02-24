(ns softwarebears.routes.blog
  (:import [java.time.format DateTimeFormatter])
  (:require [clojure.java.io :as io]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [hiccup.element :refer [image link-to]]
            [softwarebears.util.markdown :as md]
            [softwarebears.views.layout :as layout]))

(def blog-path (System/getenv "SB_BLOG"))

(def date-time-formatter (DateTimeFormatter/ofPattern "EEE, MMM d, uuuu"))

(def markdown-file?
  (every-pred #(not (.isDirectory %))
              #(not= (first (.getName %)) \.)
              #(.endsWith (.getName %) ".md")))

(defn- parse-to-date-time [s]
  (.parse DateTimeFormatter/ISO_DATE_TIME s))

(defn- format-date-time [t]
  (.format date-time-formatter t))

(defn- format-published-time [s]
  (-> s
    (parse-to-date-time)
    (format-date-time)))

(defn- draft? [f]
  (:draft (md/metadata f)))

(defn- get-file [item-name]
  (io/file (str blog-path "/" item-name ".md")))

(defn- blog-item [metadata markup]
  (layout/common
    [:div.intro.full.black
      [:p.middle [:span.name [:span "c"] [:span "beres"] ".com"]]]
    [:main.blog
      [:section#blog-item.white
        [:h2 (:title metadata)]
        [:p.metadata
          "By "
          [:span.author (:author metadata)]
          " on "
          [:span.time (format-published-time (:timestamp metadata))]]
        markup]]))

(defn get-blog-items []
  (->> blog-path
    (io/file)
    (.listFiles)
    (seq)
    (filter markdown-file?)
    (remove draft?)
    (sort)
    (reverse)))

(defn- trim-end [s suffix]
  (subs s 0 (- (count s) (count suffix))))

(defn- get-item-id [file-name]
  (trim-end file-name ".md"))

(defn get-id [f]
  (->> f
    (.getName)
    (get-item-id)))

(defn get-endpoint [f]
  (str "/blog/" (get-id f)))

(defn- read-more-link [f]
  (link-to (get-endpoint f) "Read more &gt;&gt;&gt;"))

(defn latest-blog-item []
  ; hiccup needs this function to return a seq (not a vector)
  (let [f (first (get-blog-items))
        metadata (md/metadata f)]
    (seq [[:h3 (:title metadata)]
          (md/preview f)
          [:h4 (read-more-link f)]])))

(defn- preview-blog-item [f]
  (let [metadata (md/metadata f)]
    [:section.white
      [:h2 (:title metadata)]
      (md/preview f)
      [:h4 (read-more-link f)]]))

(defn blog []
  (layout/common
    [:div.intro.headline.full.trnp.plax.plax5
      [:p "Writing about technology from an engineer's point of view."]]
    [:main.blog
      (->> (get-blog-items)
        (map preview-blog-item)
        (interpose [:div.divider.trnp.plax.plax4]))]))

(defn render-blog-item [id]
  (let [f (get-file id)]
    (blog-item (md/metadata f) (md/render f))))

(defn get-blog-item-ids []
  (->> (get-blog-items)
    (map #(.getName %))
    (map get-item-id)))

(defroutes blog-routes
  (GET "/blog/:f" [f] (render-blog-item f))
  (GET "/blog" [] (blog))
  (route/files "/" {:root blog-path}))
