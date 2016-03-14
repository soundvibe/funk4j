package funk4j.functions;

import org.junit.Test;

import static funk4j.functions.Func1.f;
import static org.junit.Assert.*;

/**
 * @author Cipolinas on 2016.03.14.
 */
public class FunctionsTest {

    @Test
    public void shouldApplyFunc1() throws Exception {
        Func1<String, String> func1 = s -> s + "bar";
        final String actual = func1.apply("foo");
        assertEquals("foobar", actual);
    }

    @Test
    public void shouldComposeFunc1() throws Exception {
        Func1<String, String> func1 = s -> s + "Bar";
        final String actual = func1.compose(v -> v + "To").apply("foo");
        assertEquals("fooToBar", actual);
    }

    @Test
    public void shouldAndThenFunc1() throws Exception {
        Func1<String, String> func1 = s -> s + "Bar";
        final String actual = func1.andThen(v -> v + "To").apply("foo");
        assertEquals("fooBarTo", actual);
    }

    @Test
    public void shouldConvertMethodToFunc1() throws Exception {
        final String actual = f(this::ordinaryMethod).apply("foo");
        assertEquals("foobar", actual);
    }

    @Test
    public void shouldConvertMethodToFunc1AndMemoize() throws Exception {
        final Func1<String, String> memoized = f(this::ordinaryMethod).memoized();
        assertEquals("foobar", memoized.apply("foo"));
        assertEquals("foobar", memoized.apply("foo"));
    }

    @Test
    public void shouldPipeForwardFunc2() throws Exception {
        Func2<String, String, String> func2 = (s, s2) -> s + s2;
        final String actual = func2.andThen(s -> s + "end").apply("foo", "bar");
        assertEquals("foobarend", actual);
    }

    @Test
    public void shouldPartiallyApplyFunc2() throws Exception {
        Func2<String, String, String> func2 = (s, s2) -> s + s2;
        final String actual = func2.partial1("foo").apply("bar");
        assertEquals("foobar", actual);

        final String actual2 = func2.partial2("foo").apply("bar");
        assertEquals("barfoo", actual2);
    }

    private String ordinaryMethod(String arg) {
        return arg + "bar";
    }
}
