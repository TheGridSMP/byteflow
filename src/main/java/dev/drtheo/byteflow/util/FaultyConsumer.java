package dev.drtheo.byteflow.util;

@FunctionalInterface
public interface FaultyConsumer<T> {
    void accept(T t) throws Exception;
}
