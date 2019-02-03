package funk4j.adt;

import funk4j.functions.Func1;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.*;

final class Some<T> implements Option<T>, Serializable {

    private static final long serialVersionUID = 1L;

    private final T value;

    Some(T value) {
        this.value = value;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public void ifPresent(Consumer<? super T> consumer) {
        consumer.accept(value);
    }

    @Override
    public <U> Option<U> cast(Class<U> aClass) {
        if (aClass.isInstance(value)) {
            return new Some<>(aClass.cast(value));
        }
        return None.instance();
    }

    @Override
    public Option<T> filter(Predicate<? super T> predicate) {
        return predicate.test(value) ? this : Option.none();
    }

    @Override
    public <U> Option<U> map(Func1<? super T, ? extends U> mapper) {
        return Option.of(mapper.apply(value));
    }

    @Override
    public <U> Option<U> flatMap(Func1<? super T, Option<U>> mapper) {
        return mapper.apply(value);
    }

    @Override
    public Option<T> or(Supplier<Option<T>> supplier) {
        return this;
    }

    @Override
    public T orElse(T other) {
        return value;
    }

    @Override
    public T orElseGet(Supplier<? extends T> other) {
        return value;
    }

    @Override
    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj == this) || (obj instanceof Some && Objects.equals(value, ((Some<?>) obj).value));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return String.format("Some(%s)", value);
    }

    @Override
    public Iterator<T> iterator() {
        return Collections.singletonList(value).iterator();
    }
}
