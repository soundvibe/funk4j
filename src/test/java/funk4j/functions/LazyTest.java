package funk4j.functions;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * @author OZY on 2016.03.14.
 */
public class LazyTest {

    @Test
    public void whenCalledMultipleTimes_ReturnCachedResult() throws Exception {
        Lazy<Integer> lazy = Lazy.of(() -> new Random().nextInt());
        Integer actual = lazy.get();
        Integer actualNext = lazy.get();
        assertEquals(actual, actualNext);
    }

    @Test
    public void returnExpectedResult() throws Exception {
        assertEquals("foobar", Lazy.of(() -> "foobar").get());
    }

    @Test
    public void twoLaziesAreEqual() throws Exception {
        Lazy<Integer> lazyOne = Lazy.of(() -> 1);
        Lazy<Integer> lazyTwo = Lazy.of(() -> 1);
        assertTrue(lazyOne.equals(lazyTwo));
    }

    @Test
    public void twoLaziesAreNotEqual() throws Exception {
        Lazy<Integer> lazyOne = Lazy.of(() -> 1);
        Lazy<Integer> lazyTwo = Lazy.of(() -> 2);
        assertFalse(lazyOne.equals(lazyTwo));
    }

    @Test
    public void whenIsCached_ToStringReturnsCachedValue() throws Exception {
        Lazy<Integer> lazyOne = Lazy.of(() -> 1);
        lazyOne.get();
        assertEquals("Lazy(1)", lazyOne.toString());
    }

    @Test
    public void whenIsNotCached_ToStringReturnsQuestionMark() throws Exception {
        Lazy<Integer> lazyOne = Lazy.of(() -> 1);
        assertEquals("Lazy(?)", lazyOne.toString());
    }

    @Test
    public void hashCodeIsNotZero() throws Exception {
        Lazy<Integer> lazyOne = Lazy.of(() -> 1);
        assertEquals(32, lazyOne.hashCode());
    }

}