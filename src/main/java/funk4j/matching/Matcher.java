package funk4j.matching;

import funk4j.adt.Option;

@FunctionalInterface
public interface Matcher<T, R> {

    Option<R> matches(T value);

}
