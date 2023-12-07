package dev.drtheo.byteflow.woid;

import org.objectweb.asm.ClassReader;
import woid.util.ClassPool;

import java.io.IOException;
import java.io.InputStream;

public class ClazzPool extends ClassPool<Clazz> {

    @Override
    protected Clazz createAndRead(ClassLoader loader, String name, boolean load) throws IOException, ClassNotFoundException {
        Clazz transformerNode = new Clazz(loader, name, load);
        Class<?> clazz = transformerNode.runtime();

        try (InputStream stream = clazz.getResource(clazz.getSimpleName() + ".class").openStream()) {
            new ClassReader(stream).accept(transformerNode, 0);
        }

        return transformerNode;
    }
}
