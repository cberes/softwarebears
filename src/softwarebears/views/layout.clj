(ns softwarebears.views.layout
  (:require [hiccup.page :refer [html5 include-css include-js]]
            [hiccup.element :refer [image javascript-tag link-to mail-to]]))

(def base-url (or (System/getenv "SB_BASEURL") ""))
(def description (or (System/getenv "SB_DESCRIPTION") ""))
(def title "cberes")
(def email "corey.beres+www@gmail.com")
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
      [:link {:rel "apple-touch-icon" :sizes "57x57" :href "/apple-icon-57x57.png"}]
      [:link {:rel "apple-touch-icon" :sizes "60x60" :href "/apple-icon-60x60.png"}]
      [:link {:rel "apple-touch-icon" :sizes "72x72" :href "/apple-icon-72x72.png"}]
      [:link {:rel "apple-touch-icon" :sizes "76x76" :href "/apple-icon-76x76.png"}]
      [:link {:rel "apple-touch-icon" :sizes "114x114" :href "/apple-icon-114x114.png"}]
      [:link {:rel "apple-touch-icon" :sizes "120x120" :href "/apple-icon-120x120.png"}]
      [:link {:rel "apple-touch-icon" :sizes "144x144" :href "/apple-icon-144x144png"}]
      [:link {:rel "apple-touch-icon" :sizes "152x152" :href "/apple-icon-152x152.png"}]
      [:link {:rel "apple-touch-icon" :sizes "180x180" :href "/apple-icon-180x180.png"}]
      [:link {:rel "icon" :type "image/png" :sizes "192x192" :href "/android-icon-192x192.png"}]
      [:link {:rel "icon" :type "image/png" :sizes "32x32" :href "/favicon-32x32.png"}]
      [:link {:rel "icon" :type "image/png" :sizes "96x96" :href "/favicon-96x96.png"}]
      [:link {:rel "icon" :type "image/png" :sizes "16x16" :href "/favicon-16x16.png"}]
      [:link {:rel "manifest" :href "/manifest.json"}]
      [:meta {:name "msapplication-TileColor" :content "#000000"}]
      [:meta {:name "msapplication-TileImage" :content "/ms-icon-144x144.png"}]
      [:meta {:name "theme-color" :content "#ffffff"}]]
    [:body
      [:header
        [:h1.name
          [:span "c"]
          [:span "beres"]]]
      [:nav
        [:div#show-nav]
        [:ul
          [:li (link-to "/" "Home")]
          [:li (link-to "/portfolio" "Portfolio")]
          [:li (link-to "/blog" "Blog")]
          [:li (link-to "/contact" "Contact")]]]
      body
      [:footer.black
        [:h2
          "Want to see more? View my "
          (link-to "/portfolio" "portfolio")
          ", read my "
          (link-to "/blog" "blog")
          ", or check out my GitHub "
          (link-to "https://github.com/cberes" "repos")
          "."]
        [:p
          "Corey Beres" [:br]
          "Professional software engineer" [:br]
          "SOA, web services, Android apps &amp; SDKs," [:br]
          "web applications, and database design"]
        [:p
          "Email: " (mail-to email) [:br]
          "Tel: " (image {:class "bottom"} "/img/dial-now-dark.png") [:br]
          "Find us on "
          (link-to "https://github.com/cberes" "GitHub") ", "
          (link-to "https://instagram.com/coreyberes" "Instagram") ", "
          (link-to "https://www.twitch.tv/cberes" "Twitch") ", &amp; "
          (link-to "https://twitter.com/_cberes" "Twitter") [:br]
          "&copy; 2015-" year " " [:span.name [:span "c"] [:span "beres"]]]
        [:div.clear]]]))
