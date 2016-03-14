package funk4j.matching;

/**
 * @author Cipolinas on 2016.03.10.
 */
public final class Pattern<T> {

    public Pattern() {
    }

    public <R> Match<T, R> when(Matcher<T, R> matcher) {
        return new MatchImpl<>(Lists.of(matcher));
    }
}
