package funk4j.adt;

import funk4j.functions.Func1;
import funk4j.matching.*;

import java.io.Serializable;
import java.util.*;
import java.util.function.*;

import static funk4j.matching.Matchers.*;

/**
 * @author OZY on 2015.08.15.
 */
public final class TrySuccess<T> implements Try<T>, Serializable {

    private static final long serialVersionUID = 1L;

    private final T success;

    protected TrySuccess(T success) {
        Objects.requireNonNull(success, "Success cannot be null");
        this.success = success;
    }


    @Override
    public <R> Try<R> map(Func1<? super T, R> mapper) {
        try {
            return Try.success(mapper.apply(success));
        } catch (Throwable throwable) {
            return Try.failure(throwable);
        }
    }

    @Override
    public <R> Try<R> flatMap(Func1<? super T, Try<R>> mapper) {
        try {
            return mapper.apply(success);
        } catch (Throwable t) {
            return Try.failure(t);
        }
    }

    @Override
    public <R> Try<T> filter(Func1<? super T, R> predicate) {
        return filter(predicate, () -> new FilterNotSatisfied("Filter not satisfied"));
    }

    @Override
    public <R> Try<T> filter(Func1<? super T, R> predicate, Supplier<Throwable> failureSupplier) {
        return filter(predicate, t -> failureSupplier.get());
    }

    private static final Match<Object, Boolean> filterPredicateMatcher = new Pattern<>()
            .when(instanceOf(Boolean.class, Func1.identity()))
            .when(instanceOf(Collection.class, list -> !list.isEmpty()))
            .when(instanceOf(Optional.class, Optional::isPresent))
            .when(isNull(() -> false))
            .when(instanceOf(Try.class, Try::isSuccess))
    ;


    @Override
    public <R> Try<T> filter(Func1<? super T, R> predicate, Func1<? super T, Throwable> failureMapper) {
        try {
            R condition = predicate.apply(success);
            return filterPredicateMatcher.apply(condition).orElse(true) ?
                    this :
                    Try.failure(failureMapper.apply(success));
        } catch (Throwable t) {
            return Try.failure(t);
        }
    }

    @Override
    public Try<Throwable> failed() {
        return Try.failure(new UnsupportedOperationException());
    }

    @Override
    public Try<T> peek(Consumer<T> consumer) {
        try {
            consumer.accept(success);
            return this;
        } catch (Throwable t) {
            return Try.failure(t);
        }
    }

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public void foreach(Consumer<T> consumer) {
        consumer.accept(success);
    }

    @Override
    public T get() {
        return this.success;
    }

    @Override
    public Throwable getFailure() {
        throw new IllegalStateException("Cannot get failure because it is a success");
    }

    @Override
    public Try<T> recover(Func1<Throwable, T> recoverFunc) {
        return this;
    }

    @Override
    public Try<T> recoverWith(Func1<Throwable, Try<T>> throwableFunc) {
        return this;
    }

    @Override
    public <R> Try<R> transform(Func1<T, Try<R>> successMapper, Func1<Throwable, Try<R>> failureMapper) {
        return flatMap(successMapper);
    }

    @Override
    public <R, X extends Throwable> Try<R> mapAny(Func1<T, R> successMapper, Func1<Throwable, X> failureMapper) {
        try {
            return Try.success(successMapper.apply(success));
        } catch (Throwable throwable) {
            return Try.failure(failureMapper.apply(throwable));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R> Try<R> cast(Class<R> aClass) {
        return aClass.isAssignableFrom(success.getClass()) ?
            Try.success((R) success) :
            Try.failure(new ClassCastException("Cannot cast success " + success.getClass() + " to " + aClass));

    }

    @Override
    public Optional<T> toOptional() {
        return Optional.of(success);
    }

    @Override
    public T orElse(Func1<Throwable, T> otherFunc) {
        return success;
    }

    @Override
    public T orElse(T other) {
        return success;
    }

    @Override
    public T orElse(Supplier<T> otherSupplier) {
        return success;
    }

    @Override
    public void orElseThrow() throws Throwable {
        //do nothing
    }

    @Override
    public T orElseRuntimeThrow() throws RuntimeException {
        return success;
    }

    @Override
    public <E extends RuntimeException> T orElseRuntimeThrow(Function<Throwable, E> exceptionSupplier) throws E {
        return success;
    }

    @Override
    public <E extends Throwable> T orElseThrow(Function<Throwable, E> exceptionSupplier) throws E {
        return success;
    }

    @Override
    public Try<T> ifFailure(Consumer<Throwable> action) {
        return this;
    }

    @Override
    public String toString() {
        return "Success{" + success + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrySuccess<?> that = (TrySuccess<?>) o;
        return Objects.equals(success, that.success);
    }

    @Override
    public int hashCode() {
        return Objects.hash(success);
    }
}
