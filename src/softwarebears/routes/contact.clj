(ns softwarebears.routes.contact
  (:require [compojure.core :refer :all]
            [hiccup.element :refer [image link-to mail-to]]
            [hiccup.form :refer [form-to label email-field text-area text-field hidden-field submit-button]]
            [softwarebears.views.layout :as layout]))

(defn contact []
  (layout/common
    [:div.intro.headline.full.trnp.plax.plax6
      [:p "Here's how to contact me."]]
    [:main
      [:section.colory.white
        [:h2 "Contact"]
        [:h3 "How to reach me"]
        [:p
          "You can find me on "
          (link-to "https://www.linkedin.com/in/coreyberes/" "LinkedIn")
          ". If I don't respond to you, don't take it personally; I don't login to LinkedIn often."]
        [:p
          "If you have questions about this website or my other software projects, you can create an issue on "
          (link-to "https://github.com/cberes" "GitHub")
          "."]
       ]
     ]
   )
  )

(defroutes contact-routes
  (GET "/contact" [] (contact)))
