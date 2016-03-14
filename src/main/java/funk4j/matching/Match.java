package funk4j.matching;

import java.util.Optional;

/**
 * @author Cipolinas on 2016.03.10.
 */
public interface Match<T, R> {

    Optional<R> apply(T valueToMatch);

    default R match(T valueToMatch) {
        return apply(valueToMatch)
                .orElseThrow(() -> new MatchError(valueToMatch));
    }

    Match<T, R> when(Matcher<T, R> matcher);

}
