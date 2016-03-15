package funk4j.matching;

import org.junit.Test;

import java.util.Optional;

import static funk4j.matching.JdkMatchers.*;
import static funk4j.matching.Matchers.any;
import static funk4j.matching.Matchers.eq;
import static org.junit.Assert.*;

/**
 * @author OZY on 2016.03.15.
 */
public class JdkMatchersTest {

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

}