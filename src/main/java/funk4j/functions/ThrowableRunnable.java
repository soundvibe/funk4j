package funk4j.functions;

@FunctionalInterface
public interface ThrowableRunnable {

    void run() throws Throwable;

}
