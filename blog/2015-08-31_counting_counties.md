%%begin-meta%%
title=Most popular county names in America
author=Corey Beres
timestamp=2015-08-31T10:00:00Z
%%end-meta%%

![Chart of most popular county names versus frequency](/img/county_chart.png "Chart of most popular county names versus frequency")

Lately I've been curious about the names of our counties. I live in Erie County. Are there other Erie Counties? What's the most popular name for a county? Is Erie a popular name? What about Buffalo ... a few years back, there was some talk about changing Erie County's name to Buffalo.

Okay, it's not the most interesting topic. Heck, it's probably not *interesting* at all. But I have questions, and now I want answers. This feels like a good opportunity to use [Clojure](http://clojure.org/)!

%%read-more%%

*You can find all the code in this post on [GitHub](https://github.com/cberes/counties).*

### Finding county data

First, I needed to find a source of data on counties. All I needed were names. Conveniently, there's a [Wikipedia article](https://en.wikipedia.org/wiki/List_of_United_States_counties_and_county_equivalents) with a list of counties in the USA. The article even lists state and population. Neato.

### Parsing the data

Being a Wikipedia article, our medium is HTML. That's easy to handle. In the past, I've used [jsoup](http://jsoup.org/), and I liked it well enough to use it again. It lets you parse HTML using CSS selectors, with which I'm already familiar. Here's an example from their website:

```java
Document doc = Jsoup.connect("http://en.wikipedia.org/").get();
Elements newsHeadlines = doc.select("#mp-itn b a");
```

But as I said, I want to use Clojure. If you search for "Clojure jsoup," changes are [clojure-soup](https://github.com/mfornos/clojure-soup) will be the top result. It has a similar API, except for a weird dollar-sign macro:

```clojure
($ (get! "http://en.wikipedia.org/") "#mp-itn b a")
```

There's also [Crouton](https://github.com/weavejester/crouton), but that parses documents into "a DOM tree compatible with that produced by clojure.xml/parse." I'm already familiar with jsoup's API. [Reaver](https://github.com/mischov/reaver) is a third option, which looks pretty similar to Clojure-soup. They both have similar stats on GitHub, but Clojure-soup's API is a little more terse, so I went with that option. I'd like to see a comparison of performance, but I couldn't find one. A future blog post, perhaps?

One more thing: the CSS selectors. Let's take a look at that table. There's no ID, but luckily it's the only table with the wikitable class. Jsoup has a variety of [selectors](http://jsoup.org/apidocs/org/jsoup/select/Selector.html) available, and the eq(n) selector will be really useful to get the second cell in each row. So, we end up with this selector:

```
table.wikitable tr td:eq(1)
```

Of course, `eq`'s index is zero-based.

### Cleaning the data

Did you read the Wikipedia article? The introduction would tell you that we're in trouble. For instance, Alaska is divided into buroughs and census areas, and, in Virginia and a few other states, some cities operate as counties. Some entries also have citations.

Let's write some initial code.

```clojure
(ns counties.core
  (:use jsoup.soup)
  (:gen-class))

(def url "https://en.wikipedia.org/wiki/List_of_United_States_counties_and_county_equivalents")

(defn names []
  "gets all raw county names"
  ($ (get! url :max-body-size 0)
     "table.wikitable tr td:eq(1)"
     (text)
     (map #(.replaceAll % "\\[\\d+\\]" ""))))

(defn -main
  "county counter"
  [& args]
  (println (remove #(.endsWith % " County") (names))))
```

First, we import Clojure-soup. Then, the URL of the Wikipedia article. Next, a function to get the county names from the table. Let's look at that.

We must set the `max-body-size` option when we read the article, because the page is over 1 MB. Passing zero as the argument tells jsoup not to limit the body size. The `(text)` function gets the text from the cell. Next, use `map` to remove all citations.

**Note:** *Clojure-soup 0.1.2 doesn't support the `max-body-size` option. I committed a [change](https://github.com/mfornos/clojure-soup/commit/f006b4f7cf2f85e6f4e4eef7f10effdd8d8e7bd1), which has since been merged, but a new version hasn't been pushed to [Clojars](https://clojars.org/clj-soup/clojure-soup).*

To take a peek at our data, we'll print out the names of every county that doesn't end with " County." Run that, and we see we have a lot of Parishes. Let's add that to the list.

```clojure
(defmacro any [& v] `(boolean (or ~@v)))

(defn -main
  "county counter"
  [& args]
  (println (remove #(any (.endsWith % " County")
                         (.endsWith % " Parish")) (names))))
```

I defined a macro to make the filter a little simpler. Run the program again. Looks a little better. Let's add " Census Area and " Borough" to the list.

```clojure
(defn -main
  "county counter"
  [& args]
  (println (remove #(any (.endsWith % " County")
                         (.endsWith % " Parish")
                         (.endsWith % " Census Area")
                         (.endsWith % " Borough")) (names))))
```

Run the program again, and ... We have to clean up the output. Let's print every county on its own line.

```clojure
(defn print-all [coll]
  "prints every element of the collection on a new line"
  (doseq [i coll] (println i)))
```

This function prints every element of the collection on its own line. Replace the reference to `println` with a call to `print-all`. Run the program again, and we see out last problem. Every county that's left is a city. Conveniently, we can just remove anything after the first comma we see. Add that filter to the `names` function, along with functions to remove the four common phrases we found.

```clojure
(defn names []
  "gets all raw county names"
  ($ (get! url :max-body-size 0)
     "table.wikitable tr td:eq(1)"
     (text)
     (map #(.replaceAll % "\\[\\d+\\]" ""))
     (map #(.replaceFirst % ",.+$" ""))
     (map #(.replaceFirst % " County$" ""))
     (map #(.replaceFirst % " Parish$" ""))
     (map #(.replaceFirst % " Borough$" ""))
     (map #(.replaceFirst % " Census Area$" ""))))
```

Now our data is as clean as a whistle!

### Counting counties

This step is pretty easy thanks to the magic of Clojure! Here's the rest of the code:

```clojure
(defn csv [m]
  "concats map keys and values with a comma"
  (map #(str (key %) "," (val %)) m))
  
(defn -main
  "county counter"
  [& args]
  (print-all (csv (sort-by val (frequencies (names))))))
```

Clojure provides the [frequencies](https://clojuredocs.org/clojure.core/frequencies) function, which counts the elements in a collection, returning a map of the distinct elements in the collection with the number of times they appear. We can use that on the list of names. But then we want to sort the counties. For that, we have the [sort-by](https://clojuredocs.org/clojure.core/sort-by) function. Because we're sorting a map by its values, we pass the [val](https://clojuredocs.org/clojure.core/val) function to `sort-by`.

Of course, the string representation of the key-value pairs is not very useful. For that, I defined a `csv` function. It simply joins the key and value of each pair with a comma. Now, run the program, and we get this output:

```
...
Carroll,13
Greene,14
Warren,14
Grant,15
Wayne,16
Monroe,17
Marion,17
Union,18
Montgomery,18
Clay,18
Madison,20
Lincoln,24
Jackson,24
Franklin,26
Jefferson,26
Washington,31
```

We can copy those lines, paste them into a spreadsheet, and generate a chart like the one at the beginning of this post!

### Get the code

The complete code for this post is on [GitHub](https://github.com/cberes/counties)!

### Next time

Next time, I'll show how to use Clojure to generate the chart of the most popular county names.
