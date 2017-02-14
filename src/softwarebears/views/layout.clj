(ns softwarebears.views.layout
  (:require [hiccup.page :refer [html5 include-css include-js]]
            [hiccup.element :refer [javascript-tag link-to mail-to]]))

(def base-url (System/getenv "SB_BASEURL"))
(def title "Software Bears")
(def phone "(716) 222 0088")
(def email "hello@softwarebears.com")
(def year (.get (java.util.Calendar/getInstance) java.util.Calendar/YEAR))

(defn common [& body]
  (html5
    [:head
      [:title title]
      (include-css "//fonts.googleapis.com/css?family=Montserrat+Alternates:400,700")
      (include-css (str base-url "/css/normalize.min.css"))
      (include-css (str base-url "/css/main.css"))
      (include-css (str base-url "/css/highlight.min.css"))
      (include-js (str base-url "/js/main.js"))
      (include-js (str base-url "/js/highlight.pack.js"))
      (javascript-tag "hljs.configure({languages: []}); hljs.initHighlightingOnLoad();")
      [:link {:rel "apple-touch-icon" :sizes "57x57" :href (str base-url "/apple-touch-icon-57x57.png")}]
      [:link {:rel "apple-touch-icon" :sizes "60x60" :href (str base-url "/apple-touch-icon-60x60.png")}]
      [:link {:rel "apple-touch-icon" :sizes "72x72" :href (str base-url "/apple-touch-icon-72x72.png")}]
      [:link {:rel "apple-touch-icon" :sizes "76x76" :href (str base-url "/apple-touch-icon-76x76.png")}]
      [:link {:rel "apple-touch-icon" :sizes "114x114" :href (str base-url "/apple-touch-icon-114x114.png")}]
      [:link {:rel "apple-touch-icon" :sizes "120x120" :href (str base-url "/apple-touch-icon-120x120.png")}]
      [:link {:rel "icon" :type "image/png" :href (str base-url "/favicon-32x32.png") :sizes "32x32"}]
      [:link {:rel "icon" :type "image/png" :href (str base-url "/favicon-96x96.png") :sizes "96x96"}]
      [:link {:rel "icon" :type "image/png" :href (str base-url "/favicon-16x16.png") :sizes "16x16"}]
      [:link {:rel "manifest" :href (str base-url "/manifest.json")}]
      [:meta {:name "msapplication-TileColor" :content "#000000"}]
      [:meta {:name "theme-color" :content "#ffffff"}]]
    [:body
      [:header
        [:h1.name
          [:span "Software"]
          [:span "Bears"]]]
      [:nav
        [:div#show-nav]
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
          "&copy; 2015-" year " " [:span.name [:span "Software"] [:span "Bears"]]]
        [:div.clear]]]))
