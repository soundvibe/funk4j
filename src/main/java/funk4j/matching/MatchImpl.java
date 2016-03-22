package funk4j.matching;

import funk4j.adt.Option;

import java.util.List;

/**
 * @author Cipolinas on 2016.03.10.
 */
final class MatchImpl<T,R> implements Match<T,R> {

    private final List<Matcher<T,R>> matchers;

    MatchImpl(List<Matcher<T, R>> matchers) {
        this.matchers = matchers;
    }

    @Override
    public Option<R> apply(T valueToMatch) {
        for (Matcher<T, R> matcher : matchers) {
            final Option<R> matches = matcher.matches(valueToMatch);
            if (matches.isPresent()) {
                return matches;
            }
        }
        return Option.none();
    }

    @Override
    public Match<T, R> when(Matcher<T, R> matcher) {
        return new MatchImpl<>(Lists.add(matchers, matcher));
    }
}
