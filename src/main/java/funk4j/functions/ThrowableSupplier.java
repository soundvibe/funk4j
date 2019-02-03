package funk4j.functions;

public interface ThrowableSupplier<T> {

    T get() throws Throwable;
}
