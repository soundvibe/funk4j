package funk4j.matching;

import org.junit.Test;

import java.util.*;

import static funk4j.matching.JdkMatchers.*;
import static funk4j.matching.Matchers.any;
import static funk4j.matching.Matchers.eq;
import static java.util.Arrays.asList;
import static org.junit.Assert.*;

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

    @Test
    public void shouldMatchNilList() throws Exception {
        final String actual = new Pattern<List<String>>()
                .when(nil(() -> "nil"))
                .match(Collections.emptyList());

        assertEquals("nil", actual);
    }

    @Test(expected = MatchError.class)
    public void shouldNotMatchNilList() throws Exception {
        new Pattern<List<String>>()
                .when(nil(() -> "nil"))
                .match(Collections.singletonList("foo"));
    }

    @Test
    public void shouldMatchNilListWhenMatchForNull() throws Exception {
        final String actual = new Pattern<List<String>>()
                .when(nil(() -> "nil"))
                .match(null);
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

    @Test(expected = MatchError.class)
    public void shouldNotMatchHeadTailOfList() throws Exception {
        new Pattern<List<String>>()
                .when(headTail((x, xs) -> "head: " + x + " tail: " + xs))
                .match(Collections.emptyList());
    }

    @Test
    public void shouldMatch2HeadsAndTailOfList() throws Exception {
        final String actual = new Pattern<List<String>>()
                .when(nil(() -> "nil"))
                .when(headTail((x1, x2, xs) -> "head: " + x1 + x2 + " tail: " + xs))
                .match(asList("one", "two", "three"));

        assertEquals("head: onetwo tail: [three]", actual);
    }

    @Test(expected = MatchError.class)
    public void shouldNotMatch2HeadsTailOfList() throws Exception {
        new Pattern<List<String>>()
                .when(headTail((x1, x2, xs) -> "head: " + x1 + x2 + " tail: " + xs))
                .match(Collections.emptyList());
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
    public void shouldNotMatch3HeadsTailOfList() throws Exception {
        new Pattern<List<String>>()
                .when(headTail((x1, x2, x3, xs) -> x1 + x2 + x3 + " tail: " + xs))
                .match(Collections.emptyList());
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

    @Test(expected = MatchError.class)
    public void shouldNotMatch4HeadsTailOfList() throws Exception {
        new Pattern<List<String>>()
                .when(headTail((x1, x2, x3, x4, xs) -> x1 + x2 + x3 + x4 + " tail: " + xs))
                .match(Collections.emptyList());
    }


    @Test
    public void shouldMatchHeadOfList() throws Exception {
        final String actual = new Pattern<List<String>>()
                .when(nil(() -> "nil"))
                .when(head(x -> "head: " + x))
                .match(asList("one", "two", "three"));

        assertEquals("head: one", actual);
    }

    @Test(expected = MatchError.class)
    public void shouldNotMatchHeadOfList() throws Exception {
        new Pattern<List<String>>()
                .when(head(x -> "head: " + x))
                .match(Collections.emptyList());
    }

    @Test
    public void shouldMatchTailOfList() throws Exception {
        final String actual = new Pattern<List<String>>()
                .when(nil(() -> "nil"))
                .when(tail(xs -> "tail: " + xs))
                .match(asList("one", "two", "three"));

        assertEquals("tail: [two, three]", actual);
    }

    @Test(expected = MatchError.class)
    public void shouldNotMatchTailOfList() throws Exception {
        new Pattern<List<String>>()
                .when(tail(xs -> "tail: " + xs))
                .match(Collections.emptyList());
    }

    @Test(expected = MatchError.class)
    public void shouldNotMatchMapWithKey() throws Exception {
        new Pattern<Map<String, String>>()
                .when(map("foo", any(), (key, value) -> key + value))
                .match(Collections.emptyMap());
    }

    @Test(expected = MatchError.class)
    public void shouldNotMatchMapWithValue() throws Exception {
        Map<String, String> map = new HashMap<>(2);
        map.put("foo", "bar");
        new Pattern<Map<String, String>>()
                .when(map("foo", eq("foo"), (key, value) -> key + value))
                .match(map);
    }

    @Test
    public void shouldMatchMapWithKey() throws Exception {
        Map<String, String> map = new HashMap<>(2);
        map.put("foo", "bar");
        final String actual = new Pattern<Map<String, String>>()
                .when(map("foo", any(), (key, value) -> key + value))
                .match(map);
        assertEquals("foobar", actual);
    }

    @Test
    public void shouldMatchMapWithValue() throws Exception {
        HashMap<String, String> map = new HashMap<>(2);
        map.put("foo2", "bar2");
        map.put("foo", "bar");
        final String actual = new Pattern<Map<String, String>>()
                .when(map("foo", eq("bar"), (key, value) -> key + value))
                .match(map);
        assertEquals("foobar", actual);
    }
}