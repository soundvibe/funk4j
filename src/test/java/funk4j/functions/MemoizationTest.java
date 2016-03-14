package funk4j.functions;

import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static funk4j.functions.Memoization.memoize;
import static org.junit.Assert.*;

/**
 * @author OZY on 2016.03.14.
 */
public class MemoizationTest {

    @Test
    public void when_LongLastingMethod_Memoize_Is_Faster() throws Exception {
        Function<String, Integer> cachedFunc = memoize(this::getStringLength);
        long startTime = System.nanoTime();
        assertEquals(6, (int) cachedFunc.apply("FooBar"));
        long elapsed = System.nanoTime() - startTime;

        startTime = System.nanoTime();
        assertEquals(6, (int) cachedFunc.apply("FooBar"));
        long elapsedCached = System.nanoTime() - startTime;
        assertTrue("Cached method should be faster", elapsed > elapsedCached);
    }

    @Test
    public void shouldCacheMethodAndThenExpireAfterSomeTime() throws Exception {
        AtomicInteger counter = new AtomicInteger(0);
        Function<String, Integer> cachedFunc = memoize(s -> counter.incrementAndGet(), 100L, TimeUnit.MILLISECONDS);

        final Integer actual1 = cachedFunc.apply("a");
        assertEquals("Should cache value on first call", 1, actual1.intValue());

        final Integer actual2 = cachedFunc.apply("a");
        assertEquals("Should return cached value 1", 1, actual2.intValue());

        Thread.sleep(130L);
        final Integer actual3 = cachedFunc.apply("a");
        assertEquals("Should call function because cache is cleared", 2, actual3.intValue());

        final Integer actual4 = cachedFunc.apply("a");
        assertEquals("Should return cached value 2", 2, actual4.intValue());
    }

    private int getStringLength(String value) {
        try {
            Thread.sleep(100); //simulate long lasting operation
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return (value != null ? value.length() : 0);
    }

}