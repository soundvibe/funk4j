package funk4j.adt;

import funk4j.functions.*;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.*;
import java.util.stream.Stream;

/**
 * @author OZY on 2015.08.14.
 */

/**
 * The Try type represents a computation that may either result in an exception, or return a successfully computed value.
 */
public interface Try<T> {

    /**
     * Maps the given function to the value from this Success or returns this if this is a Failure.
     */
    <R> Try<R> map(Func1<? super T, R> mapper);

    /**
     * Returns the given function applied to the value from this Success or returns this if this is a Failure.
     */
    <R> Try<R> flatMap(Func1<? super T, Try<R>> mapper);

    /**
     * Converts this to a FilterNotSatisfied Failure if the predicate is not satisfied.
     */
    <R> Try<T> filter(Func1<? super T, R> predicate);

    /**
     * Converts this to a Failure supplied by failureSupplier if the predicate is not satisfied.
     */
    <R> Try<T> filter(Func1<? super T, R> predicate, Supplier<Throwable> failureSupplier);

    /**
     * Converts this to a Failure supplied by failureSupplier if the predicate is not satisfied.
     */
    <R> Try<T> filter(Func1<? super T, R> predicate, Func1<? super T, Throwable> failureSupplier);

    /**
     * Transforms a nested Try, ie, a Try of type Try&lt;Try&lt;T&gt;&gt;, into an un-nested Try, ie, a Try of type Try&lt;T&gt;.
     */
    default T flatten() {
        return get();
    }

    /**
     * Completes this Try with an exception wrapped in a Success.
     * The exception is either the exception that the Try failed with (if a Failure) or an UnsupportedOperationException.
     */
    Try<Throwable> failed();

    /**
     * Returns the same Try&lt;T&gt;, additionally performing the provided action if it is a success.
     * If consumer throws an exception, returns Try of failure.
     */
    Try<T> peek(Consumer<T> consumer);

    /**
     * Returns true if the Try is a Success, false otherwise.
     */
    boolean isSuccess();

    /**
     * Returns true if the Try is a Failure, false otherwise.
     */
    default boolean isFailure() {
        return !isSuccess();
    }

    /**
     * Applies the given consumer function if this is a Success, otherwise returns void if this is a Failure.
     * Note: If consumer throws, then this method may throw an exception.
     */
    void foreach(Consumer<T> consumer);

    /**
     * Returns the value from this Success or throws the exception if this is a Failure.
     */
    T get();

    /**
     * Returns the value from this Failure or throws the exception if this is a Success.
     */
    Throwable getFailure();

    /**
     * Applies the given function f if this is a Failure, otherwise returns this if this is a Success.
     * This is like map for the exception.
     */
    Try<T> recover(Func1<Throwable, T> recoverFunc);

    /**
     * Applies the given function f if this is a Failure, otherwise returns this if this is a Success.
     * This is like flatMap for the exception.
     */
    Try<T> recoverWith(Func1<Throwable, Try<T>> throwableFunc);

    /**
     * Completes this Try by applying the success function to this if this is of type Success,
     * or conversely, by applying failure function if this is Failure.
     */
    <R> Try<R> transform(Func1<T, Try<R>> successMapper, Func1<Throwable, Try<R>> failureMapper);

    /**
     * Completes this Try by applying the success function to this if this is of type Success,
     * or conversely, by applying failure function if this is Failure.
     */
    <R, X extends Throwable> Try<R> mapAny(Func1<T, R> successMapper, Func1<Throwable, X> failureMapper);

    <R> Try<R> cast(Class<R> aClass);

    /**
     * Returns Optional.empty() if this is a Failure or an Optional containing the value if this is a Success.
     */
    Optional<T> toOptional();

    default Option<T> toOption() {
        return isSuccess() ? Option.of(get()) : Option.none();
    }

    default Stream<T> toStream() {
        return isSuccess() ? Stream.of(get()) : Stream.empty();
    }

    T orElse(Func1<Throwable, T> otherFunc);

    /**
     * Returns the value from this Success or the given default argument if this is a Failure.
     */
    T orElse(T other);

    /**
     * Returns the value from this Success or the given supplied argument if this is a Failure.
     */
    T orElse(Supplier<T> otherSupplier);

    /**
     * Throws a failure exception if this is a failure, otherwise does nothing.
     */
    void orElseThrow() throws Throwable;

    T orElseRuntimeThrow() throws RuntimeException;

    <E extends RuntimeException> T orElseRuntimeThrow(Function<Throwable, E> exceptionSupplier) throws E;

    <E extends Throwable> T orElseThrow(Function<Throwable, E> exceptionSupplier) throws E;

    /**
     * Performs an action if is Failure, otherwise does nothing
     */
    Try<T> ifFailure(Consumer<Throwable> action);

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();

    @Override
    String toString();

    /**
     * Constructs a Try of Success from the given value
     */
    static <T> Try<T> from(final T value) {
        return from(() -> value);
    }

    static <T> Try<T> fromThrowableSupplier(final ThrowableSupplier<T> throwableSupplier) {
        if (throwableSupplier == null) return Try.failure(new NullPointerException("Cannot get Try from null value"));
        try {
            T item = throwableSupplier.get();
            return item != null ? Try.success(item) : Try.failure(new NullPointerException("Supplier retrieved null value"));
        } catch (Throwable t) {
            return Try.failure(t);
        }
    }


    /**
     * Constructs a Try of Success from the given supplier function.
     * If a supplier throws, constructs a Try of Failure with the thrown exception.
     * If a supplier returns null, constructs a Try with a Failure of NullPointerException.
     */
    static <T> Try<T> from(final Supplier<T> supplier) {
        if (supplier == null) return Try.failure(new NullPointerException("Cannot get Try from null value"));
        try {
            T item = supplier.get();
            return item != null ? Try.success(item) : Try.failure(new NullPointerException("Supplier retrieved null value"));
        } catch (Throwable t) {
            return Try.failure(t);
        }
    }

    /**
     * Constructs a Try with Success if Future completed successfully.
     * Otherwise constructs a Try of Failure with thrown exception.
     */
    static <T> Try<T> from(Future<T> future) {
        try {
            T t = future.get();
            return Try.success(t);
        } catch (Throwable t) {
            return Try.failure(t);
        }
    }

    /**
     * Construct Try from Optional value
     */
    static <T> Try<T> from(Optional<T> optionalValue) {
        return optionalValue.map(Try::success)
                .orElse(Try.failure(new NoSuchElementException("No value present in Optional<T>")));
    }

    /**
     * Construct Try from Option value
     */
    static <T> Try<T> from(Option<T> optionValue) {
        return Try.from(optionValue::get);
    }

    /**
     * Constructs a Try of a Success with a given value
     */
    static <T> Try<T> success(T value) {
        return new TrySuccess<>(value);
    }

    /**
     * Constructs a Try of a Failure with a given exception
     */
    static <T> Try<T> failure(Throwable t) {
        return new TryFailure<>(t);
    }

    static <T> T unchecked(ThrowableSupplier<T> throwableSupplier) {
        try {
            return throwableSupplier.get();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    static void doUnchecked(ThrowableRunnable throwableRunnable) {
        try {
            throwableRunnable.run();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    class FilterNotSatisfied extends RuntimeException {
        public FilterNotSatisfied(String message) {
            super(message);
        }
    }
}
