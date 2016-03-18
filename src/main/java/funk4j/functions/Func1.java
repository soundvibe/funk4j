package funk4j.functions;

import java.time.LocalTime;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author OZY on 2015.06.12.
 */
@FunctionalInterface
public interface Func1<T, R> {

    /**
     * Applies this function to the given argument.
     *
     * @param t the function argument
     * @return the function result
     */
    R apply(T t);

    /**
     * Returns a composed function that first applies the {@code before}
     * function to its input, and then applies this function to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param <V> the type of input to the {@code before} function, and to the
     *           composed function
     * @param before the function to apply before this function is applied
     * @return a composed function that first applies the {@code before}
     * function and then applies this function
     * @throws NullPointerException if before is null
     */
    default <V> Func1<V, R> compose(Func1<? super V, ? extends T> before) {
        Objects.requireNonNull(before);
        return (V v) -> apply(before.apply(v));
    }

    /**
     * Returns a composed function that first applies this function to
     * its input, and then applies the {@code after} function to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param <V> the type of output of the {@code after} function, and of the
     *           composed function
     * @param after the function to apply after this function is applied
     * @return a composed function that first applies this function and then
     * applies the {@code after} function
     * @throws NullPointerException if after is null
     */
    default <V> Func1<T, V> andThen(Func1<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T t) -> after.apply(apply(t));
    }

    /**
     * Returns a function that always returns its input argument.
     *
     * @param <T> the type of the input and output objects to the function
     * @return a function that always returns its input argument
     */
    static <T> Func1<T, T> identity() {
        return t -> t;
    }

    static <T,R> Func1<T, R> f(Func1<T, R> func1) {
        return func1;
    }

    default Func1<T,R> memoized() {
        return Memoization.memoize1(this);
    }

    default Func1<T,R> memoized(long expireIn, TimeUnit timeUnit) {
        return Memoization.memoize1(this, expireIn, timeUnit);
    }

    default Func1<T,R> memoized(LocalTime expireInOnDailyBasis) {
        return Memoization.memoize1(this, expireInOnDailyBasis);
    }

}
