(ns softwarebears.routes.contact
  (:require [compojure.core :refer :all]
            [hiccup.element :refer [image link-to mail-to]]
            [hiccup.form :refer [form-to label email-field text-area text-field file-upload hidden-field check-box submit-button]]
            [noir.validation :refer [rule errors? has-value? is-email? matches-regex? not-nil? on-error]]
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
        [:p "Please use this form to introduce yourself and to tell us about your project. You can include any additional contact info in the message. Then we'll get back to you shortly."]
        (form-to {:class "simple"} [:post (str "https://formspree.io/" to-email)]
          (hidden-field "_subject" "softwarebears.com contact")
          (text-field {:style "display:none;"} :_gotcha)
          [:table
            ; First and last name
            [:tr
              [:th (label :fname "First name")]
              [:th (label :lname "Last name")]]
            [:tr
              [:td (on-error :fname first)]
              [:td (on-error :lname first)]]
            [:tr
              [:td (text-field {:maxlength 100} :fname)]
              [:td (text-field {:maxlength 100} :lname)]]]
          ; email address
          (label :email "Email address")
          [:div.error (on-error :email first)]
          (email-field {:maxlength 250} :email)
          ; email message)
          (label :message "Tell us about your project")
          [:div.error (on-error :message first)]
          (text-area {:maxlength 2000 :rows 10} :message)
          ; submit button
          (submit-button "Send"))]]))

(defroutes contact-routes
  (GET "/contact" [] (contact)))
