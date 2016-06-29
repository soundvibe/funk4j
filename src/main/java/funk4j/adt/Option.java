package funk4j.adt;

import funk4j.functions.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;

/**
 * @author OZY on 2015.10.12.
 */

/**
 * Represents optional values. Instances of Option are either an instance of Some or the object None.
 * The most idiomatic way to use an Option instance is to treat it as a collection or monad and use map,flatMap, filter, or foreach.
 */
public interface Option<T>  {

    long serialVersionUID = 1L;

    static <T> Option<T> of(T value) {
        return value == null ? None.instance() : new Some<>(value);
    }

    static <T> Option<T> from(Optional<T> optional) {
        Objects.requireNonNull(optional);
        return optional.isPresent() ? Option.some(optional.get()) : Option.none();
    }

    static <T> Option<T> none() {
        return None.instance();
    }

    static <T> Option<T> some(T value) {
        Objects.requireNonNull(value, "Some should contain a value");
        return new Some<>(value);
    }

    boolean isEmpty();

    default boolean isPresent() {
        return !isEmpty();
    }

    T get();

    void ifPresent(Consumer<? super T> consumer);

    default Option<T> peek(Consumer<? super T> consumer) {
        ifPresent(consumer);
        return this;
    }

    <U> Option<U> cast(Class<U> aClass);

    default void foreach(Consumer<? super T> consumer) {
        ifPresent(consumer);
    }

    Option<T> filter(Predicate<? super T> predicate);

    <U> Option<U> map(Func1<? super T, ? extends U> mapper);

    <U> Option<U> flatMap(Func1<? super T, Option<U>> mapper);

    Option<T> or(Supplier<Option<T>> supplier);

    T orElse(T other);

    T orElseGet(Supplier<? extends T> other);

    <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X;

    default Stream<T> toStream() {
        return isEmpty() ? Stream.empty() : Stream.of(get());
    }

    default Optional<T> toOptional() {
        return isEmpty() ? Optional.empty() : Optional.of(get());
    }

    default Try<T> toTry() {
        return Try.from(this);
    }

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();

    @Override
    String toString();

}
