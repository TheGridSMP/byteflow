package dev.drtheo.byteflow.meta;

@FunctionalInterface
public interface FaultySupplier<T, E extends Exception> {
    T get() throws E;
}
