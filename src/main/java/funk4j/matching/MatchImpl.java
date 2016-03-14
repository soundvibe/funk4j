package funk4j.matching;

import java.util.List;
import java.util.Optional;

/**
 * @author Cipolinas on 2016.03.10.
 */
public final class MatchImpl<T,R> implements Match<T,R> {

    private final List<Matcher<T,R>> matchers;

    public MatchImpl(List<Matcher<T, R>> matchers) {
        this.matchers = matchers;
    }

    @Override
    public Optional<R> apply(T valueToMatch) {
        for (Matcher<T, R> matcher : matchers) {
            final Optional<R> matches = matcher.matches(valueToMatch);
            if (matches.isPresent()) {
                return matches;
            }
        }
        return Optional.empty();
    }

    @Override
    public Match<T, R> when(Matcher<T, R> matcher) {
        return new MatchImpl<>(Lists.add(matchers, matcher));
    }
}
