(ns softwarebears.views.layout
  (:require [hiccup.page :refer [html5 include-css include-js]]
            [hiccup.element :refer [link-to mail-to]]))

(def title "Software Bears")
(def phone "(716) 222 0088")
(def email "hello@softwarebears.com")

(defn common [& body]
  (html5
    [:head
      [:title title]
      (include-css "http://fonts.googleapis.com/css?family=Montserrat+Alternates:400,700")
      (include-css "/css/normalize.css")
      (include-css "/css/main.css")
      (include-css "/css/highlight/default.css")
      (include-js "/js/jquery-1.11.3.min.js")
      (include-js "/js/scroll.js")
      (include-js "/js/highlight.pack.js")
      [:script "hljs.configure({languages: []}); hljs.initHighlightingOnLoad();"]]
    [:body
      [:header
        [:h1.name
          [:span "Software"]
          [:span "Bears"]]]
      [:nav
        [:ul
          [:li (link-to "/" "Home")]
          [:li (link-to "/portfolio" "Portfolio")]
          [:li (link-to "/blog" "Blog")]
          [:li (link-to "/contact" "Contact us")]]]
      body
      [:footer.black
        [:h2
          "Want to see more? View our "
          (link-to "/portfolio" "portfolio")
          ", read our "
          (link-to "/blog" "blog")
          ", or check out our GitHub "
          (link-to "https://github.com/cberes" "repos")
          "."]
        [:p
          "Corey Beres" [:br]
          "Freelance software engineer" [:br]
          "SOA, web services, Android apps &amp; SDKs," [:br]
          "web applications, and database design"]
        [:p
          "Email: " (mail-to email) [:br]
          (str "Tel: " phone) [:br]
          "Find us on "
          (link-to "https://github.com/cberes" "GitHub") ", "
          (link-to "https://instagram.com/softwarebears" "Instagram") ", &amp "
          (link-to "https://twitter.com/softwarebears" "Twitter") [:br]
          "&copy; 2015 " [:span.name [:span "Software"] [:span "Bears"]]]
        [:div.clear]]]))
