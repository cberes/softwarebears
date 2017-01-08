(ns softwarebears.routes.contact
  (:require [clojure.string :refer [trim]]
            [clojure.java.io :as io]
            [compojure.core :refer :all]
            [ring.middleware.anti-forgery :refer [*anti-forgery-token*]]
            [hiccup.element :refer [image link-to mail-to]]
            [hiccup.form :refer [form-to label email-field text-area text-field file-upload hidden-field check-box submit-button]]
            [noir.validation :refer [rule errors? has-value? is-email? matches-regex? not-nil? on-error]]
            [softwarebears.views.layout :as layout]))

(def phone "(716) 222 0088")

(def to-email "hello@softwarebears.com")

(def formatter (java.time.format.DateTimeFormatter/ofPattern "yyyyMMddHHmmssSSS"))

(defn random-file-name [ext]
  (str (.format formatter (java.time.LocalDateTime/now)) "." ext))

(defn contact [& [result result-class fname lname email message]]
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
        (when result [:p {:class result-class} result])
        (form-to {:class "simple"} [:post "/contact"]
          (hidden-field "__anti-forgery-token" *anti-forgery-token*)
          [:table
            ; First and last name
            [:tr
              [:th (label :fname "First name")]
              [:th (label :lname "Last name")]]
            [:tr
              [:td (on-error :fname first)]
              [:td (on-error :lname first)]]
            [:tr
              [:td (text-field {:maxlength 100} :fname fname)]
              [:td (text-field {:maxlength 100} :lname lname)]]]
          ; email address
          (label :email "Email address")
          [:div.error (on-error :email first)]
          (email-field {:maxlength 250} :email email)
          ; email message)
          (label :message "Tell us about your project")
          [:div.error (on-error :message first)]
          (text-area {:maxlength 2000 :rows 10} :message message)
          ; submit button
          (submit-button "Send"))]]))

(defn build-name [fname lname]
  (trim (str fname " " lname)))

(defn build-message [fname lname email message]
  (str "Name: " (build-name fname lname) \newline
       "From: " email \newline
       message))

(defn send-contact [fname lname email message]
  (spit (io/file (System/getenv "SB_EMAIL_DIR") (random-file-name "txt"))
        (build-message fname lname email message))
  (contact "Thanks! We'll be in touch." "success"))

(defn handle-contact [fname lname email message]
  (rule (is-email? email) [:email "Please enter a valid email address."])
  (rule (has-value? message) [:message "Please enter a message."])
  (if (errors? :email :message)
    (contact "See the errors below" "error" fname lname email message)
    (send-contact fname lname email message)))

(defroutes contact-routes
  (GET "/contact" [] (contact))
  (POST "/contact" [fname lname email message] (handle-contact fname lname email message)))
