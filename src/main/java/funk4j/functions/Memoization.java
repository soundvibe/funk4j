package funk4j.functions;

import java.time.LocalTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author OZY on 2014.12.15
 */

public interface Memoization {

    static <T, U> Function<T, U> memoize(final Function<T, U> function) {
        final Map<T, U> cache = new ConcurrentHashMap<>();
        return input -> cache.computeIfAbsent(input, function::apply);
    }

    static <T, U> Function<T, U> memoize(final Function<T, U> function, long expireIn, TimeUnit timeUnit) {
        final Cache<T, U> cache = ExpiringCache.periodically(expireIn, timeUnit);
        return input -> cache.computeIfAbsent(input, function::apply);
    }

    static <T, U> Function<T, U> memoize(final Function<T, U> function, LocalTime expireInOnDailyBasis) {
        final Cache<T, U> cache = ExpiringCache.daily(expireInOnDailyBasis);
        return input -> cache.computeIfAbsent(input, function::apply);
    }

    static <T, U> Func1<T, U> memoize1(final Func1<T, U> function) {
        final Map<T, U> cache = new ConcurrentHashMap<>();
        return input -> cache.computeIfAbsent(input, function::apply);
    }

    static <T, U> Func1<T, U> memoize1(final Func1<T, U> function, long expireIn, TimeUnit timeUnit) {
        final Cache<T, U> cache = ExpiringCache.periodically(expireIn, timeUnit);
        return input -> cache.computeIfAbsent(input, function::apply);
    }

    static <T, U> Func1<T, U> memoize1(final Func1<T, U> function, LocalTime expireInOnDailyBasis) {
        final Cache<T, U> cache = ExpiringCache.daily(expireInOnDailyBasis);
        return input -> cache.computeIfAbsent(input, function::apply);
    }
}
