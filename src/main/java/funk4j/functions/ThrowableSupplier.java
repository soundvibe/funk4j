package funk4j.functions;

/**
 * @author OZY on 2015.09.28.
 */
public interface ThrowableSupplier<T> {

    T get() throws Throwable;
}
