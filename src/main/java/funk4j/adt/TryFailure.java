package funk4j.adt;

import funk4j.functions.Func1;

import java.io.Serializable;
import java.util.*;
import java.util.function.*;

final class TryFailure<T> implements Try<T>, Serializable {

    private static final long serialVersionUID = 1L;

    private final Throwable failure;

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    TryFailure(Throwable failure) {
        Objects.requireNonNull(failure, "Failure cannot be null");
        this.failure = failure;
    }

    @Override
    public <R> Try<R> map(Func1<? super T, R> mapper) {
        return Try.failure(failure);
    }

    @Override
    public <R> Try<R> flatMap(Func1<? super T, Try<R>> mapper) {
        return Try.failure(failure);
    }

    @Override
    public <R> Try<T> filter(Func1<? super T, R> predicate) {
        return this;
    }

    @Override
    public <R> Try<T> filter(Func1<? super T, R> predicate, Supplier<Throwable> failureSupplier) {
        return this;
    }

    @Override
    public <R> Try<T> filter(Func1<? super T, R> predicate, Func1<? super T, Throwable> failureSupplier) {
        return this;
    }

    @Override
    public Try<Throwable> failed() {
        return Try.success(failure);
    }

    @Override
    public Try<T> peek(Consumer<T> consumer) {
        return this;
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public void foreach(Consumer<T> consumer) {
        //do nothing
    }

    @Override
    public T get() {
        throw new IllegalStateException("Cannot get success because it is a failure: " + failure);
    }

    @Override
    public Throwable getFailure() {
        return this.failure;
    }

    @Override
    public Try<T> recover(Func1<Throwable, T> recoverFunc) {
        try {
            return Try.success(recoverFunc.apply(failure));
        } catch (Throwable t) {
            return Try.failure(t);
        }
    }

    @Override
    public Try<T> recoverWith(Func1<Throwable, Try<T>> throwableFunc) {
        try {
            return throwableFunc.apply(failure);
        } catch (Throwable t) {
            return Try.failure(t);
        }
    }

    @Override
    public <R> Try<R> transform(Func1<T, Try<R>> successMapper, Func1<Throwable, Try<R>> failureMapper) {
        try {
            return failureMapper.apply(failure);
        } catch (Throwable t) {
            return Try.failure(t);
        }
    }

    @Override
    public <R, X extends Throwable> Try<R> mapAny(Func1<T, R> successMapper, Func1<Throwable, X> failureMapper) {
        try {
            return Try.failure(failureMapper.apply(failure));
        } catch (Throwable t) {
            return Try.failure(t);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R> Try<R> cast(Class<R> aClass) {
        return (Try<R>) this;
    }

    @Override
    public Optional<T> toOptional() {
        return Optional.empty();
    }

    @Override
    public T orElse(Func1<Throwable, T> otherFunc) {
        return otherFunc.apply(this.failure);
    }

    @Override
    public T orElse(T other) {
        return other;
    }

    @Override
    public T orElse(Supplier<T> otherSupplier) {
        return otherSupplier.get();
    }

    @Override
    public void orElseThrow() throws Throwable {
        throw failure;
    }

    @Override
    public T orElseRuntimeThrow() throws RuntimeException {
        if (failure instanceof RuntimeException) throw (RuntimeException) failure;
        throw new RuntimeException(failure);
    }

    @Override
    public <E extends RuntimeException> T orElseRuntimeThrow(Function<Throwable, E> exceptionSupplier) throws E {
        throw exceptionSupplier.apply(failure);
    }

    @Override
    public <E extends Throwable> T orElseThrow(Function<Throwable, E> exceptionSupplier) throws E {
        throw exceptionSupplier.apply(failure);
    }

    @Override
    public Try<T> ifFailure(Consumer<Throwable> action) {
        action.accept(failure);
        return this;
    }

    @Override
    public String toString() {
        return "Failure{" + failure + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TryFailure<?> that = (TryFailure<?>) o;
        return Objects.equals(failure, that.failure);
    }

    @Override
    public int hashCode() {
        return Objects.hash(failure);
    }
}
