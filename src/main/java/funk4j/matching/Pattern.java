package funk4j.matching;

public final class Pattern<T> {

    public Pattern() {
    }

    public <R> Match<T, R> when(Matcher<T, R> matcher) {
        return new MatchImpl<>(Lists.of(matcher));
    }
}
