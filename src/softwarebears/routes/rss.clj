(ns softwarebears.routes.rss
  (:import [java.text SimpleDateFormat])
  (:require [clojure.java.io :as io]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [clj-rss.core :as rss]
            [softwarebears.views.layout :refer [base-url description title]]
            [softwarebears.routes.blog :as blog]
            [softwarebears.util.markdown :as md]))

(def endpoint "/rss.xml")

(defn- parse-published-time [s]
  (.parse (SimpleDateFormat. "yyyy-MM-dd'T'HH:mm:ssX") s))

(defn- build-item [f]
  (let [{title :title timestamp :timestamp} (md/metadata f)
        preview (md/preview f)]
    {:title title
     :link (str base-url (blog/get-endpoint f))
     :guid [{:isPermaLink false} (blog/get-id f)]
     :pubDate (parse-published-time timestamp)
     :description (str "<![CDATA[ " preview " ]]>")}))

(defn- build-channel [items]
  (rss/channel-xml {:title title
                    :link (str base-url endpoint)
                    :lastBuildDate (:pubDate (first items))
                    :description description} items))

(defn rss-xml []
  (->> (blog/get-blog-items)
    (map build-item)
    (build-channel)))

(defroutes rss-routes
  (GET endpoint [] (rss-xml)))

