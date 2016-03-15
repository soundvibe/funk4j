package funk4j.matching;

import funk4j.adt.Option;
import funk4j.functions.Func1;

import java.util.Optional;
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

}
