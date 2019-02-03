package funk4j.functions;

import java.util.Objects;

@FunctionalInterface
public interface Func2<T1, T2, R> {

    /**
     * Applies this function to the given arguments.
     *
     * @param t1 the first function argument
     * @param t2 the second function argument
     * @return the function result
     */
    R apply(T1 t1, T2 t2);

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
    default <V> Func2<T1, T2, V> andThen(Func1<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (t1, t2) -> after.apply(apply(t1, t2));
    }

    default Func1<T2, R> partial1(T1 t1) {
        return t2 -> apply(t1, t2);
    }

    default Func1<T1, R> partial2(T2 t2) {
        return t1 -> apply(t1, t2);
    }

    static <T1, T2, R> Func2<T1, T2, R> f(Func2<T1, T2, R> func2) {
        return func2;
    }
}
