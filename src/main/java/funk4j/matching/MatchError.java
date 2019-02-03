package funk4j.matching;

import java.util.NoSuchElementException;
import java.util.Objects;

public class MatchError extends NoSuchElementException {

    public final Object object;

    public MatchError(Object object) {
        this.object = object;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchError that = (MatchError) o;
        return Objects.equals(object, that.object);
    }

    @Override
    public int hashCode() {
        return Objects.hash(object);
    }

    @Override
    public String toString() {
        return "MatchError{" +
                "object=" + object +
                '}';
    }
}
