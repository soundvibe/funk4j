package funk4j.functions;

import java.util.Objects;
import java.util.function.Function;

public interface Func4<T1, T2, T3, T4, R> {

    R apply(T1 t1, T2 t2, T3 t3, T4 t4);

    default <V> Func4<T1, T2, T3, T4, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T1 t1, T2 t2, T3 t3, T4 t4) -> after.apply(apply(t1, t2, t3, t4));
    }

    default Func3<T2, T3, T4, R> partial1(T1 t1) {
        return (t2, t3, t4) -> apply(t1, t2, t3, t4);
    }

    default Func3<T1, T3, T4, R> partial2(T2 t2) {
        return (t1, t3, t4) -> apply(t1, t2, t3, t4);
    }

    default Func3<T1, T2, T4, R> partial3(T3 t3) {
        return (t1, t2, t4) -> apply(t1, t2, t3, t4);
    }

    default Func3<T1, T2, T3, R> partial4(T4 t4) {
        return (t1, t2, t3) -> apply(t1, t2, t3, t4);
    }

}
