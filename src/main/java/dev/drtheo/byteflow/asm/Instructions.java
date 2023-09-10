package dev.drtheo.byteflow.asm;

import org.objectweb.asm.MethodVisitor;

import java.util.HashSet;
import java.util.function.Consumer;

public class Instructions extends HashSet<Consumer<MethodVisitor>> {

    public void apply(MethodVisitor visitor) {
        for (Consumer<MethodVisitor> consumer : this) {
            consumer.accept(visitor);
        }
    }
}
