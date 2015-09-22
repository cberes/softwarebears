(ns softwarebears.routes.home
  (:require [compojure.core :refer :all]
            [hiccup.element :refer [image link-to]]
            [softwarebears.routes.blog :refer [latest-blog-item]]
            [softwarebears.views.layout :as layout]))

(defn latest-project1 []
  [:section.white.slant-top-pre
    [:h2 "Featured project"]
    [:figure
      (link-to "/img/alsi_new.png" (image {:class "side"} "/img/alsi_new.png" "Screenshot of www.alsi.org"))]
    [:h3 "Advanced Life Support Institute"]
    [:p "Software Bears delivered to the Advanced Life Support Institute a modern design that focuses on content first. The latest web technologies, HTML 5 and CSS 3, were used."]
    [:div.reset.zero ]
    [:h4 (link-to "/portfolio#alsi" "Read more &gt;&gt;&gt;")]])

(defn latest-project2 []
  [:section.grey.slant-top.from-right
    [:h2 "Featured project"]
    [:figure
      (link-to "/img/funner_main.png" (image {:class "side"} "/img/funner_main.png" "Screenshot of Funner Summer"))]
    [:h3 "Funner Summer, an Android app"]
    [:p "Software Bears created Funner Summer, an Android app that suggests activities based on several factors: time of day, day of week, current weather, and whether you're alone, with someone else, or with a group. The app records your activities and uses your history to improve suggestions."]
    [:div.reset.zero ]
    [:h4 (link-to "/portfolio#fs" "Read more &gt;&gt;&gt;")]])

(defn client-quotes []
  [:div.half.trnp.plax.plax2
    [:section.middle
      [:h2 "Our clients have spoken"]
      [:figure (image {:class "who"} "/img/beres_chris.jpg" "Christine Beres, ALSI")]
      [:blockquote
        "I like it! Love the colors. And the photos jazz it up. I like the mobile web page. What a great idea. Never thought of that. Thank you so much for fixing things up.  "
        [:footer [:cite "Chris Beres, " (link-to "http://alsi.org/" "ALSI")]]]]])

(defn latest-blog-post []
  [:section.grey.slant-bottom.from-left.blog-imgs
    [:h2 "Latest blog post"]
    (latest-blog-item)])

(defn contact-us []
  [:section.white.slant-bottom-post
    [:h2 "Contact us"]
    [:p "Email us or call us about a project, or just to ask a question."]
    [:p (link-to {:class "button"} "/contact" "Say hi")]])

(defn home []
  (layout/common
    [:div.intro.headline.full.trnp.plax.plax1
      [:p "We're full-stack software engineers who are ready to accommodate any budget."]]
    [:main
      (latest-project1)
      (latest-project2)
      (client-quotes)
      (latest-blog-post)
      (contact-us)]))

(defroutes home-routes
  (GET "/" [] (home)))
