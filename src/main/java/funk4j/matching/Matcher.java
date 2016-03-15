package funk4j.matching;

import funk4j.adt.Option;

/**
 * @author Cipolinas on 2016.03.10.
 */
@FunctionalInterface
public interface Matcher<T, R> {

    Option<R> matches(T value);

}
