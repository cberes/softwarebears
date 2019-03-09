(ns softwarebears.routes.contact
  (:require [compojure.core :refer :all]
            [hiccup.element :refer [image link-to mail-to]]
            [hiccup.form :refer [form-to label email-field text-area text-field hidden-field submit-button]]
            [softwarebears.views.layout :as layout]))

(defn contact []
  (layout/common
    [:div.intro.headline.full.trnp.plax.plax6
      [:p "Let's collaborate on a project."]]
    [:main
      [:section.colory.white
        [:h2 "Contact"]
        [:h3 "Add me to your contacts"]
        [:table
          [:tr
            [:th "Email"] [:td (mail-to layout/email)]]
          [:tr
            [:th "Telephone"] [:td (image {:class "bottom"} "/img/dial-now-light.png")]]]
        [:h3 "Or send a message right now"]
        [:p "Please use this form to introduce yourself and to tell me about your project. You can include any additional contact info in the message. Your message will be sent via " (link-to "https://formspree.io/" "Formspree") ". Then I'll get back to you shortly."]
        (form-to {:class "simple"} [:post (str "https://formspree.io/" layout/email)]
          (hidden-field "_subject" "seabears.net contact")
          (text-field {:style "display:none;"} :_gotcha)
          [:table
            ; First and last name
            [:tr
              [:th (label :fname "First name")]
              [:th (label :lname "Last name")]]
            [:tr
              [:td (text-field {:maxlength 100} :fname)]
              [:td (text-field {:maxlength 100} :lname)]]]
          ; email address
          (label :email "Email address")
          (email-field {:maxlength 250} :email)
          ; email message)
          (label :message "Don't edit yourself, Hank")
          (text-area {:maxlength 2000 :rows 10} :message)
          ; submit button
          (submit-button "Send"))]]))

(defroutes contact-routes
  (GET "/contact" [] (contact)))
