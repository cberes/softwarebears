(ns softwarebears.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [softwarebears.routes.home :refer [home-routes]]
            [softwarebears.routes.portfolio :refer [portfolio-routes]]
            [softwarebears.routes.blog :refer [blog-routes]]
            [softwarebears.routes.error :refer [error-routes]]))

(defroutes app-routes
  (route/resources "/"))

(def app
  (wrap-defaults
    (routes
      home-routes
      portfolio-routes
      blog-routes
      app-routes
      error-routes)
    site-defaults))
