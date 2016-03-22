package funk4j.matching;

import funk4j.adt.*;
import funk4j.tuples.Pair;
import org.junit.Test;

import java.util.*;

import static funk4j.matching.Matchers.*;
import static funk4j.matching.Matchers.instanceOf;
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
        final Option<String> actual = new Pattern<String>()
                .when(eq("John", john -> "found"))
                .apply("Bob");
        assertEquals(Option.none(), actual);
    }

    @Test
    public void shouldMatchSuccessOfTry() throws Exception {
        final String actual = new Pattern<Try<String>>()
                .when(trySuccess(s -> "got " + s))
                .match(Try.success("foo"));

        assertEquals("got foo", actual);
    }

    @Test
    public void shouldExtractSuccessOfTry() throws Exception {
        final String actual = new Pattern<Try<String>>()
                .when(trySuccess(eq("foo"), s -> "got " + s))
                .match(Try.success("foo"));

        assertEquals("got foo", actual);
    }

    @Test
    public void shouldMatchFailureOfTry() throws Exception {
        final String actual = new Pattern<Try<String>>()
                .when(tryFailure(s -> "got " + s.getMessage()))
                .match(Try.failure(new RuntimeException("foo")));

        assertEquals("got foo", actual);
    }

    @Test(expected = MatchError.class)
    public void shouldNotMatchFailureOfTry() throws Exception {
        new Pattern<Try<String>>()
                .when(tryFailure(s -> "got " + s.getMessage()))
                .match(Try.success("foo"));
    }

    @Test
    public void shouldExtractFailureOfTry() throws Exception {
        final RuntimeException exception = new RuntimeException("foo");
        final String actual = new Pattern<Try<String>>()
                .when(tryFailure(eq(exception), s -> "got " + s.getMessage()))
                .match(Try.failure(exception));

        assertEquals("got foo", actual);
    }

    @Test
    public void shouldExtractFailureOfTryWhenMatchedExceptionClass() throws Exception {
         final String actual = new Pattern<Try<String>>()
                .when(tryFailure(instanceOf(RuntimeException.class), s -> "got " + s.getMessage()))
                .match(Try.failure(new RuntimeException("foo")));

        assertEquals("got foo", actual);
    }

    @Test(expected = MatchError.class)
    public void shouldNotExtractFailureOfTry() throws Exception {
        new Pattern<Try<String>>()
                .when(tryFailure(classOf(RuntimeException.class), s -> "got " + s.getMessage()))
                .match(Try.success("foo"));
    }

    @Test(expected = MatchError.class)
    public void shouldNotMatchAndExtractFailureOfTry() throws Exception {
        final RuntimeException exception = new RuntimeException("foo");
        new Pattern<Try<String>>()
                .when(tryFailure(eq(exception), s -> "got " + s.getMessage()))
                .match(Try.failure(new RuntimeException("error")));
    }


    @Test
    public void shouldMatchOneCaseWhenEq() throws Exception {
        final String actual = new Pattern<String>()
                .when(eq("foo", s -> "got: " + s))
                .match("foo");

        assertEquals("got: foo", actual);
    }

    @Test
    public void shouldMatchOneNullCase() throws Exception {
        final String actual = new Pattern<String>()
                .when(isNull(() -> "got null"))
                .match(null);

        assertEquals("got null", actual);
    }

    @Test(expected = MatchError.class)
    public void shouldNotMatchOneNullCase() throws Exception {
        final String actual = new Pattern<String>()
                .when(isNull(() -> "got null"))
                .match("foo");

        assertEquals("got null", actual);
    }

    @Test
    public void shouldMatchOneNotNullCase() throws Exception {
        final String actual = new Pattern<String>()
                .when(isNotNull(e -> "got " + e))
                .match("foo");

        assertEquals("got foo", actual);
    }

    @Test(expected = MatchError.class)
    public void shouldNotMatchOneNotNullCase() throws Exception {
        final String actual = new Pattern<String>()
                .when(isNotNull(e -> "got " + e))
                .match(null);

        assertEquals("got foo", actual);
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

    @Test(expected = MatchError.class)
    public void shouldNotMatchWhenIsGreaterThen5() throws Exception {
        new Pattern<Integer>()
                .when(greaterThan(5))
                .match(5);
    }

    @Test
    public void shouldMatchWhenIsLessThen5() throws Exception {
        final Integer actual = new Pattern<Integer>()
                .when(lessThan(5))
                .match(4);
        assertEquals(4, actual.intValue());
    }

    @Test(expected = MatchError.class)
    public void shouldNotMatchWhenIsLessThen5() throws Exception {
        new Pattern<Integer>()
                .when(lessThan(5))
                .match(5);
    }

    @Test
    public void shouldMatchWhenIsGreaterThen5() throws Exception {
        final Integer actual = new Pattern<Integer>()
                .when(greaterThan(5))
                .match(6);
        assertEquals(6, actual.intValue());
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

    private static int fibonacciRecursion(int val) {
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
        final String actual = new Pattern<Option<String>>()
                .when(none(() -> "got: none"))
                .match(Option.none());

        assertEquals("got: none", actual);
    }

    @Test
    public void shouldMatchSome() throws Exception {
        final String actual = new Pattern<Option<String>>()
                .when(none(() -> "got: none"))
                .when(some(e -> "got: some " + e))
                .match(Option.some("foo"));

        assertEquals("got: some foo", actual);
    }

    @Test
    public void shouldMatchSomeWithValue() throws Exception {
        final String actual = new Pattern<Option<String>>()
                .when(none(() -> "got: none"))
                .when(some(eq("aaa"), e -> "got: some " + e))
                .when(some(eq("foo"), e -> "got: some " + e))
                .match(Option.of("foo"));

        assertEquals("got: some foo", actual);
    }

    @Test
    public void shouldMatchSomeWithAnyValue() throws Exception {
        final String actual = new Pattern<Option<String>>()
                .when(none(() -> "got: none"))
                .when(some(eq("aaa"), e -> "got: some " + e))
                .when(some(any(), e -> "got: some " + e))
                .match(Option.of("foo"));

        assertEquals("got: some foo", actual);
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
    public void shouldExtractPairWhenAny_SimpleForm() throws Exception {
        final String actual = new Pattern<Pair<String, Integer>>()
                .when(pair((name, value) -> name + "bar" + value))
                .match(Pair.of("foo", 100));

        assertEquals("foobar100", actual);
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

    private class FooExt extends Foo {

        final Integer age;

        FooExt(String name, Integer age) {
            super(name);
            this.age = age;
        }
    }

}
