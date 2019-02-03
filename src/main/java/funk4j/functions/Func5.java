package funk4j.functions;

import java.util.Objects;
import java.util.function.Function;

public interface Func5<T1, T2, T3, T4, T5, R> {

    R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5);

    default <V> Func5<T1, T2, T3, T4, T5, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) -> after.apply(apply(t1, t2, t3, t4, t5));
    }

    default Func4<T2, T3, T4, T5, R> partial1(T1 t1) {
        return (t2, t3, t4, t5) -> apply(t1, t2, t3, t4, t5);
    }

    default Func4<T1, T3, T4, T5, R> partial2(T2 t2) {
        return (t1, t3, t4, t5) -> apply(t1, t2, t3, t4, t5);
    }

    default Func4<T1, T2, T4, T5, R> partial3(T3 t3) {
        return (t1, t2, t4, t5) -> apply(t1, t2, t3, t4, t5);
    }

    default Func4<T1, T2, T3, T5, R> partial4(T4 t4) {
        return (t1, t2, t3, t5) -> apply(t1, t2, t3, t4, t5);
    }

    default Func4<T1, T2, T3, T4, R> partial5(T5 t5) {
        return (t1, t2, t3, t4) -> apply(t1, t2, t3, t4, t5);
    }

}
