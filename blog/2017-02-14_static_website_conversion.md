%%begin-meta%%
title=How I converted this website to a static website
author=Corey Beres
timestamp=2017-02-14T22:40:00Z
%%end-meta%%

![Static](/img/static.png "Do not adjust your TV set.")

Yesterday I converted this website (my personal portfolio) to a static website. The advantages seemed clear: cheap hosting, faster loading times, etc. But this website was built with [Ring](https://github.com/ring-clojure/ring) and [Compojure](https://github.com/weavejester/compojure). I'll explain how I made the change in this post.

%%read-more%%

I made the changes in three commits. You can see them on GitHub: [here](https://github.com/cberes/softwarebears/commit/50c81cf40e020a0dcf7339036881b592b62cb220), [here](https://github.com/cberes/softwarebears/commit/beb8866753dfeb5bbfca8aea47e1485e67d04d84), and [here](https://github.com/cberes/softwarebears/commit/a6c2271505852f2732b2ea3361e5572b2c6c2bc3).

### Replace dynamic features with external services

The first change I made was to remove one dynamic feaure: the contact form.

The contact form sends an email to me with the contents of the form. At least that was the plan. Amazon limits your ability to send email from their EC2 service. You need to use their [SES](https://aws.amazon.com/ses) service to send email. I got this working for my website [7itemsorless.com](http://7itemsorless.com), but I never got around to making the change on this website. So this form actually saved a file to the server.

Using [Formspree](https://formspree.io/) made this change really easy. All I did was change the form's `action`, and I removed the parts of the module that processed the form. Formspree does the rest. So that part was easy

### Render static pages

Next, I wrote the code that saved each page's HTML to a file. Because of the way Compojure works, I already had functions written that created the HTML for each page. All that was left to do was to create a new `main` function that grabbed the HTML and saved it to a file.

I could have just saved the HTML to a file for each page (e.g. portfolio.html, contact.html, etc.). But I wanted to keep the same URLs from the dynamic website. Thus I created a folder with an index.html file inside for each page.

Now, a robust solution would have queried Compojure to get all available routes. Then HTML pages could be built for each of these routes. But I decided to make it easy on myself and hard-code the pages. Afterall, I don't envision my adding many pages to this website.

Here's the relevant code below.

```clojure
(ns softwarebears.generator
  (:gen-class)
  (:require [clojure.java.io :as io]
            [softwarebears.routes.home :refer [home]]
            [softwarebears.routes.portfolio :refer [portfolio]]
            [softwarebears.routes.blog :refer [blog]]
            [softwarebears.routes.contact :refer [contact]]
            [softwarebears.routes.error :refer [not-found]]))

(defn save-page [f & paths]
  (let [file (apply io/file paths)]
    (io/make-parents file)
    (spit file (f))))

(defn -main [& args]
  (let [root (first args)]
    (save-page home root "index.html")
    (save-page not-found root "404.html")
    (save-page portfolio root "portfolio" "index.html")
    (save-page contact root "contact" "index.html")
    (save-page blog root "blog" "index.html")))
```

One last thing. I had to edit project.clj and tell `lein` where to find the new `main` function.

### Render pages for each blog

One more dynamic feature remained: the blog. I needed to get every blog entry's URL and save an HTML file in the right location. Luckily, I already had written code that gets the blog entries and renders their HTML!

All that I needed to do was re-arrange some code in the blog module and then call the new functions from the `main` function. I created a helper function for this. Here it is.

```clojure
(defn save-blog-entries [root blog-root file-name]
  (doseq [id (get-blog-item-ids)]
    (save-page (render-blog-item id) root blog-root id file-name)))
```

And I called it like so:

```clojure
(save-blog-entries root "blog" "index.html")
```

### Putting it all together

I still needed to build the JAR, execute it, and copy some assets (JavaScript, CSS, and images). I use ClojureScript for this project. I was worried that it might be complicated to generate a JavaScript file from these sources. But that was a silly worry&mdash;the JavaScript was already being created every time I built the JAR.

I created a `bash` script that builds the JAR, executes the JAR, and copies assets. I think it would have been more prudent to create a Leiningen hook. However, I'm not fimiliar with Leiningen hooks. Instead I went with the quick and dirty solution (i.e. `bash`).

### Future work

All of that was sufficient to replace my EC2-hosted website with a static website that I can host for free on [GitHub Pages](https://pages.github.com/). Of course, there is still some work to do.

0. Add pagination for the blog entries
0. Create an RSS file to index blog entries
0. Query Compojure to get routes to generate
0. Write a Leiningen hook to build the static website
0. Clean up the build process (ClojureScript can be compiled independently from the uberjar's creation)

