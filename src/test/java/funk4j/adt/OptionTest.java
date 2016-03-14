package funk4j.adt;

import org.junit.Test;

import java.io.*;
import java.util.*;
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
    public void whenMap() throws Exception {
        assertEquals(1, some("foo").map(s -> 1).get().intValue());
    }

    @Test
    public void whenFlatMap() throws Exception {
        assertEquals(1, some("foo").flatMap(s -> Option.of(1)).get().intValue());
    }

    @Test
    public void whenOrElse() throws Exception {
        assertEquals("foo", none().orElse("foo"));
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
    public void shouldCast() throws Exception {
        Option<Object> foo = some("foo");
        final String actual = foo.cast(String.class)
                .get();

        assertEquals("foo", actual);
    }

    @Test
    public void shouldFilterOutValueWhenCastingToClassWhichIsNotAssignable() throws Exception {
        Option<Object> foo = some("foo");
        final Option<Integer> actual = foo.cast(Integer.class);
        assertTrue(actual.isEmpty());
    }

}