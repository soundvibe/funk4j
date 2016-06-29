package funk4j.adt;

import org.junit.Test;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static funk4j.adt.Option.*;
import static java.util.stream.Collectors.joining;
import static org.junit.Assert.*;

/**
 * @author OZY on 2016.03.14.
 */
public class OptionTest {

    @Test
    public void whenOf() throws Exception {
        assertEquals("foo", Option.of("foo").get());
    }

    @Test
    public void whenOfNull() throws Exception {
        assertTrue(Option.of(null).isEmpty());
    }

    @Test
    public void whenIsEmpty() throws Exception {
        assertTrue(none().isEmpty());
    }

    @Test
    public void whenIsNotEmpty() throws Exception {
        assertFalse(Option.of("foo").isEmpty());
    }

    @Test
    public void whenFilter() throws Exception {
        assertTrue(some("foo").filter(s -> s.equals("foo")).isPresent());
    }

    @Test
    public void whenNotFilter() throws Exception {
        assertFalse(Option.none().filter(s -> s.equals("foo")).isPresent());
    }

    @Test
    public void shouldCreateFromOptional() throws Exception {
        final String actual = Option.from(Optional.of("foo"))
                .get();
        assertEquals("foo", actual);
    }

    @Test
    public void shouldCreateFromEmptyOptional() throws Exception {
        assertTrue(Option.from(Optional.empty()).isEmpty());
    }

    @Test
    public void shouldEnumerate() throws Exception {
        AtomicReference<String> actual = new AtomicReference<>();
        Option.of("foo").foreach(actual::set);
        assertEquals("foo", actual.get());
    }

    @Test
    public void shouldConvertToOptional() throws Exception {
        final String actual = Option.of("foo").toOptional()
                .get();
        assertEquals("foo", actual);
    }

    @Test
    public void shouldConvertToEmptyOptional() throws Exception {
        assertFalse(Option.none().toOptional().isPresent());
    }

    @Test
    public void whenMap() throws Exception {
        assertEquals(1, some("foo").map(s -> 1).get().intValue());
    }

    @Test
    public void whenFlatMap() throws Exception {
        assertEquals(1, some("foo").flatMap(s -> Option.of(1)).get().intValue());
    }

    @Test
    public void shouldFlatMapNone() throws Exception {
        assertTrue(none().flatMap(Option::some).isEmpty());
    }

    @Test
    public void shouldMapNone() throws Exception {
        assertTrue(none().map(o -> "foo").isEmpty());
    }

    @Test
    public void whenOrElse() throws Exception {
        assertEquals("foo", none().orElse("foo"));
    }

    @Test
    public void shouldSkipOrElseWhenSome() throws Exception {
        final String actual = some("foo").orElse("bar");
        assertEquals("foo", actual);
    }

    @Test
    public void shouldSkipOrElseGetWhenSome() throws Exception {
        final String actual = some("foo").orElseGet(() -> "bar");
        assertEquals("foo", actual);
    }

    @Test
    public void shouldSkipOrElseThrowWhenSome() throws Exception {
        final String actual = some("foo").orElseThrow(IllegalStateException::new);
        assertEquals("foo", actual);
    }

    @Test
    public void whenOrElseGet() throws Exception {
        assertEquals("foo", none().orElseGet(() -> "foo"));
    }

    @Test(expected = RuntimeException.class)
    public void whenOrElseThrow() throws Exception {
        assertEquals("foo", none().orElseThrow(() -> new RuntimeException("foo")));
    }

    @Test
    public void whenPeek() throws Exception {
        List<String> strings = new ArrayList<>();
        some("foo").peek(strings::add);
        assertEquals("foo", strings.get(0));
    }

    @Test
    public void whenNotPeek() throws Exception {
        List<String> strings = new ArrayList<>();
        some("foo").filter(s -> false).peek(strings::add);
        assertTrue(strings.isEmpty());
    }

    @Test
    public void whenToTrySuccess() throws Exception {
        assertEquals("foo", some("foo").toTry().get());
    }

    @Test
    public void whenToTryFailure() throws Exception {
        assertTrue(none().toTry().isFailure());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldBeSerializable() throws Exception {
        Option<String> foo = some("foo");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(foo);
        byteArrayOutputStream.flush();

        byte[] bytes = byteArrayOutputStream.toByteArray();
        assertNotNull(bytes);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Option<String> readObject = (Option<String>) objectInputStream.readObject();
        assertEquals("foo", readObject.get());
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

    @Test
    public void shouldCastSome() throws Exception {
        Option<Object> foo = some("foo");
        final String actual = foo.cast(String.class)
                .get();

        assertEquals("foo", actual);
    }

    @Test
    public void shouldCastNone() throws Exception {
        Option<Object> foo = none();
        final boolean actual = foo.cast(String.class)
                .isPresent();

        assertEquals(false, actual);
    }

    @Test
    public void shouldFilterOutValueWhenCastingToClassWhichIsNotAssignable() throws Exception {
        Option<Object> foo = some("foo");
        final Option<Integer> actual = foo.cast(Integer.class);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void shouldSomeHaveEqualsAndHashcode() throws Exception {
        final Option<String> left = some("foo");
        final Option<String> right = some("foo");
        final Option<String> notRight = some("bar");

        assertEquals(left, right);
        assertEquals(left, left);
        assertNotEquals(left, notRight);
        assertTrue(left.equals(left));

        assertEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void shouldNoneHaveEqualsAndHashcode() throws Exception {
        final Option<String> left = none();
        final Option<String> right = none();
        final Option<String> notRight = some("bar");

        assertEquals(left, right);
        assertNotEquals(left, notRight);

        assertEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void shouldPrintToString() throws Exception {
        final Option<String> left = some("foo");
        final Option<String> right = none();

        assertEquals("Some(foo)", left.toString());
        assertEquals("None", right.toString());
    }

    @Test
    public void shouldFallbackToAnotherOption() throws Exception {
        Option<String> none = none();
        final Option<String> actual = none.or(() -> some("foo"));
        assertEquals(some("foo"), actual);
    }

    @Test
    public void shouldNotFallbackToAnotherOption() throws Exception {
        final Option<String> actual = some("foo").or(Option::none);
        assertEquals(some("foo"), actual);
    }
}
