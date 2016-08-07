package net.xfolch.dtech.vendingmachine.domain.model;

import java.text.MessageFormat;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Represents a computation that either can go well or can go wrong.
 * <p>
 * This abstraction provides some flexibility to manage different failure scenarios.
 * <p>
 * Created by xfolch on 7/8/16.
 */
public abstract class Try<T> {

    private Try() {
    }

    public static <T> Try<T> that(Callable<T> callable) {
        try {
            return new Success<>(callable.call());
        } catch (Exception e) {
            return new Failure<>(e);
        }
    }

    public static <T> Try<T> success(T value) {
        return new Success<>(value);
    }

    public static <T> Try<T> failure(Exception e) {
        return new Failure<>(e);
    }

    public abstract T get(java.util.function.Function<Exception, T> errorHandler);

    public abstract T getOrThrowRuntimeException();

    public abstract T getOrThrowException() throws Exception;

    public abstract boolean isFailure();

    public abstract boolean isSuccess();

    public abstract Optional<T> toOptional();

    public abstract <R> Try<R> map(Function<? super T, ? extends R> mapping);

    public abstract <R> Try<R> flatMap(Function<? super T, ? extends Try<? extends R>> mapping);

    public abstract Try<T> filter(Predicate<T> filter, Supplier<Exception> exceptionSupplier);

    public abstract Try<T> recoverWith(java.util.function.Function<Exception, Try<T>> recover);

    private static final class Success<T> extends Try<T> {
        final T value;

        Success(T value) {
            this.value = value;
        }

        @Override
        public T get(java.util.function.Function<Exception, T> errorHandler) {
            return value;
        }

        @Override
        public T getOrThrowRuntimeException() {
            return value;
        }

        @Override
        public T getOrThrowException() throws Exception {
            return value;
        }

        @Override
        public boolean isFailure() {
            return false;
        }

        @Override
        public boolean isSuccess() {
            return true;
        }

        @Override
        public Optional<T> toOptional() {
            return Optional.ofNullable(value);
        }

        @Override
        public <R> Try<R> map(Function<? super T, ? extends R> mapping) {
            return map(value, mapping);
        }

        @Override
        public <R> Try<R> flatMap(Function<? super T, ? extends Try<? extends R>> mapping) {
            return flat(map(value, mapping));
        }

        @Override
        public Try<T> filter(Predicate<T> filter, Supplier<Exception> exceptionSupplier) {
            return filter.test(value)
                    ? this
                    : Try.failure(exceptionSupplier.get());
        }

        @Override
        public Try<T> recoverWith(java.util.function.Function<Exception, Try<T>> recover) {
            return this;
        }

        private static <T, R> Try<R> map(T value, Function<? super T, ? extends R> mapping) {
            return Try.that(() -> mapping.apply(value));
        }

        private static <R> Try<R> flat(Try<? extends Try<? extends R>> mapping) {
            return Try.that(() -> mapping.getOrThrowException().getOrThrowException());
        }

        @Override
        public String toString() {
            return "Success(" + value + ")";
        }
    }

    private static final class Failure<T> extends Try<T> {
        final Exception e;

        Failure(Exception e) {
            this.e = e;
        }

        @Override
        public T get(java.util.function.Function<Exception, T> errorHandler) {
            return errorHandler.apply(e);
        }

        @Override
        public T getOrThrowRuntimeException() {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }

            throw new RuntimeException(e.getMessage(), e);
        }

        @Override
        public T getOrThrowException() throws Exception {
            throw e;
        }

        @Override
        public boolean isFailure() {
            return true;
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public Optional<T> toOptional() {
            return Optional.empty();
        }

        @SuppressWarnings("unchecked")
        @Override
        public <R> Try<R> map(Function<? super T, ? extends R> mapping) {
            return (Try<R>) this;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <R> Try<R> flatMap(Function<? super T, ? extends Try<? extends R>> mapping) {
            return (Try<R>) this;
        }

        @Override
        public Try<T> filter(Predicate<T> filter, Supplier<Exception> exceptionSupplier) {
            return this;
        }

        @Override
        public Try<T> recoverWith(java.util.function.Function<Exception, Try<T>> recover) {
            return recover.apply(e);
        }

        @Override
        public String toString() {
            return MessageFormat.format("Failure({0}(\"{1}\"))",
                    e.getClass().getSimpleName(),
                    e.getMessage());
        }
    }

    @FunctionalInterface
    public interface Function<S, D> {
        D apply(S source) throws Exception;
    }

}
