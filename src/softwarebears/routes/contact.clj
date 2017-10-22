(ns softwarebears.routes.contact
  (:require [compojure.core :refer :all]
            [hiccup.element :refer [link-to mail-to]]
            [hiccup.form :refer [form-to label email-field text-area text-field hidden-field submit-button]]
            [softwarebears.views.layout :as layout]))

(def phone "(716) 222 0088")

(def to-email "hello@softwarebears.com")

(defn contact []
  (layout/common
    [:div.intro.headline.full.trnp.plax.plax6
      [:p "We want to hear about your project."]]
    [:main
      [:section.colory.white
        [:h2 "Contact us"]
        [:h3 "Add us to your contacts"]
        [:table
          [:tr
            [:th "Email"] [:td (mail-to to-email)]]
          [:tr
            [:th "Telephone"] [:td phone]]]
        [:h3 "Or send a message right now"]
        [:p "Please use this form to introduce yourself and to tell us about your project. You can include any additional contact info in the message. Your message will be sent via " (link-to "https://formspree.io/" "Formspree") ". Then we'll get back to you shortly."]
        (form-to {:class "simple"} [:post (str "https://formspree.io/" to-email)]
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
          (label :message "Tell us about your project")
          (text-area {:maxlength 2000 :rows 10} :message)
          ; submit button
          (submit-button "Send"))]]))

(defroutes contact-routes
  (GET "/contact" [] (contact)))
