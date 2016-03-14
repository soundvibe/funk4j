package funk4j.functions;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

/**
 * @author OZY on 2016.02.02.
 */
public final class ExpiringCache<T, U> implements Cache<T, U> {

    private static final long MILLIS_IN_ONE_DAY = TimeUnit.DAYS.toMillis(1L);
    private final Map<T, U> cache = new ConcurrentHashMap<>();
    private final Object lock = new Object();

    private ExpiringCache(long expireIn, TimeUnit timeUnit) {
        final long duration = timeUnit.toMillis(expireIn);
        schedule(duration, duration);
    }

    private ExpiringCache(long delay, long period) {
        schedule(delay, period);
    }

    /**
     * Constructs a cache which should expire periodically after specified expireIn period
     * @param expireIn expiration period
     * @param timeUnit time unit of expiration period
     * @return ExpiringCache instance
     */
    public static <T,U> ExpiringCache<T,U> periodically(long expireIn, TimeUnit timeUnit) {
        return new ExpiringCache<>(expireIn, timeUnit);
    }

    /**
     * Constructs a cache which should expire on specified time on a daily basis
     * @param expirationTime time when cache should be cleared. Time should be in UTC
     * @return ExpiringCache instance
     */
    public static <T,U> ExpiringCache<T,U> daily(LocalTime expirationTime) {
        return daily(expirationTime, () -> LocalTime.now(Clock.systemUTC()));
    }

    public static <T,U> ExpiringCache<T,U> daily(LocalTime expirationTime, Supplier<LocalTime> currentTimeSupplier) {
        final long msBetween = ChronoUnit.MILLIS.between(currentTimeSupplier.get(), expirationTime.atOffset(ZoneOffset.UTC));
        final long adjustedMsBetween = msBetween < 0 ? MILLIS_IN_ONE_DAY + msBetween : msBetween;
        return new ExpiringCache<>(adjustedMsBetween, MILLIS_IN_ONE_DAY);
    }

    @Override
    public U computeIfAbsent(T key,
                             Func1<? super T, ? extends U> mappingFunction) {
        return cache.computeIfAbsent(key, mappingFunction::apply);
    }

    private void schedule(long delay, long period) {
        final Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                synchronized (lock) {
                    cache.clear();
                }
            }
        }, delay, period);
    }

}
