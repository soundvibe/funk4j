package funk4j.matching;

import org.junit.Test;

import static org.junit.Assert.*;

public class MatchErrorTest {

    @Test
    public void shouldHaveEqualsAndHashcode() throws Exception {
        final MatchError left = new MatchError("foo");
        final MatchError right = new MatchError("foo");
        final MatchError notRight = new MatchError("bar");
        assertEquals(left, left);
        assertEquals(left, right);
        assertEquals(left.hashCode(), right.hashCode());
        assertNotEquals(left, notRight);
        assertNotEquals(left, "foo");
        assertNotEquals(left, null);
    }

    @Test
    public void shouldPrintToString() throws Exception {
        final MatchError actual = new MatchError("foo");
        assertEquals("MatchError{object=foo}", actual.toString());
    }
}