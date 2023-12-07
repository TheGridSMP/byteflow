package dev.drtheo.byteflow.woid;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import woid.clazz.ClassNode;

public class Clazz extends ClassNode {

    private final Class<?> representation;

    public Clazz(Class<?> clazz) {
        super(Opcodes.ASM9);

        this.representation = clazz;
    }

    public Clazz(ClassLoader loader, String path, boolean load) throws ClassNotFoundException {
        this(Class.forName(path, load, loader));
    }

    public Class<?> runtime() {
        return representation;
    }

    public byte[] toBytes() {
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        this.accept(writer);

        return writer.toByteArray();
    }
}
