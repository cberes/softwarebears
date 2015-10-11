(ns softwarebears.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [noir.validation :refer [wrap-noir-validation]]
            [softwarebears.routes.home :refer [home-routes]]
            [softwarebears.routes.portfolio :refer [portfolio-routes]]
            [softwarebears.routes.blog :refer [blog-routes]]
            [softwarebears.routes.contact :refer [contact-routes]]
            [softwarebears.routes.error :refer [error-routes]]))

(defroutes app-routes
  (route/resources "/"))

(def app
  (-> (routes
        home-routes
        portfolio-routes
        blog-routes
        contact-routes
        app-routes
        error-routes)
      (wrap-defaults site-defaults)
      (wrap-noir-validation)))
