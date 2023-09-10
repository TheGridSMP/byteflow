package dev.drtheo.byteflow.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ASM {

    public static ClassNode getClass(String path) throws IOException {
        ClassReader reader = new ClassReader(path);
        ClassNode node = new ClassNode();

        reader.accept(node, ClassReader.EXPAND_FRAMES);
        return node;
    }

    public static byte[] toBytes(ClassNode node) {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        node.accept(classWriter);

        return classWriter.toByteArray();
    }

    public static byte[] apply(String path, BiConsumer<ClassReader, ClassWriter> consumer) throws IOException {
        ClassReader reader = new ClassReader(path);
        ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES);

        consumer.accept(reader, writer);
        return writer.toByteArray();
    }
}
