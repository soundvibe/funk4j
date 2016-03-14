package funk4j.functions;

import java.util.Objects;

/**
 * @author OZY on 2015.06.12.
 */
@FunctionalInterface
public interface Func3<T1, T2, T3, R> {

    R apply(T1 t1, T2 t2, T3 t3);

    default <V> Func3<T1, T2, T3, V> andThen(Func1<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (t1, t2, t3) -> after.apply(apply(t1, t2, t3));
    }

    static <T1, T2, T3, R> Func3<T1, T2, T3, R> f(Func3<T1, T2, T3, R> func3) {
        return func3;
    }

    default Func2<T2, T3, R> partial1(T1 t1) {
        return (t2, t3) -> apply(t1, t2, t3);
    }

    default Func2<T1, T3, R> partial2(T2 t2) {
        return (t1, t3) -> apply(t1, t2, t3);
    }

    default Func2<T1, T2, R> partial3(T3 t3) {
        return (t1, t2) -> apply(t1, t2, t3);
    }
}
