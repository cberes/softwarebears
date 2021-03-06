(ns softwarebears.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [softwarebears.routes.home :refer [home-routes]]
            [softwarebears.routes.portfolio :refer [portfolio-routes]]
            [softwarebears.routes.blog :refer [blog-routes]]
            [softwarebears.routes.contact :refer [contact-routes]]
            [softwarebears.routes.rss :refer [rss-routes]]
            [softwarebears.routes.error :refer [error-routes]]))

(defroutes app-routes
  (route/resources "/"))

(def app
  (-> (routes
        home-routes
        portfolio-routes
        blog-routes
        contact-routes
        rss-routes
        app-routes
        error-routes)
      (wrap-defaults site-defaults)))
