package funk4j.functions;

import org.junit.Test;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

public class ExpiringCacheTest {

    @Test
    public void shouldCacheAndThenExpireAndThenAddAgain() throws Exception {
        Cache<String, String> sut = ExpiringCache.periodically(100L, TimeUnit.MILLISECONDS);
        final String actual1 = sut.computeIfAbsent("1", s -> "foo");
        final String actual2 = sut.computeIfAbsent("2", s -> "bar");

        assertEquals("Should add foo to cache", "foo", actual1);
        assertEquals("Should add bar to cache", "bar", actual2);

        final String actual3 = sut.computeIfAbsent("1", s -> "foo1");
        final String actual4 = sut.computeIfAbsent("2", s -> "bar1");

        assertEquals("Should get foo from cache", "foo", actual3);
        assertEquals("Should get bar from cache", "bar", actual4);

        Thread.sleep(140L);

        final String actual5 = sut.computeIfAbsent("1", s -> "fooNew");
        final String actual6 = sut.computeIfAbsent("2", s -> "barNew");

        assertEquals("Should add fooNew to cache", "fooNew", actual5);
        assertEquals("Should add barNew to cache", "barNew", actual6);
    }

    @Test
    public void shouldScheduleOnDaily() throws Exception {
        AtomicReference<LocalTime> time = new AtomicReference<>(LocalTime.of(5,59,59, (int) TimeUnit.MILLISECONDS.toNanos(900L)));
        Cache<String, String> sut = ExpiringCache.daily(LocalTime.of(6,0), time::get);
        assertEquals("Should add foo to cache", "foo", sut.computeIfAbsent("1", s -> "foo"));
        assertEquals("Should get foo from cache", "foo", sut.computeIfAbsent("1", s -> "notFoo"));

        Thread.sleep(120L);
        assertEquals("Should add newFoo to cache", "newFoo", sut.computeIfAbsent("1", s -> "newFoo"));
        assertEquals("Should get newFoo from cache", "newFoo", sut.computeIfAbsent("1", s -> "notFoo"));
    }

    @Test
    public void shouldRunOnDailyBasis() throws Exception {
        final long msInOneDay = TimeUnit.DAYS.toMillis(1L);
        final long msBetween = ChronoUnit.MILLIS.between(LocalTime.of(10,1), LocalTime.of(8,0));
        final long adjustedMsBetween = msBetween < 0 ? msInOneDay + msBetween : msBetween;

        final String actual = timeDifference(adjustedMsBetween);
        assertEquals("21:59:00", actual);
    }

    @Test
    public void shouldRunOnDailyBasis2() throws Exception {
        final long msInOneDay = TimeUnit.DAYS.toMillis(1L);
        final long msBetween = ChronoUnit.MILLIS.between(LocalTime.of(5,0), LocalTime.of(8,0));
        final long adjustedMsBetween = msBetween < 0 ? msInOneDay + msBetween : msBetween;

        final String actual = timeDifference(adjustedMsBetween);
        assertEquals("03:00:00", actual);
    }

    @Test
    public void shouldRunOnDailyBasisWhenTimeIsTheSame() throws Exception {
        final long msInOneDay = TimeUnit.DAYS.toMillis(1L);
        final long msBetween = ChronoUnit.MILLIS.between(LocalTime.of(5,0), LocalTime.of(5,0));
        final long adjustedMsBetween = msBetween < 0 ? msInOneDay + msBetween : msBetween;

        final String actual = timeDifference(adjustedMsBetween);
        assertEquals("00:00:00", actual);
    }

    public static String timeDifference(long timeDifference1) {
        long timeDifference = timeDifference1 / 1000;
        int h = (int) (timeDifference / (3600));
        int m = (int) ((timeDifference - (h * 3600)) / 60);
        int s = (int) (timeDifference - (h * 3600) - m * 60);

        return String.format("%02d:%02d:%02d", h, m, s);
    }

}
