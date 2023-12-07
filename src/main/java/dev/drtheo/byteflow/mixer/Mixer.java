package dev.drtheo.byteflow.mixer;

public interface Mixer {
    default void prepare() { }
    void mix(Class<?> clazz, byte[] bytes);
    default void finish() { }
}
