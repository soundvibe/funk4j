package funk4j.matching;

import funk4j.adt.Option;
import funk4j.functions.*;

import java.util.*;
import java.util.function.Supplier;

/**
 * @author OZY on 2016.03.15.
 */
public interface JdkMatchers {

    static <T,R> Matcher<Optional<T>,R> none(Supplier<R> supplier) {
        return val -> !val.isPresent() ? Option.of(supplier.get()) : Option.none();
    }

    static <T,R> Matcher<Optional<T>,R> some(Func1<T, R> func) {
        return val -> Option.from(val.map(func::apply));
    }

    static <T,U,R> Matcher<Optional<T>,R> some(Matcher<T, U> matcher, Func1<U, R> func) {
        return val -> Option.from(val)
                .flatMap(matcher::matches)
                .map(func::apply);
    }

    static <T,R, U extends Collection<T>> Matcher<U,R> nil(Supplier<R> supplier) {
        return val -> val == null ? Option.of(supplier.get()) : Option.of(val)
                .filter(Collection::isEmpty)
                .map(ts -> supplier.get());
    }

    static <T,R, U extends Collection<T>> Matcher<U,R> head(Func1<T, R> func) {
        return val -> Option.of(val)
                .filter(ts -> !ts.isEmpty())
                .map(ts -> func.apply(Lists.head(ts)));
    }

    static <T,R, U extends Collection<T>> Matcher<U,R> tail(Func1<Collection<T>, R> func) {
        return val -> Option.of(val)
                .filter(ts -> ts.size() > 1)
                .map(ts -> func.apply(Lists.tail(ts)));
    }

    static <T,R, U extends Collection<T>> Matcher<U,R> headTail(Func2<T, Collection<T>, R> func2) {
        return val -> Option.of(val)
                .filter(ts -> !ts.isEmpty())
                .map(ts -> func2.apply(Lists.head(ts), Lists.tail(ts)));
    }

    static <T,R, U extends Collection<T>> Matcher<U,R> headTail(Func3<T, T, Collection<T>, R> func3) {
        return val -> Option.of(val)
                .filter(ts -> ts.size() > 1)
                .map(ts -> func3.apply(Lists.head(ts), Lists.head(ts, 1), Lists.tail(ts, 2)));
    }

    static <T,R, U extends Collection<T>> Matcher<U,R> headTail(Func4<T, T, T, Collection<T>, R> func4) {
        return val -> Option.of(val)
                .filter(ts -> ts.size() > 2)
                .map(ts -> func4.apply(Lists.head(ts), Lists.head(ts, 1), Lists.head(ts, 2), Lists.tail(ts, 3)));
    }

    static <T,R, U extends Collection<T>> Matcher<U,R> headTail(Func5<T, T, T, T, Collection<T>, R> func5) {
        return val -> Option.of(val)
                .filter(ts -> ts.size() > 3)
                .map(ts -> func5.apply(Lists.head(ts), Lists.head(ts, 1), Lists.head(ts, 2), Lists.head(ts, 3), Lists.tail(ts, 4)));
    }

    static <T1, T2, U2, R> Matcher<Map<T1,T2>,R> map(T1 key, Matcher<T2, U2> valueMatcher, Func2<T1, U2, R> func) {
        return val -> Option.of(val)
                .map(m -> m.get(key))
                .flatMap(valueMatcher::matches)
                .map(v -> func.apply(key, v));
    }
}
