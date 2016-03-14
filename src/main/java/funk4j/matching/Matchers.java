package funk4j.matching;

import funk4j.functions.*;
import funk4j.tuples.Pair;

import java.util.*;
import java.util.function.Supplier;

/**
 * @author Cipolinas on 2016.03.10.
 */
public interface Matchers {

    static <T> Matcher<T,T> any() {
        return Optional::ofNullable;
    }

    static <T,R> Matcher<T,R> any(Func1<T, R> func) {
        return val -> Optional.ofNullable(func.apply(val));
    }

    static <T> Matcher<T,T> __() {
        return any();
    }

    static <T,R> Matcher<T,R> __(Func1<T, R> func) {
        return any(func);
    }

    static <T,U> Matcher<T,U> eq(U value) {
        return val -> Optional.ofNullable(value)
                .filter(t -> t.equals(val));
    }

    static <T,R,U> Matcher<T,R> eq(U value, Func1<U, R> func) {
        return val -> Optional.ofNullable(value)
                .filter(t -> t.equals(val))
                .map(func::apply);
    }

    static <T,U extends T,R> Matcher<T,R> classOf(Class<U> aClass, Func1<U, R> func) {
        return val -> Optional.ofNullable(val)
                .filter(t -> aClass.equals(t.getClass()))
                .map(aClass::cast)
                .map(func::apply);
    }

    static <T,U extends T,R> Matcher<T,R> instanceOf(Class<U> aClass, Func1<U, R> func) {
        return val -> Optional.ofNullable(val)
                .filter(aClass::isInstance)
                .map(aClass::cast)
                .map(func::apply);
    }

    static <T,R> Matcher<Optional<T>,R> none(Supplier<R> supplier) {
        return val -> !val.isPresent() ? Optional.ofNullable(supplier.get()) : Optional.empty();
    }

    static <T,R> Matcher<Optional<T>,R> some(Func1<T, R> func) {
        return val -> val.map(func::apply);
    }

    static <T,U,R> Matcher<Optional<T>,R> some(Matcher<T, U> matcher, Func1<U, R> func) {
        return val -> val
                .flatMap(matcher::matches)
                .map(func::apply);
    }

    static <R> Matcher<String,R> regex(String regex, Func1<String, R> func) {
        return val -> Optional.ofNullable(val)
                .filter(s -> s.matches(regex))
                .map(func::apply);
    }

    static <T,R, U extends Collection<T>> Matcher<U,R> nil(Supplier<R> supplier) {
        return val -> val == null ? Optional.ofNullable(supplier.get()) : Optional.of(val)
                .filter(Collection::isEmpty)
                .map(ts -> supplier.get());
    }

    static <T,R, U extends Collection<T>> Matcher<U,R> head(Func1<T, R> func) {
        return val -> Optional.ofNullable(val)
                .filter(ts -> !ts.isEmpty())
                .map(ts -> func.apply(Lists.head(ts)));
    }

    static <T,R, U extends Collection<T>> Matcher<U,R> tail(Func1<Collection<T>, R> func) {
        return val -> Optional.ofNullable(val)
                .filter(ts -> ts.size() > 1)
                .map(ts -> func.apply(Lists.tail(ts)));
    }

    static <T,R, U extends Collection<T>> Matcher<U,R> headTail(Func2<T, Collection<T>, R> func2) {
        return val -> Optional.ofNullable(val)
                .filter(ts -> !ts.isEmpty())
                .map(ts -> func2.apply(Lists.head(ts), Lists.tail(ts)));
    }

    static <T,R, U extends Collection<T>> Matcher<U,R> headTail(Func3<T, T, Collection<T>, R> func3) {
        return val -> Optional.ofNullable(val)
                .filter(ts -> ts.size() > 1)
                .map(ts -> func3.apply(Lists.head(ts), Lists.head(ts, 1), Lists.tail(ts, 2)));
    }

    static <T,R, U extends Collection<T>> Matcher<U,R> headTail(Func4<T, T, T, Collection<T>, R> func4) {
        return val -> Optional.ofNullable(val)
                .filter(ts -> ts.size() > 2)
                .map(ts -> func4.apply(Lists.head(ts), Lists.head(ts, 1), Lists.head(ts, 2), Lists.tail(ts, 3)));
    }

    static <T,R, U extends Collection<T>> Matcher<U,R> headTail(Func5<T, T, T, T, Collection<T>, R> func5) {
        return val -> Optional.ofNullable(val)
                .filter(ts -> ts.size() > 3)
                .map(ts -> func5.apply(Lists.head(ts), Lists.head(ts, 1), Lists.head(ts, 2), Lists.head(ts, 3), Lists.tail(ts, 4)));
    }

    static <T1, T2, U1, U2, R> Matcher<Pair<T1,T2>,R> pair(Matcher<T1, U1> matcher1, Matcher<T2, U2> matcher2,
                                                           Func2<U1, U2, R> func) {
        return val -> Optional.ofNullable(val)
                .flatMap(pair -> matcher1.matches(pair._1)
                        .flatMap(t1 -> matcher2.matches(pair._2).map(t2 -> Pair.of(t1, t2))))
                .map(pair -> func.apply(pair._1, pair._2));
    }


}
