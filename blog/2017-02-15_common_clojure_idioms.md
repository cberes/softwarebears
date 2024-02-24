%%begin-meta%%
title=Common Clojure idioms
author=Corey Beres
timestamp=2017-02-15T22:40:00Z
%%end-meta%%

![Lembert Dome with some Clojure on top](/img/clojure_lembert_dome.jpg "The Lembert Dome is pretty even with blurry Clojure on top.")

Back in December I participated in [Advent of Code](http://adventofcode.com/2016). It's a 25-day programming challenge with a new challenge posted everyday from December 1 to December 25. I decided to take on the challenge with Clojure. Here's what I learned.

%%read-more%%

I believe the challenges would have been easier had I used Java. Afterall, it's what I use most for work. But I couldn't bring myself to write Java in my spare time. And this seemed like a good opportunity to gain some experience with Clojure.

Using Clojure everyday for most of a month was great! Now I remember many more functions, and I learned some useful patterns.

### Use the `->` and `->>` operators

The `->` and `->>` are great for stringing together multiple function calls. The output of one function is used as an argument for the next function. With `->` the result is used as the first argument. With `->>` the result is used at the last argument.

The `->` operator is useful when processing maps. This is because functions like `update` and `assoc` return a map and they accept a map as their first argument. For example, take a look at this function from [day 17](https://github.com/cberes/advent-of-code/blob/master/src/advent_of_code/day17.clj) of the challenge. Given an initial state and a move, it creates the next state.

```clojure
(defn get-next-state [{[x y] :position :as start} move]
  (-> start
    (update :code conj (:letter move))
    (update :moves conj (:letter move))
    (assoc :position [(+ x (:x move)) (+ y (:y move))])))
```

On the other hand, the `->>` operator is useful for processing vectors and lists. Functions such as `map`, `filter`, `remove`, `count`, and `into` accept sequences as their final argument. Here is an example from [day 11](https://github.com/cberes/advent-of-code/blob/master/src/advent_of_code/day11.clj). This function filters a list of maps with a certain value, gets the `:element` value, and returns a set of the remaining values.

```clojure
(defn generator-elements [items]
  (->> items
    (filter #(= :generator (:type %)))
    (map :element)
    (into #{})))
```

Furthermore, you can create your own functions to use with these operators. As an example of an anti-pattern, check out this function from [day 3](https://github.com/cberes/advent-of-code/blob/master/src/advent_of_code/day3.clj)

```clojure
(defn run [file]
  (count (filter triangle? (fix-triples (read-triples file)))))
```

Now look at this example from [day 4](https://github.com/cberes/advent-of-code/blob/master/src/advent_of_code/day4.clj).

```clojure
(defn run [file]
  (->> file
    (read-names)
    (filter real?)
    (map decrypt)))
```

Which function is easier to read?

### Use `or` to find the first non-`nil` value

The `or` function is very useful. Where in other languages you might have a long `if` statement, you can use the `or` function with the results of several function calls. Then you will get the result of the first function invocation that does not return `nil`. Here is an excerpt from [day 8](https://github.com/cberes/advent-of-code/blob/master/src/advent_of_code/day8.clj) where a series of functions try to parse commands.

```clojure
(defn parse-cmd-as-rect [cmd]
  (when-let [matches (re-find rect-pattern cmd)]
    [:rect (to-int (nth matches 1)) (to-int (nth matches 2))]))

(defn parse-cmd-as-rot-row [cmd]
  (when-let [matches (re-find rotate-row-pattern cmd)]
    [:rot-row (to-int (nth matches 1)) (to-int (nth matches 2))]))

(defn parse-cmd-as-rot-col [cmd]
  (when-let [matches (re-find rotate-column-pattern cmd)]
    [:rot-col (to-int (nth matches 1)) (to-int (nth matches 2))]))

(defn parse-cmd [cmd]
  (or (parse-cmd-as-rect cmd)
      (parse-cmd-as-rot-row cmd)
      (parse-cmd-as-rot-col cmd)))
```

If you want a default value, you can specify that as the final argument to `or`.

### Use `recur` for recursion

The challenge's problems were all divided into two halves. The first half presented a problem. Usually it was relatively easy to write a solution that would return an answer within seconds. The second half usually added a twist that required orders of magnitude more processing.

One problem I had during the challenge was that my solution (which often involved recursion) would work for the first part of the day's challenge and fail for the second half with a stack overflow!

This is where the `recur` function saves the day! It invokes the current function with a new list of arguments. Or, if used with `loop`, it jumps back to the call to `loop` with new arguments. Behind the scenes, tail call optimization is used to prevent overflowing the stack.

Here is an example from [day 20](https://github.com/cberes/advent-of-code/blob/master/src/advent_of_code/day20.clj). It uses `loop` to define initial values for the recursive function.

```clojure
(defn find-min [ranges]
  (loop [ranges ranges
         allowed-count 0
         max-blocked 0]
    (if (empty? ranges)
      (+ allowed-count (- 4294967296 (inc max-blocked)))
      (let [[low high] (first ranges)]
        (recur (rest ranges)
               (if (< (inc max-blocked) low) (+ allowed-count (- low (inc max-blocked))) allowed-count)
               (max max-blocked high))))))
```

### Use Java's data structures only when necessary

You should use Clojure's immutable data structures whenever you can. However, you may find that one of Java's mutable data structures yields greater performance. In these cases, don't be afraid to use Java's data structure.

I ran into such a situation during [day 19](https://github.com/cberes/advent-of-code/blob/master/src/advent_of_code/day20.clj). Creating a new vector was causing my solution to run way too slowly. So I used a mutable ArrayList.

First, import the class.

```clojure
(:import [java.util ArrayList])
```

Add a type annotation to functions that return instances of the class. This example actually returns an ArrayList of vectors.

```clojure
(defn #^ArrayList initial-state [elf-count]
  (ArrayList. (->> elf-count
                (range)
                (map (fn [index] [index 1])))))
```

Finally, you may need to coerce explicitly the arguments to this class's methods. For example, when removing elements from the ArrayList (named `state` in this snippet).

```clojure
(.remove state (int giver-index))
```

### Decompose your solution into small readable functions

When writing Clojure, it can be easy to cram as much as I can into every single function. You end up with code that looks like the Rocky Mountains when rotated 90 degrees.

To combat this, break your monolothic functions into smaller functions. Give them names that explain what they do. And give useful names to the arguments. The single-letter parameter names you see in the Clojure API are just **bad** (check out [update](https://clojuredocs.org/clojure.core/update): `(update m k f x y z & more)`).

I think I did a reasonably good job of this on [day 15](https://github.com/cberes/advent-of-code/blob/master/src/advent_of_code/day15.clj). I defined some small functions.

```clojure
(defn update-position [{positions :positions position :position}]
  (mod (inc position) positions)) 

(defn tick [state]
  (update-values state #(assoc %1 :position (update-position %1))))

(defn done? [state capsule]
  (> capsule (key (apply max-key key state))))

(defn can-continue? [state capsule]
  (or (not (contains? state capsule)) (= 0 (:position (get state capsule)))))
```

This allowed my `solve` function to be much easier to read.

```clojure
(defn solve [initial-state]
  (loop [state initial-state
         capsule 0
         state-when-dropped state
         time-when-dropped 0]
    (cond
      (done? state capsule) time-when-dropped
      (can-continue? state capsule) (recur (tick state) (inc capsule) state-when-dropped time-when-dropped)
      :else (recur (tick state-when-dropped) 0 (tick state-when-dropped) (inc time-when-dropped)))))
```

