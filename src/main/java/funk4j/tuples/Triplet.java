package funk4j.tuples;

import funk4j.adt.Option;

import java.io.Serializable;
import java.util.*;
import java.util.function.*;

public final class Triplet<T1, T2, T3> implements Serializable, Iterable<Object> {

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

    public static <T1, T2, T3> Triplet<T1, T2, T3> triplet(final T1 val1, final T2 val2, final T3 val3) {
        return of(val1, val2, val3);
    }

    public Option<T1> maybe1() {
        return Option.of(_1);
    }

    public Option<T2> maybe2() {
        return Option.of(_2);
    }

    public Option<T3> maybe3() {
        return Option.of(_3);
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

    static <T,T1 extends T, T2 extends T,T3 extends T> void forEach(
            Triplet<T1, T2, T3> triplet,
            Consumer<? super T> consumer) {
        consumer.accept(triplet._1);
        consumer.accept(triplet._2);
        consumer.accept(triplet._3);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triplet triplet = (Triplet) o;
        return Objects.equals(_1, triplet._1) && Objects.equals(_2, triplet._2) && Objects.equals(_3, triplet._3);
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

    @Override
    public Iterator<Object> iterator() {
        final ArrayList<Object> objects = new ArrayList<>(3);
        objects.add(_1);
        objects.add(_2);
        objects.add(_3);
        return objects.iterator();
    }
}
