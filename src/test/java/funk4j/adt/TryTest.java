package funk4j.adt;

import funk4j.functions.Func1;
import org.junit.*;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.*;

/**
 * @author OZY on 2016.03.14.
 */
@SuppressWarnings({"ThrowableResultOfMethodCallIgnored", "NumericOverflow"})
public class TryTest {

    @Test(expected = RuntimeException.class)
    public void whenFlatMapThrowsException_OrElseThrowRethrowsIt() throws Throwable {
        Try.from(() -> "foo")
                .flatMap(s -> {
                    throw new RuntimeException("error");
                })
                .orElseThrow();
    }

    @Test
    public void whenRecoverWithNewSuccess_GetIt() throws Exception {
        String actual = Try.from("foo")
                .map(fromValue -> {
                            if (fromValue.equals("Bob")) {
                                return "bob";
                            } else {
                                throw new RuntimeException(fromValue);
                            }
                        }
                )
                .recoverWith(throwable -> Try.success("bar"))
                .get();

        assertEquals("bar", actual);
    }

    @Test
    public void whenPipelineIsSuccess() throws Exception {
        String actual = Try.from("foo")
                .filter(s -> s.equals("foo"))
                .map(s -> s + "bar")
                .peek(System.out::println)
                .transform(Try::success, Try::failure)
                .recover(throwable -> "not used")
                .orElse("not used");

        assertEquals("foobar", actual);
    }

    @Test
    public void whenPipelineIsFailure_RecoverFromIt() throws Exception {
        String actual = Try.from("foo")
                .filter(s -> s.equals("foobar"))
                .map(s -> s + "bar")
                .peek(System.out::println)
                .transform(Try::success, Try::failure)
                .recover(throwable -> "not used")
                .orElse("not used at all");

        assertEquals("not used", actual);

    }

    @Test
    public void whenFlatteningReturnCorrectValue() throws Exception {
        String actual = Try.from(Try.from("foo"))
                .flatten()
                .get();

        assertEquals("foo", actual);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void whenFailedIsCalled_ResultIsSuccessWithUnsupportedOperationException() throws Throwable {
        Try.from("foo")
                .failed()
                .orElseThrow();

    }

    @Test(expected = RuntimeException.class)
    public void whenFailedIsCalled_ResultIsSuccessWithFailure() throws Throwable {
        throw Try.from("foo")
                .map(s -> {
                    throw new RuntimeException("error");
                })
                .failed()
                .get();

    }

    @Test
    public void shouldFlatMapFailure() throws Exception {
        assertTrue(Try.failure(new RuntimeException())
                .flatMap(o -> Try.success("foo"))
                .isFailure());
    }

    @Test
    public void shouldFilterFailure() throws Exception {
        assertTrue(Try.failure(new RuntimeException())
                .filter(o -> true)
                .isFailure());
    }

    @Test
    public void shouldFilterFailureWithFailureMappingButUseOriginalFailure() throws Exception {
        final Throwable error = Try.failure(new RuntimeException())
                .filter(o -> true, o1 -> new IllegalStateException("error"))
                .getFailure();

        assertEquals(RuntimeException.class, error.getClass());

        final Throwable error2 = Try.failure(new RuntimeException())
                .filter(o -> true, () -> new IllegalStateException("error"))
                .getFailure();

        assertEquals(RuntimeException.class, error2.getClass());
    }

    @Test
    public void shouldDoNothingWhenForeachOnFailure() throws Exception {
        AtomicBoolean state = new AtomicBoolean(false);
        Try.failure(new RuntimeException()).foreach(e -> state.set(true));
        assertFalse(state.get());
    }

    @SuppressWarnings({"ThrowableResultOfMethodCallIgnored", "NumericOverflow"})
    @Test
    public void whenFromFutureAndFutureThrowsException_ReturnFailure() throws Throwable {
        Throwable error = Try.from(CompletableFuture.supplyAsync(() -> 5 / 0)).getFailure();
        assertEquals(ExecutionException.class, error.getClass());
    }

    @Test
    public void whenFromFutureIsSuccess() throws Exception {
        String actual = Try.from(CompletableFuture.supplyAsync(() -> "foo"))
                .get();

        assertEquals("foo", actual);
    }

    @Test
    public void whenIsFailure_IsFailureReturnsTrue() throws Exception {
        boolean actual = Try.from(() -> 5 / 0)
                .isFailure();

        assertTrue(actual);
    }

    @Test
    public void whenIsNotFailure_IsFailureReturnsFalse() throws Exception {
        boolean actual = Try.from(5)
                .isFailure();

        assertFalse(actual);
    }

    @Test
    public void whenIsSuccessToOptionalHasValue() throws Exception {
        String actual = Try.from("foo")
                .toOptional()
                .get();

        assertEquals("foo", actual);
    }

    @SuppressWarnings("NumericOverflow")
    @Test
    public void whenIsFailureToOptionalIsEmpty() throws Exception {
        boolean actual = Try.from(() -> 5 / 0)
                .toOptional()
                .isPresent();

        assertFalse(actual);
    }

    @Test(expected = RuntimeException.class)
    public void whenForeachCanThrowException() throws Exception {
        Try.from("foo")
                .foreach(s -> {
                    throw new RuntimeException(s);
                });
    }

    @Test
    public void whenMapAnyTransfromSuccess() throws Exception {
        Try<String> actual = Try.from("foo")
                .mapAny(s -> s + "bar", throwable -> throwable);

        assertEquals("foobar", actual.get());
    }

    @Test
    public void whenMapAnyFromSuccessAndThenThrowException_GetMappedException() throws Exception {
        Try<Integer> actual = Try.from("foo")
                .mapAny(s -> 5/0, throwable -> new RuntimeException("foo"));

        assertEquals(RuntimeException.class, actual.getFailure().getClass());
    }

    @Test
    public void whenMapAnyTransformFailure() throws Exception {
        Try<String> actual = Try.from(() -> 5/0)
                .mapAny(s -> s + "bar",
                        throwable -> new RuntimeException("div by zero"));
        assertEquals(RuntimeException.class, actual.getFailure().getClass());
    }

    @Test
    public void whenFromOptionalGetSuccess() throws Exception {
        Try<String> actual = Try.from(Optional.of("foo"))
                .map(s -> s + "bar");
        assertEquals("foobar", actual.get());
    }

    @Test(expected = NoSuchElementException.class)
    public void whenFromOptionalGetFailure() throws Throwable {
        Try<String> actual = Try.from(Optional.empty())
                .map(s -> s + "bar");
        throw actual.getFailure();
    }

    @Test
    public void whenSuccessToString() throws Exception {
        String actual = Try.from("foo")
                .toString();

        assertEquals("Success{foo}", actual);
    }

    @Test
    public void whenFailureToString() throws Exception {
        String actual = Try.from(() -> 5 / 0)
                .toString();
        assertEquals("Failure{java.lang.ArithmeticException: / by zero}", actual);
    }

    @Test
    public void whenIsFailureNotEqualsSuccess() throws Exception {
        boolean actual = Try.from(() -> 5 / 0)
                .equals(Try.success(0));
        assertFalse(actual);
    }

    @Test
    public void whenTwoSuccessesAreEqual() throws Exception {
        Try<Integer> first = Try.from(5);
        Try<Integer> second = Try.from(5);

        assertEquals(first, second);
    }

    @Test
    public void whenTwoSuccessesAreNotEqual() throws Exception {
        Try<Integer> first = Try.from(5);
        Try<Integer> second = Try.from(6);

        assertNotEquals(first, second);
    }

    @Test
    public void whenIsSuccessHashcodeIsCorrect() throws Exception {
        int actual1 = Try.from(1).hashCode();
        int actual2 = Try.from(2).hashCode();
        assertNotEquals(actual1, actual2);
    }

    @Test(expected = RuntimeException.class)
    public void whenIsFailureOrElseThrowMapsToRuntimeException() throws Exception {
        Integer actual = Try.from(() -> 5 / 0)
                .orElseThrow(RuntimeException::new);
    }

    @Test
    public void shouldReturnResultInOrElseIfFailure() throws Exception {
        final Object actual = Try.failure(new RuntimeException()).orElse("foo");
        assertEquals("foo", actual);

        final Object actual2 = Try.failure(new RuntimeException()).orElse(() -> "foo");
        assertEquals("foo", actual2);
    }

    @Test
    public void whenIsFailureOfRuntimeExceptionOrElseThrow_ThrowsItWithoutMapping() throws Exception {
        exception.expect(IllegalStateException.class);
        exception.expectMessage("foo");
        Try.from(() -> {throw new IllegalStateException("foo");})
                .orElseRuntimeThrow();
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();


    @Test
    public void whenIsFailureOrElseThrow_ThrowsRuntimeException() throws Exception {
        exception.expect(RuntimeException.class);
        Try.from(() -> {
            throw new IllegalAccessError();
        }).orElseRuntimeThrow();
    }

    @Test
    public void whenIsSuccessCastToSuperTypeIsOk() throws Exception {
        Object actual = Try.from("foo")
                .cast(Object.class)
                .get();

        assertEquals("foo", actual);
    }

    @Test
    public void whenCastFromSuperIsOk() throws Exception {
        List list = Try.from(new ArrayList<>())
                .map(objects -> {objects.add("foo"); return objects;})
                .cast(List.class)
                .get();

        assertEquals(1, list.size());
    }

    @Test
    public void whenCastIsWrongReturnFailure() throws Exception {
        exception.expect(IllegalStateException.class);
        Try.from("foo")
                .cast(Integer.class)
                .get();

    }

    @Test
    public void whenFilterHasFailureMapper() throws Exception {
        exception.expect(RuntimeException.class);
        exception.expectMessage("foo");
        Try.from("foo")
                .filter(s -> s.equals("bar"), (Func1<String, Throwable>) RuntimeException::new)
                .orElseRuntimeThrow();
    }

    @Test
    public void whenFromThrowableSupplierAndThrows_ReturnAFailure() throws Exception {
        Throwable actual = Try.fromThrowableSupplier(() -> getFooOrThrow(false))
                .getFailure();
        assertEquals("bar", actual.getMessage());
    }

    @Test
    public void whenFromThrowableSupplier_ReturnValue() throws Exception {
        String actual = Try.fromThrowableSupplier(() -> getFooOrThrow(true))
                .get();
        assertEquals("foo", actual);
    }

    @Test
    public void shouldNotBeCheckedWhenUsingUnchecked() throws Exception {
        int number = 0;
        String actual = Try.unchecked(() -> {
            if (number > 0) {
                throw new IOException("Error");
            } else return "foo";
        });
        assertEquals("foo", actual);
    }

    @Test
    public void shouldGetErrorInOrElse() throws Exception {
        String actual = Try.from("foo")
                .flatMap(s -> Try.<String>failure(new RuntimeException("Error: " + s)))
                .orElse(Throwable::getMessage);

        assertEquals("Error: foo", actual);
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowMappedRuntimeException() throws Exception {
        Try.failure(new IllegalArgumentException("e"))
                .orElseRuntimeThrow(RuntimeException::new);
    }

    @Test
    public void shouldGetSuccessInOrElse() throws Exception {
        String actual = Try.from("foo")
                .orElse(Throwable::getMessage);

        assertEquals("foo", actual);
    }

    @Test
    public void shouldConvertToOption() throws Exception {
        final String actual = Try.from("foo").toOption().get();
        assertEquals("foo", actual);
    }

    @Test
    public void shouldConvertFailureToOption() throws Exception {
        assertTrue(Try.failure(new RuntimeException("e")).toOption().isEmpty());
    }

    @Test(expected = RuntimeException.class)
    public void shouldRunAsUnchecked() {
        Try.doUnchecked(this::throwsSomeException);
    }

    @Test(expected = RuntimeException.class)
    public void shouldRunAsUncheckedWithLambda() {
        Try.doUnchecked(this::throwsSomeException);
    }

    @Test
    public void shouldComposeWithStreams() throws Exception {
        final List<String> list = Stream.<Try<String>>of(Try.from("foo"), Try.failure(new RuntimeException("error")))
                .flatMap(Try::toStream)
                .collect(toList());

        assertEquals(singletonList("foo"), list);
    }

    @Test
    public void shouldCallConsumerInCaseOfFailure() throws Exception {
        AtomicReference<Throwable> exception = new AtomicReference<>();
        Try.failure(new RuntimeException("error"))
                .ifFailure(exception::set);

        assertEquals(RuntimeException.class, exception.get().getClass());
    }

    @Test
    public void shouldFailureHaveEqualsAndHashcode() throws Exception {
        final RuntimeException exception = new RuntimeException("e");
        final Try<Object> left = Try.failure(exception);
        final Try<Object> right = Try.failure(exception);
        final Try<Object> notRight = Try.failure(new IllegalArgumentException());

        assertEquals(left, left);
        assertEquals(left, right);
        assertNotEquals(left, notRight);
        assertNotEquals(left, "foo");
        assertNotEquals(left, null);

        assertEquals(left.hashCode(), right.hashCode());
        assertNotEquals(left.hashCode(), notRight.hashCode());
    }

    @Test
    public void shouldSuccessHaveEqualsAndHashcode() throws Exception {
        final Try<String> left = Try.success("foo");
        final Try<String> right = Try.success("foo");
        final Try<String> notRight = Try.success("bar");

        assertEquals(left, left);
        assertEquals(left, right);
        assertNotEquals(left, notRight);
        assertNotEquals(left, "foo");
        assertNotEquals(left, null);

        assertEquals(left.hashCode(), right.hashCode());
        assertNotEquals(left.hashCode(), notRight.hashCode());
    }

    @Test
    public void shouldBeFailureWhenPeekConsumerThrows() throws Exception {
        assertTrue(Try.success("foo")
                .peek(s -> {throw new RuntimeException(s);})
                .isFailure());
    }

    @Test
    public void shouldSuccessForeach() throws Exception {
        AtomicReference<String> val = new AtomicReference<>();
        Try.success("foo")
                .foreach(val::set);
        assertEquals("foo", val.get());
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowWhenGetFailureFromSuccess() throws Exception {
        Try.success("foo").getFailure();
    }

    @Test
    public void shouldDoNothingWhenOrElse() throws Throwable {
        final Try<String> success = Try.success("foo");
        success.orElse(throwable -> {fail(); return "s";});
        assertEquals("foo", success.orElse("error"));
        assertEquals("foo", success.orElseRuntimeThrow());
        assertEquals("foo", success.orElseThrow(RuntimeException::new));
        success.orElseThrow();
        assertEquals("foo", success.orElseRuntimeThrow(RuntimeException::new));
    }

    @Test
    public void shouldDoNothingWhenIsSuccessAndIfFailure() throws Exception {
        assertEquals("foo", Try.success("foo").ifFailure(throwable -> fail()).get());
    }

    private void throwsSomeException() throws IllegalAccessException {
        throw new IllegalAccessException("haha");
    }

    private String getFooOrThrow(boolean isFoo) throws IOException {
        if (isFoo) {
            return "foo";
        } else {
            throw new IOException("bar");
        }
    }

    public void whenIsSuccessOrElseThrowReturnsValue() throws Exception {
        Integer actual = Try.from(() -> 5)
                .orElseThrow(RuntimeException::new);
        assertEquals(5, actual.intValue());
    }

}
