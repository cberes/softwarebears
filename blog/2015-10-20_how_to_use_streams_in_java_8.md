%%begin-meta%%
title=Real-world tips for streams in Java 8
author=Corey Beres
timestamp=2015-10-20T10:00:00Z
%%end-meta%%

![Pre-Java-8 stream](/img/tuolumne_river_at_tuolumne_meadows.jpg "Streams existed before Java 8 as well")

We made the switch from Java 7 to Java 8 a few months ago, not long after the last public release of JDK 7. This is at my 9-to-5 job. It was practically a seamless transition. Since then, I've enjoyed using one of Java 8's new features: streams! Streams add a functional element to Java that is useful for processing collections of elements.

When I created my first streams, I wasn't aware of the full capabilities offered by this new feature. So, I'd like to share some tips from my real-world use of streams. While I've modified the objects to be more generic, these examples are very similar to streams I created for real services.

%%read-more%%

### The method reference operator

Along with streams, one of the new features in Java 8 is the **method reference operator**. I think of it as the "double colon" operator usually, because it's two colons in a row. This operator allows you to refer to a class method to produce a function interface type, such as a [Predicate](https://docs.oracle.com/javase/8/docs/api/java/util/function/Predicate.html) or [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html). For example, you might use `Point::toString` to use an object's `toString` method in the [map](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html#map-java.util.function.Function-) function.

Oracle has a [good list of example uses](https://docs.oracle.com/javase/specs/jls/se8/html/jls-15.html#jls-15.13) of the method reference operator.

I've found the method reference operator to be useful when creating new objects.

```java
Set<String> names = people.stream()
    .map(Person::getFirstName)
    .collect(toCollection(HashSet::new));
```

I didn't realize at first that the operator could be used to create new arrays as well.

```java
String[] urls = images.stream().map(i -> i.url)
    .filter(Objects::nonNull)
    .toArray(String[]::new);
```

Finally, the operator is useful to get an iterator from the stream.

```java
urlsToLoad.addAll(Arrays.stream(urls)
    .map(String::trim)::iterator);
```

### Flat streams

Say you have a list of HTML elements, and each element has a list of CSS classes. You can use the [flatMap](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html#flatMap-java.util.function.Function-) function to get a list of all the class names. Its argument must be a function that returns a stream.

```java
elements.stream().filter(e -> e.tag.equals("p"))
    .flapMap(e -> e.cssClassNames.stream())
    .collect(ArrayList::new);
```

There are also special functions for primitives, such as [flatMapToInt](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html#flatMapToInt-java.util.function.Function-), which accepts a function that returns an [IntStream](https://docs.oracle.com/javase/8/docs/api/java/util/stream/IntStream.html). If you have a list of items, each of which has a list of numeric categories, you could obtain a single array of the categories like this:

```java
int[] categories = items.stream()
    .filter(b -> b.categories != null)
    .flatMapToInt(b -> IntStream.of(b.categories))
    .toArray();
```

### Streaming maps

With streams, you can operate on maps in addition to arrays and collections. To do so, you can get a stream on the [entrySet](https://docs.oracle.com/javase/8/docs/api/java/util/Map.html#entrySet--). (Of course, you can use the [keys](https://docs.oracle.com/javase/8/docs/api/java/util/Map.html#keySet--) or [values](https://docs.oracle.com/javase/8/docs/api/java/util/Map.html#values--), but then you get only ... keys and values.) Then, you can use [toMap](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Collectors.html#toMap-java.util.function.Function-java.util.function.Function-) to convert the stream back to a map.

```java
cookies.entrySet().stream()
    .filter(c -> c.getValue().equals("1"))
    .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
```

You can also create a stream from a collection, and convert the stream to a map. Again, Oracle has some [examples](https://docs.oracle.com/javase/tutorial/collections/streams/reduction.html#collect) of how this can be done.

### Do not return streams from public methods

It's tempting, but I would advise against returning streams from methods, especially public methods. A stream can be used only once. A stream returned by a public method is bound to cause a bug at some point. A more flexible option is to return a collection. Then, the caller of the method can create a stream from the collection if they so choose.

For example, instead of this signature:

```java
public Stream<String> findFirstNames()
```

I would recommend this one:

```java
public Set<String> findFirstNames()
```

If it's a private method, then it might be okay to return a stream, because you can control its usage.

### Concatenate streams

Multiple streams can be concatenated into a single stream with the [concat](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html#concat-java.util.stream.Stream-java.util.stream.Stream-) function.

You might have a private function that accepts two streams as an argument. It could combine both streams using `concat`, then filter them together.

```java
private static List<Point> combine(Stream<Point> a, Stream<Point> b) {
    return Stream.concat(a, b)
        .filter(p -> p.x >= 0)
        .collect(Collectors.toList());
}
```

Calls to the function might look like the example below. You can create streams with the [empty](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html#empty--) and [of](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html#of-T...-) functions.

```java
// call with empty stream
List<Point> points1 = combine(PointFactory.randomPoints(10).stream(), Stream.empty());

// inject additional points from another source
List<Point> points2 = combine(points1, Stream.of(new Point(1, 1), new Point(-1, -1)));
```
