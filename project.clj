(defproject softwarebears "1.0.0-SNAPSHOT"
  :description "cberes.com website"
  :url "https://cberes.com"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.48"]
                 [compojure "1.4.0"]
                 [ring/ring-defaults "0.1.5"]
                 [hiccup "1.0.5"]
                 [lib-noir "0.9.9"]
                 [markdown-clj "0.9.69"]
                 [clj-rss "0.2.3"]]
  :plugins [[lein-ring "0.9.6"]
            [lein-cljsbuild "1.1.0"]]
  :ring {:handler softwarebears.handler/app}
  :cljsbuild {
    :builds [{
      :source-paths ["src-cljs"]
      :compiler {
        :output-to "resources/public/js/main.js"
        :optimizations :whitespace
        :pretty-print false}}]}
  :hooks [leiningen.cljsbuild]
  :main ^:skip-aot softwarebears.generator
  :jvm-opts ["--add-modules" "java.xml.bind"]
  :target-path "target/%s"
  :profiles {
    :uberjar {
      :aot :all}
    :dev {
      :dependencies [[javax.servlet/javax.servlet-api "3.1.0"]
                     [ring/ring-mock "0.2.0"]]}})
