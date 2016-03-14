package funk4j.adt;

import funk4j.functions.*;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.function.*;

/**
 * @author OZY on 2015.10.12.
 */
public final class None<T> implements Option<T>, Serializable {

    private static final long serialVersionUID = 1L;

    private final static None<?> INSTANCE = new None<>();

    private None() {
    }

    @SuppressWarnings("unchecked")
    public static <T> None<T> instance() {
        return (None<T>) INSTANCE;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public T get() {
        throw new NoSuchElementException("No value present");
    }

    @Override
    public void ifPresent(Consumer<? super T> consumer) {
        //nop
    }

    @Override
    public <U> Option<U> cast(Class<U> aClass) {
        return None.instance();
    }

    @Override
    public Option<T> filter(Predicate<? super T> predicate) {
        return this;
    }

    @Override
    public <U> Option<U> map(Func1<? super T, ? extends U> mapper) {
        return None.instance();
    }

    @Override
    public <U> Option<U> flatMap(Func1<? super T, Option<U>> mapper) {
        return None.instance();
    }

    @Override
    public T orElse(T other) {
        return other;
    }

    @Override
    public T orElseGet(Supplier<? extends T> other) {
        return other.get();
    }

    @Override
    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        throw exceptionSupplier.get();
    }

    @Override
    public boolean equals(Object o) {
        return o == this;
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public String toString() {
        return "None";
    }
}
