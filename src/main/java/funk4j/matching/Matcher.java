package funk4j.matching;

import java.util.Optional;

/**
 * @author Cipolinas on 2016.03.10.
 */
@FunctionalInterface
public interface Matcher<T, R> {

    Optional<R> matches(T value);

}
