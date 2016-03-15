package funk4j.tuples;

import funk4j.adt.Option;

import java.io.Serializable;
import java.util.function.*;

/**
 * @author OZY on 2014.12.16
 */

public final class Pair<T1, T2> implements Serializable {

    private static final long serialVersionUID = 1L;

    public final T1 _1;
    public final T2 _2;

    public Pair(final T1 _1, final T2 _2) {
        this._1 = _1;
        this._2 = _2;
    }

    public static <T1, T2> Pair<T1, T2> of(final T1 val1, final T2 val2) {
        return new Pair<>(val1, val2);
    }

    public Option<T1> maybe1() {
        return Option.of(_1);
    }

    public Option<T2> maybe2() {
        return Option.of(_2);
    }

    public <R> R map(Function<Pair<T1, T2>, R> mapper) {
        return mapper.apply(this);
    }

    public <R> Pair<R, T2> map1(Function<T1,R> mapper) {
        R apply = mapper.apply(_1);
        return Pair.of(apply, _2);
    }

    public <R> Pair<T1, R> map2(Function<T2, R> mapper) {
        R apply = mapper.apply(_2);
        return Pair.of(_1, apply);
    }

    public <T3> Triplet<T1, T2, T3> add(final T3 val3) {
        return new Triplet<>(this._1, this._2, val3);
    }

    public Pair<T1,T2> update1(final T1 arg1) {
        return new Pair<>(arg1, this._2);
    }

    public Pair<T1,T2> update2(final T2 arg2) {
        return new Pair<>(this._1, arg2);
    }

    static <T,T1 extends T, T2 extends T> void forEach(
            Pair<T1, T2> pair,
            Consumer<? super T> consumer) {
        consumer.accept(pair._1);
        consumer.accept(pair._2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair pair = (Pair) o;
        return !(_1 != null ? !_1.equals(pair._1) : pair._1 != null) &&
               !(_2 != null ? !_2.equals(pair._2) : pair._2 != null);

    }

    @Override
    public int hashCode() {
        int result = _1 != null ? _1.hashCode() : 0;
        result = 31 * result + (_2 != null ? _2.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "_1=" + _1 +
                ", _2=" + _2 +
                '}';
    }

}
