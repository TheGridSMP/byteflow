package dev.drtheo.byteflow.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;

public abstract class ConfigProvider {

    protected final String id;
    protected final ClassLoader classLoader;

    protected final String pkg;
    protected final String[] mixins;

    public ConfigProvider(File file, ClassLoader loader) throws IOException {
        this(file.getName().split("\\.")[0], loader, new FileInputStream(file));
    }

    protected ConfigProvider(String id, ClassLoader loader, InputStream stream) {
        throw new UnsupportedOperationException();
    }

    protected ConfigProvider(String id, ClassLoader loader, Supplier<ConfigData> data) {
        this.id = id;
        this.classLoader = loader;
        ConfigData config = data.get();

        this.pkg = config.pkg();
        this.mixins = config.mixins();
    }

    public String getId() {
        return this.id;
    }

    public ClassLoader getClassLoader() {
        return this.classLoader;
    }

    public String getPackage() {
        return this.pkg;
    }

    public String[] getMixins() {
        return this.mixins;
    }
}
