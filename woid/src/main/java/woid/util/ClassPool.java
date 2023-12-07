package woid.util;

import woid.clazz.ClassNode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class ClassPool<T extends ClassNode> {

    protected final Map<String, T> lookup = new HashMap<>();

    protected abstract T createAndRead(ClassLoader loader, String name, boolean load) throws IOException, ClassNotFoundException;

    public T readClass(ClassLoader loader, String name, boolean load) throws ClassNotFoundException {
        T result = this.lookup.get(name);

        if (result == null) {
            try {
                this.lookup.put(name, this.createAndRead(loader, name, load));
                return this.readClass(loader, name, load);
            } catch (IOException e) {
                System.err.println("Couldn't read class " + name);
            }
        }

        return result;
    }

    public T readClass(ClassLoader loader, String name) throws ClassNotFoundException {
        return this.readClass(loader, name, false);
    }
}
