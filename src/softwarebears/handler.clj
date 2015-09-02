(ns softwarebears.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [softwarebears.views.layout :as layout]))

(defroutes app-routes
  (route/resources "/")
  (route/not-found (layout/common [:p "Not Found"])))

(defroutes home-routes
  (GET "/" [] (layout/common [:div.intro.headline.full.trnp.plax.plax1 [:p "Hello World"]])))

(def app
  (wrap-defaults
    (routes
      home-routes
      app-routes)
    site-defaults))
