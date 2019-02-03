package funk4j.matching;

import funk4j.adt.Option;

public interface Match<T, R> {

    Option<R> apply(T valueToMatch);

    default R match(T valueToMatch) {
        return apply(valueToMatch)
                .orElseThrow(() -> new MatchError(valueToMatch));
    }

    Match<T, R> when(Matcher<T, R> matcher);

}
