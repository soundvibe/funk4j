package funk4j.tuples;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;

/**
 * @author OZY on 2014.12.16
 */

public final class Triplet<T1, T2, T3> implements Serializable {

    private static final long serialVersionUID = 1L;

    public final T1 _1;
    public final T2 _2;
    public final T3 _3;

    public Triplet(final T1 _1, final T2 _2, final T3 _3) {
        this._1 = _1;
        this._2 = _2;
        this._3 = _3;
    }

    public static <T1, T2, T3> Triplet<T1, T2, T3> of(final T1 val1, final T2 val2, final T3 val3) {
        return new Triplet<>(val1, val2, val3);
    }

    public Optional<T1> maybe1() {
        return ofNullable(_1);
    }

    public Optional<T2> maybe2() {
        return ofNullable(_2);
    }

    public Optional<T3> maybe3() {
        return ofNullable(_3);
    }

    public <R> R map(Function<Triplet<T1, T2, T3>, R> mapper) {
        return mapper.apply(this);
    }

    public Triplet<T1, T2, T3> update1(final T1 arg1) {
        return new Triplet<>(arg1, this._2, this._3);
    }

    public Triplet<T1, T2, T3> update2(final T2 arg2) {
        return new Triplet<>(this._1, arg2, this._3);
    }

    public Triplet<T1, T2, T3> update3(final T3 arg3) {
        return new Triplet<>(this._1, this._2, arg3);
    }

    public Stream<Object> stream() {
        return Stream.of(this._1, this._2, this._3);
    }

    public Stream<Object> parallelStream() {
        return stream().parallel();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triplet triplet = (Triplet) o;
        return !(_1 != null ? !_1.equals(triplet._1) : triplet._1 != null) &&
               !(_2 != null ? !_2.equals(triplet._2) : triplet._2 != null) &&
               !(_3 != null ? !_3.equals(triplet._3) : triplet._3 != null);
    }

    @Override
    public int hashCode() {
        int result = _1 != null ? _1.hashCode() : 0;
        result = 31 * result + (_2 != null ? _2.hashCode() : 0);
        result = 31 * result + (_3 != null ? _3.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Triplet{" +
                "_1=" + _1 +
                ", _2=" + _2 +
                ", _3=" + _3 +
                '}';
    }
}
