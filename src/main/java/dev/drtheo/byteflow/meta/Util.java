package dev.drtheo.byteflow.meta;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import org.objectweb.asm.ClassReader;
import woid.ClassNode;

import java.io.IOException;

public class Util {

    public static ClassNode clazz(String name) throws IOException {
        ClassReader reader = new ClassReader(name);
        ClassNode node = new ClassNode();

        reader.accept(node, 0);
        return node;
    }

    public static <T> T exception(FaultySupplier<T, NotFoundException> supplier) throws ClassNotFoundException {
        try {
            return supplier.get();
        } catch (NotFoundException e) {
            throw new ClassNotFoundException("Couldn't load Mixin: " + e);
        }
    }

    public static String shadowMethod(CtClass target, CtMethod method, String id) throws CannotCompileException {
        method.setName(id + "$" + method.getName());
        target.addMethod(method);

        return method.getName();
    }
}
