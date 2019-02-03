package funk4j.matching;

import org.junit.Test;

import java.util.*;
import static java.util.Collections.singletonList;
import static org.junit.Assert.*;

public class ListsTest {

    @Test
    public void shouldAddItem() throws Exception {
        final List<String> actual = Lists.add(new ArrayList<>(), "foo");
        assertEquals(singletonList("foo"), actual);
    }

    @Test
    public void shouldGetHeadOfAList() throws Exception {
        assertEquals("foo", Lists.head(Arrays.asList("foo", "bar")));
    }

    @Test
    public void shouldGeSecondHeadOfAList() throws Exception {
        assertEquals("bar", Lists.head(Arrays.asList("foo", "bar"), 1));
    }

    @Test
    public void shouldGetTail() throws Exception {
        assertEquals(singletonList("bar"), Lists.tail(Arrays.asList("foo", "bar")));
    }

    @Test
    public void shouldGetSecondTail() throws Exception {
        assertEquals(singletonList("3"), Lists.tail(Arrays.asList("foo", "bar", "3"), 2));
    }

    @Test
    public void shouldHeadReturnNull() throws Exception {
        assertNull(Lists.head(Collections.emptyList()));
    }
}