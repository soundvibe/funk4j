package funk4j.functions;

/**
 * @author OZY on 2016.03.02.
 */
@FunctionalInterface
public interface ThrowableRunnable {

    void run() throws Throwable;

}
