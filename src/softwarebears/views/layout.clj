(ns softwarebears.views.layout
  (:require [hiccup.page :refer [html5 include-css include-js]]
            [hiccup.element :refer [image javascript-tag link-to mail-to]]))

(def base-url (or (System/getenv "SB_BASEURL") ""))
(def description (or (System/getenv "SB_DESCRIPTION") ""))
(def title "SeaBears")
(def email "hello@seabears.net")
(def year (.get (java.util.Calendar/getInstance) java.util.Calendar/YEAR))

(defn common [& body]
  (html5
    [:head
      [:title title]
      (include-css "//fonts.googleapis.com/css?family=Montserrat+Alternates:400,700")
      (include-css "/css/normalize.min.css")
      (include-css "/css/main.css")
      (include-css "/css/highlight.min.css")
      (include-js "/js/main.js")
      (include-js "/js/highlight.pack.js")
      (javascript-tag "hljs.configure({languages: []}); hljs.initHighlightingOnLoad();")
      [:link {:rel "alternate" :type "application/rss+xml" :href "/rss.xml"}]
      [:link {:rel "apple-touch-icon" :sizes "57x57" :href "/apple-touch-icon-57x57.png"}]
      [:link {:rel "apple-touch-icon" :sizes "60x60" :href "/apple-touch-icon-60x60.png"}]
      [:link {:rel "apple-touch-icon" :sizes "72x72" :href "/apple-touch-icon-72x72.png"}]
      [:link {:rel "apple-touch-icon" :sizes "76x76" :href "/apple-touch-icon-76x76.png"}]
      [:link {:rel "apple-touch-icon" :sizes "114x114" :href "/apple-touch-icon-114x114.png"}]
      [:link {:rel "apple-touch-icon" :sizes "120x120" :href "/apple-touch-icon-120x120.png"}]
      [:link {:rel "icon" :type "image/png" :href "/favicon-32x32.png" :sizes "32x32"}]
      [:link {:rel "icon" :type "image/png" :href "/favicon-96x96.png" :sizes "96x96"}]
      [:link {:rel "icon" :type "image/png" :href "/favicon-16x16.png" :sizes "16x16"}]
      [:link {:rel "manifest" :href "/manifest.json"}]
      [:meta {:name "msapplication-TileColor" :content "#000000"}]
      [:meta {:name "theme-color" :content "#ffffff"}]]
    [:body
      [:header
        [:h1.name
          [:span "Sea"]
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
          "Tel: " (image {:class "bottom"} "/img/dial-now-dark.png") [:br]
          "Find us on "
          (link-to "https://github.com/cberes" "GitHub") ", "
          (link-to "https://instagram.com/coreyberes" "Instagram") ", &amp "
          (link-to "https://twitter.com/SeaBearsNet" "Twitter") [:br]
          "&copy; 2015-" year " " [:span.name [:span "Sea"] [:span "Bears"]]]
        [:div.clear]]]))
