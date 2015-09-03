(ns softwarebears.routes.home
  (:require [compojure.core :refer :all]
            [hiccup.element :refer [image link-to]]
            [softwarebears.views.layout :as layout]))

(defn home []
  (layout/common
    [:div.intro.headline.full.trnp.plax.plax1
      [:p "Some big text about me and what I can do, and why that's great for you."]]
    [:main
      [:section.white.slant-top-pre
        [:h2 "Featured project"]
        [:figure
          (link-to "/img/alsi_new.png" (image {:class "side"} "/img/alsi_new.png" "Screenshot of www.alsi.org"))]
        [:h3 "Advanced Life Support Institute"]
        [:p "Software Bears delivered to the Advanced Life Support Institute a modern design that focuses on content first. The latest web technologies, HTML 5 and CSS 3, were used."]
        [:div.reset.zero ]
        [:h4 (link-to "/portfolio#alsi" "Read more &gt;&gt;&gt;")]]
      [:section.grey.slant-top.from-right
        [:h2 "Featured project"]
        [:figure
          (link-to "/img/funner_main.png" (image {:class "side"} "/img/funner_main.png" "Screenshot of Funner Summer"))]
        [:h3 "Funner Summer, an Android app"]
        [:p "Software Bears created Funner Summer, an Android app that suggests activities based on several factors: time of day, day of week, current weather, and whether you're alone, with someone else, or with a group. The app records your activities and uses your history to improve suggestions."]
        [:div.reset.zero ]
        [:h4 (link-to "/portfolio#fs" "Read more &gt;&gt;&gt;")]]
      [:div.half.trnp.plax.plax2
        [:section.middle
          [:h2 "What my clients have said"]
          [:figure (image "" "")]
          [:blockquote
            "It was so great working with Corey, because he is a genius."
            [:footer [:cite "So and so"]]]]]
      [:section.grey.slant-bottom.from-left
        [:h2 "Latest blog post"]
        [:h3 "Most popular county names in America"]
        [:figure
          (image {:class "main" :style "max-width:561px;"} "/blog/img/county_chart.png" "Chart of most popular county names versus frequency")]
        [:p "Lately I've been curious about the names of our counties. I live in Erie County. Are there other Erie Counties? What's the most popular name for a county? Is Erie a popular name? What about Buffalo ... as few years back, there was some talk about changing Erie County's name to Buffalo."]
        [:p "Okay, it's not the most interesting topic. Heck, it's probably not "
            [:em "interesting"]
            " at all. But I have questions, and now I want answers. This feels like a good opportunity to use "
            (link-to "http://clojure.org/" "Clojure") "!"]
        [:h4 (link-to "/blog" "Read more &gt;&gt;&gt;")]]
      [:section.white.slant-bottom-post
        [:h2 "Contact me"]
        [:p "Email me or call me about a project, or just to ask a question."]
        [:p (link-to {:class "button"} "/contact" "Say hi")]]]))

(defroutes home-routes
  (GET "/" [] (home)))
