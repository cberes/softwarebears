(ns softwarebears.routes.error
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [softwarebears.views.layout :as layout]))

(defn not-found []
  (layout/common
    [:div.intro.headline.full.trnp.plax.plax1
      [:p "Not found"]]))

(defroutes error-routes
  (route/not-found (not-found)))
