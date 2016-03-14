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
    public void shouldPipeForwardFunc3() throws Exception {
        Func3<String, String, String, String> func3 = (s, s2, s3) -> s + s2 + s3;
        final String actual = func3.andThen(s -> s + "end").apply("foo", "bar", "1");
        assertEquals("foobar1end", actual);
    }

    @Test
    public void shouldPipeForwardFunc4() throws Exception {
        Func4<String, String, String, String, String> func4 = (s, s2, s3, s4) -> s + s2 + s3 + s4;
        final String actual = func4.andThen(s -> s + "end").apply("foo", "bar", "1", "2");
        assertEquals("foobar12end", actual);
    }

    @Test
    public void shouldPipeForwardFunc5() throws Exception {
        Func5<String, String, String, String, String, String> func5 = (s, s2, s3, s4, s5) -> s + s2 + s3 + s4 + s5;
        final String actual = func5.andThen(s -> s + "end").apply("foo", "bar", "1", "2", "3");
        assertEquals("foobar123end", actual);
    }

    @Test
    public void shouldPartiallyApplyFunc2() throws Exception {
        Func2<String, String, String> func2 = (s, s2) -> s + s2;
        final String actual = func2.partial1("foo").apply("bar");
        assertEquals("foobar", actual);

        final String actual2 = func2.partial2("foo").apply("bar");
        assertEquals("barfoo", actual2);
    }

    @Test
    public void shouldPartiallyApplyFunc3() throws Exception {
        Func3<String, String, String, String> func3 = (s, s2, s3) -> s + s2 + s3;
        final String actual = func3.partial1("foo").partial2("bar").apply("end");
        assertEquals("fooendbar", actual);

        final String actual2 = func3.partial2("foo").partial1("bar").apply("end");
        assertEquals("barfooend", actual2);

        final String actual3 = func3.partial3("foo").partial1("bar").apply("end");
        assertEquals("barendfoo", actual3);
    }

    @Test
    public void shouldPartiallyApplyFunc4() throws Exception {
        Func4<String, String, String, String, String> func4 = (s, s2, s3, s4) -> s + s2 + s3 + s4;
        final String actual = func4.partial1("foo").partial2("bar").partial1("1").apply("end");
        assertEquals("foo1barend", actual);

        final String actual2 = func4.partial2("foo").partial1("bar").partial2("1").apply("end");
        assertEquals("barfooend1", actual2);

        final String actual3 = func4.partial3("foo").partial1("bar").partial2("1").apply("end");
        assertEquals("barendfoo1", actual3);

        final String actual4 = func4.partial4("foo").partial3("bar").partial2("1").apply("end");
        assertEquals("end1barfoo", actual4);
    }

    @Test
    public void shouldPartiallyApplyFunc5() throws Exception {
        Func5<String, String, String, String, String, String> func5 = (s, s2, s3, s4, s5) -> s + s2 + s3 + s4 + s5;
        final String actual = func5.partial1("foo").partial1("bar").partial1("1").partial1("2").apply("end");
        assertEquals("foobar12end", actual);

        final String actual2 = func5.partial2("foo").partial1("bar").partial1("1").partial1("2").apply("end");
        assertEquals("barfoo12end", actual2);

        final String actual3 = func5.partial3("foo").partial1("bar").partial1("1").partial1("2").apply("end");
        assertEquals("bar1foo2end", actual3);

        final String actual4 = func5.partial4("foo").partial1("bar").partial1("1").partial1("2").apply("end");
        assertEquals("bar12fooend", actual4);

        final String actual5 = func5.partial5("foo").partial1("bar").partial2("1").partial1("2").apply("end");
        assertEquals("bar21endfoo", actual5);
    }

    @Test
    public void shouldConvertToFunc2() throws Exception {
        final String actual = Func2.f((o, o2) -> "foo").apply("1", "2");
        assertEquals("foo", actual);
    }

    @Test
    public void shouldConvertToFunc3() throws Exception {
        final String actual = Func3.f((o, o2, o3) -> "foo").apply("1", "2", "3");
        assertEquals("foo", actual);
    }

    private String ordinaryMethod(String arg) {
        return arg + "bar";
    }
}
