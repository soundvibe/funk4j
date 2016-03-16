[![Maven Central](https://maven-badges.herokuapp.com/maven-central/net.soundvibe/funk4j/badge.svg)](https://maven-badges.herokuapp.com/maven-central/net.soundvibe/funk4j)
[![Build Status](https://travis-ci.org/soundvibe/funk4j.png)](https://travis-ci.org/soundvibe/funk4j)
[![Coverage Status](https://codecov.io/github/soundvibe/funk4j/coverage.svg?branch=master)](https://codecov.io/github/soundvibe/funk4j?branch=master)

# funk4j

funk(c)tional programming for Java >= 8.

## Pattern Matching

```java
@Test
public void shouldMatchHeadOfList() throws Exception {
    final String actual = new Pattern<List<String>>()
            .when(nil(() -> "nil"))
            .when(head(x -> "head: " + x))
            .match(asList("one", "two", "three"));

    assertEquals("head: one", actual);
}

@Test
public void shouldMatchSome() throws Exception {
    final String actual = new Pattern<Optional<String>>()
            .when(none(() -> "got: none"))
            .when(some(e -> "got: some " + e))
            .match(Optional.of("foo"));

    assertEquals("got: some foo", actual);
}

@Test
public void shouldCalcFibonacci() throws Exception {
    final int i = fibonacciRecursion(10);
    assertEquals(55, i);
}

public static int fibonacciRecursion(int val) {
    return new Pattern<Integer>()
            .when(eq(1, i -> 1))
            .when(eq(2, i -> 1))
            .when(__(x -> fibonacciRecursion(x - 1) + fibonacciRecursion(x - 2)))
            .match(val);
}

@Test
public void shouldReuseMatcher() throws Exception {

    final Match<String, String> matcher = new Pattern<String>()
            .when(eq("foo", s -> "got: " + s))
            .when(eq("bar", s -> "got: " + s))
            ;

    assertEquals("got: bar", matcher.match("bar"));
    assertEquals("got: foo", matcher.match("foo"));
}
```

## ```Option<T>``` for safe null handling
```java
@Test
public void shouldFilterSome() throws Exception {
    assertTrue(some("foo").filter(s -> s.equals("foo")).isPresent());
}

@Test
public void shouldComposeWithStream() throws Exception {
    Option<String> foo = some("foo");
    Option<String> none = none();
    final String actual = Stream.of(foo, none)
            .flatMap(Option::toStream)
            .collect(joining());

    assertEquals("foo", actual);
}

```


## ```Try<T>``` for safe exception handling
```java
@Test
public void shouldReturnCorrectValueWithoutThrowing() throws Exception {
    String actual = Try.from("foo")
            .filter(s -> s.equals("foo"))
            .map(s -> s + "bar")
            .peek(System.out::println)
            .transform(Try::success, Try::failure)
            .recover(throwable -> "not used")
            .orElse("not used");

    assertEquals("foobar", actual);
}

```

## ```Lazy<T>``` for lazy initialization
```java
@Test
public void whenCalledMultipleTimes_ReturnCachedResult() throws Exception {
    Lazy<Integer> lazy = Lazy.of(() -> new Random().nextInt());
    Integer actual = lazy.get();
    Integer actualNext = lazy.get();
    assertEquals(actual, actualNext);
}

```

And more...

## Binaries

Binaries and dependency information for Maven, Ivy, Gradle and others can be found at [http://search.maven.org](http://search.maven.org/#search%7Cga%7C1%7Cnet.soundvibe.funk4j).

Example for Gradle:

```groovy
compile 'net.soundvibe:funk4j:x.y.z'
```

and for Maven:

```xml
<dependency>
    <groupId>net.soundvibe</groupId>
    <artifactId>funk4j</artifactId>
    <version>x.y.z</version>
</dependency>
```


## Bugs and Feedback

For bugs, questions and discussions please use the [Github Issues](https://github.com/soundvibe/funk4j/issues).

## LICENSE

Copyright 2016 Linas Naginionis

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

<http://www.apache.org/licenses/LICENSE-2.0>

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

