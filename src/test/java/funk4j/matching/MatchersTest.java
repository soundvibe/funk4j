package funk4j.matching;

import funk4j.tuples.Pair;
import org.junit.Test;

import java.util.*;

import static funk4j.matching.Matchers.*;
import static funk4j.matching.Matchers.any;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

/**
 * @author OZY on 2016.03.14.
 */
public class MatchersTest {

    @Test
    public void shouldReuseMatcher() throws Exception {

        final Match<String, String> matcher = new Pattern<String>()
                .when(eq("foo", s -> "got: " + s))
                .when(eq("bar", bar -> "got" + bar))
                ;

        assertEquals("gotbar", matcher.match("bar"));
        assertEquals("got: foo", matcher.match("foo"));
    }

    @Test(expected = MatchError.class)
    public void shouldNotMatchAndThrowMatchError() throws Exception {
        new Pattern<String>()
                .when(eq("John", john -> "found"))
                .match("Bob");
    }

    @Test
    public void shouldNotMatchAndReturnNone() throws Exception {
        final Optional<String> actual = new Pattern<String>()
                .when(eq("John", john -> "found"))
                .apply("Bob");
        assertEquals(Optional.empty(), actual);
    }

    @Test
    public void shouldMatchOneCaseWhenEq() throws Exception {
        final String actual = new Pattern<String>()
                .when(eq("foo", s -> "got: " + s))
                .match("foo");

        assertEquals("got: foo", actual);
    }

    @Test
    public void shouldMatchTwoCasesWhenEq() throws Exception {
        Object val = "bar";
        final String actual = new Pattern<>()
                .when(eq(1, s -> "got: " + s))
                .when(eq("bar", s -> "got: " + s))
                .when(eq(10L, l -> "got: " + l))
                .match(val);

        assertEquals("got: bar", actual);
    }

    @Test
    public void shouldMatchThreeClassCasesWhenEq() throws Exception {
        Object val = "bar";
        final String actual = new Pattern<>()
                .when(classOf(Integer.class, s -> "got: " + s))
                .when(classOf(String.class, s -> "got: " + s))
                .when(classOf(Long.class, l -> "got: " + l))
                .match(val);

        assertEquals("got: bar", actual);
    }

    @Test
    public void shouldMatchTwoCasesWithSubTypes() throws Exception {
        Foo foo = new FooExt("bar", 18);

        final String actual = new Pattern<Foo>()
                .when(classOf(Foo.class, s -> "got: " + s.name))
                .when(classOf(FooExt.class, s -> "got: " + s.age))
                .match(foo);

        assertEquals("got: 18", actual);
    }

    @Test
    public void shouldMatchInstanceOf() throws Exception {
        Foo foo = new FooExt("bar", 18);

        final String actual = new Pattern<Foo>()
                .when(classOf(Foo.class, s -> "should not match"))
                .when(instanceOf(Foo.class, s -> "got: " + s.name))
                .match(foo);

        assertEquals("got: bar", actual);
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
    public void shouldMatchTwoCasesWithSubTypesWhenEq() throws Exception {
        final String actual = new Pattern<Foo>()
                .when(eq(new Foo("foo"), s -> "got: " + s.name))
                .when(eq(new FooExt("bar", 18), s -> "got: " + s.age))
                .match(new Foo("foo"));

        assertEquals("got: foo", actual);
    }

    @Test
    public void shouldMatchNone() throws Exception {
        final String actual = new Pattern<Optional<String>>()
                .when(none(() -> "got: none"))
                .match(Optional.empty());

        assertEquals("got: none", actual);
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
    public void shouldMatchSomeWithValue() throws Exception {
        final String actual = new Pattern<Optional<String>>()
                .when(none(() -> "got: none"))
                .when(some(eq("aaa"), e -> "got: some " + e))
                .when(some(eq("foo"), e -> "got: some " + e))
                .match(Optional.of("foo"));

        assertEquals("got: some foo", actual);
    }

    @Test
    public void shouldMatchSomeWithAnyValue() throws Exception {
        final String actual = new Pattern<Optional<String>>()
                .when(none(() -> "got: none"))
                .when(some(eq("aaa"), e -> "got: some " + e))
                .when(some(any(), e -> "got: some " + e))
                .match(Optional.of("foo"));

        assertEquals("got: some foo", actual);
    }

    @Test
    public void shouldMatchNilList() throws Exception {
        final String actual = new Pattern<List<String>>()
                .when(nil(() -> "nil"))
                .match(Collections.emptyList());

        assertEquals("nil", actual);
    }

    @Test
    public void shouldMatchHeadTailOfList() throws Exception {
        final String actual = new Pattern<List<String>>()
                .when(nil(() -> "nil"))
                .when(headTail((x, xs) -> "head: " + x + " tail: " + xs))
                .match(asList("one", "two", "three"));

        assertEquals("head: one tail: [two, three]", actual);
    }

    @Test
    public void shouldMatch2HeadsAndTailOfList() throws Exception {
        final String actual = new Pattern<List<String>>()
                .when(nil(() -> "nil"))
                .when(headTail((x1, x2, xs) -> "head: " + x1 + x2 + " tail: " + xs))
                .match(asList("one", "two", "three"));

        assertEquals("head: onetwo tail: [three]", actual);
    }

    @Test
    public void shouldMatch3HeadsAndTailOfList() throws Exception {
        final String actual = new Pattern<List<String>>()
                .when(nil(() -> "nil"))
                .when(headTail((x1, x2, x3, xs) -> x1 + x2 + x3 + " tail: " + xs))
                .match(asList("one", "two", "three", "four"));

        assertEquals("onetwothree tail: [four]", actual);
    }

    @Test(expected = MatchError.class)
    public void shouldNotMatch3HeadsAndTailOfList() throws Exception {
        final String actual = new Pattern<List<String>>()
                .when(nil(() -> "nil"))
                .when(headTail((x1, x2, x3, xs) -> x1 + x2 + x3 + " tail: " + xs))
                .match(asList("one", "two"));

        assertEquals("onetwothree tail: [four]", actual);
    }

    @Test
    public void shouldMatch4HeadsAndTailOfList() throws Exception {
        final String actual = new Pattern<List<String>>()
                .when(nil(() -> "nil"))
                .when(headTail((x1, x2, x3, x4, xs) -> x1 + x2 + x3 + x4 + " tail: " + xs))
                .match(asList("one", "two", "three", "four"));

        assertEquals("onetwothreefour tail: []", actual);
    }

    @Test
    public void shouldMatchHeadOfList() throws Exception {
        final String actual = new Pattern<List<String>>()
                .when(nil(() -> "nil"))
                .when(head(x -> "head: " + x))
                .match(asList("one", "two", "three"));

        assertEquals("head: one", actual);
    }

    @Test
    public void shouldMatchTailOfList() throws Exception {
        final String actual = new Pattern<List<String>>()
                .when(nil(() -> "nil"))
                .when(tail(xs -> "tail: " + xs))
                .match(asList("one", "two", "three"));

        assertEquals("tail: [two, three]", actual);
    }

    @Test
    public void shouldMatchRegex() throws Exception {
        final String actual = new Pattern<String>()
                .when(eq("sdsds", s -> "error"))
                .when(regex("[a-z]+", s -> "regex matched"))
                .match("sra");

        assertEquals("regex matched", actual);
    }

    @Test
    public void shouldExtractPairWhenEq() throws Exception {
        final String actual = new Pattern<Pair<String, Integer>>()
                .when(pair(eq("foo"), eq(100), (_1, _2) -> _1 + "bar"))
                .match(Pair.of("foo", 100));

        assertEquals("foobar", actual);
    }

    @Test(expected = MatchError.class)
    public void shouldNotExtractPairWhenEq() throws Exception {
        new Pattern<Pair<String, Integer>>()
                .when(pair(eq("foo"), eq(150), (_1, _2) -> _1 + "bar"))
                .match(Pair.of("foo", 100));
    }

    @Test
    public void shouldExtractPairWhenAny() throws Exception {
        final String actual = new Pattern<Pair<String, Integer>>()
                .when(pair(__(), __(), (_1, _2) -> _1 + "bar" + _2))
                .match(Pair.of("foo", 100));

        assertEquals("foobar100", actual);
    }

    class Foo {
        final String name;

        Foo(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Foo foo = (Foo) o;
            return Objects.equals(name, foo.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }

    class FooExt extends Foo {

        final Integer age;

        FooExt(String name, Integer age) {
            super(name);
            this.age = age;
        }
    }

}
