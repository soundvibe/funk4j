package funk4j.functions;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Supplier;

public final class Lazy<T> implements Supplier<T>, Serializable {

    private static final long serialVersionUID = 1L;

    private volatile T value = null;

    private final Supplier<T> supplier;

    private Lazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public static <T> Lazy<T> of(final Supplier<T> supplier) {
        return new Lazy<>(supplier);
    }

    public boolean isCached() {
        return this.value != null;
    }

    @Override
    public T get() {
        synchronized (this) {
            if (!isCached()) {
                this.value = this.supplier.get();
            }
        }
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        return (o == this) || (o instanceof Lazy && Objects.equals(((Lazy) o).get(), get()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(get());
    }

    @Override
    public String toString() {
        return String.format("Lazy(%s)", isCached() ? value : "?");
    }
}
