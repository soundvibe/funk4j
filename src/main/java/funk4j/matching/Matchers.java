package funk4j.matching;

import funk4j.adt.*;
import funk4j.functions.*;
import funk4j.tuples.Pair;

import java.util.function.Supplier;
import java.util.stream.Stream;

public interface Matchers {

    static <T> Matcher<T,T> any() {
        return Option::of;
    }

    static <T,R> Matcher<T,R> any(Func1<T, R> func) {
        return val -> Option.of(func.apply(val));
    }

    static <T> Matcher<T,T> __() {
        return any();
    }

    static <T,R> Matcher<T,R> __(Func1<T, R> func) {
        return any(func);
    }

    static <U, T extends Comparable<U>> Matcher<T,T> greaterThan(U valueToCompare) {
        return greaterThan(valueToCompare, Func1.identity());
    }

    static <U, T extends Comparable<U>, R> Matcher<T,R> greaterThan(U valueToCompare, Func1<T, R> func) {
        return value -> Option.of(value)
                .filter(t -> t.compareTo(valueToCompare) > 0)
                .map(func);
    }

    static <U, T extends Comparable<U>> Matcher<T,T> lessThan(U valueToCompare) {
        return lessThan(valueToCompare, Func1.identity());
    }

    static <U, T extends Comparable<U>, R> Matcher<T,R> lessThan(U valueToCompare, Func1<T, R> func) {
        return value -> Option.of(value)
                .filter(t -> t.compareTo(valueToCompare) < 0)
                .map(func);
    }

    static <T,U> Matcher<T,U> eq(U value) {
        return eq(value, Func1.identity());
    }

    @SafeVarargs
    static <T> Matcher<T,T> inEq(T... values) {
        return inEq(Stream.of(values), Func1.identity());
    }

    static <T, R> Matcher<T,R> inEq(Stream<T> values, Func1<T,R> func) {
        return value -> values
                .anyMatch(u -> eq(u).matches(value).isPresent()) ? Option.some(func.apply(value)) : Option.none();
    }

    @SafeVarargs
    static <T> Matcher<T,T> in(Matcher<T, T>... matchers) {
        return in(Stream.of(matchers), Func1.identity());
    }

    static <T, R> Matcher<T,R> in(Stream<Matcher<T, T>> matchers, Func1<T,R> func) {
        return value -> matchers
                .anyMatch(matcher -> matcher.matches(value).isPresent()) ? Option.some(func.apply(value)) : Option.none();
    }

    static <T,R,U> Matcher<T,R> eq(U value, Func1<U, R> func) {
        return val -> Option.of(value)
                .filter(t -> t.equals(val))
                .map(func);
    }

    static <T,R> Matcher<T,R> isNull(Supplier<R> supplier) {
        return value -> value == null ? Option.of(supplier.get()) : Option.none();
    }

    static <T, R> Matcher<T,R> isNotNull(Func1<T, R> func) {
        return value -> value != null ? Option.of(func.apply(value)) : Option.none();
    }

    static <T> Matcher<T,T> classOf(Class<T> aClass) {
        return classOf(aClass, Func1.identity());
    }

    static <T,U extends T,R> Matcher<T,R> classOf(Class<U> aClass, Func1<U, R> func) {
        return val -> Option.of(val)
                .filter(t -> aClass.equals(t.getClass()))
                .map(aClass::cast)
                .map(func);
    }

    static <T> Matcher<T,T> instanceOf(Class<T> aClass) {
        return instanceOf(aClass, Func1.identity());
    }

    static <T,U extends T,R> Matcher<T,R> instanceOf(Class<U> aClass, Func1<U, R> func) {
        return val -> Option.of(val)
                .filter(aClass::isInstance)
                .map(aClass::cast)
                .map(func);
    }

    static <T,R> Matcher<Option<T>,R> none(Supplier<R> supplier) {
        return val -> !val.isPresent() ? Option.of(supplier.get()) : Option.none();
    }

    static <T,R> Matcher<Option<T>,R> some(Func1<T, R> func) {
        return val -> val.map(func);
    }

    static <T,U,R> Matcher<Option<T>,R> some(Matcher<T, U> matcher, Func1<U, R> func) {
        return val -> val
                .flatMap(matcher::matches)
                .map(func);
    }

    static <T,R> Matcher<Try<T>,R> trySuccess(Func1<T, R> func) {
        return val -> val.map(func).toOption();
    }

    static <T,U,R> Matcher<Try<T>,R> trySuccess(Matcher<T, U> matcher, Func1<U, R> func) {
        return val -> val
                .flatMap(t -> Try.from(matcher.matches(t)))
                .map(func)
                .toOption();
    }

    static <T,R> Matcher<Try<T>,R> tryFailure(Func1<Throwable, R> func) {
        return val -> val.isFailure() ? Option.of(func.apply(val.getFailure())) : Option.none();
    }

    @SuppressWarnings("unchecked")
    static <T,R, E extends Throwable> Matcher<Try<T>,R> tryFailure(Matcher<E, E> matcher, Func1<E, R> func) {
        return val -> val.isFailure() && matcher.matches((E) val.getFailure()).isPresent() ?
                Option.of(func.apply((E) val.getFailure())) : Option.none();
    }

    static <R> Matcher<String,R> regex(String regex, Func1<String, R> func) {
        return val -> Option.of(val)
                .filter(s -> s.matches(regex))
                .map(func);
    }

    static <T1,T2,R> Matcher<Pair<T1,T2>, R> pair(Func2<T1, T2, R> func) {
        return pair(any(), any(), func);
    }

    static <T1, T2, U1, U2, R> Matcher<Pair<T1,T2>,R> pair(Matcher<T1, U1> matcher1, Matcher<T2, U2> matcher2,
                                                           Func2<U1, U2, R> func) {
        return val -> Option.of(val)
                .flatMap(pair -> matcher1.matches(pair._1)
                        .flatMap(t1 -> matcher2.matches(pair._2).map(t2 -> Pair.of(t1, t2))))
                .map(pair -> func.apply(pair._1, pair._2));
    }


}
