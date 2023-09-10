package dev.drtheo.byteflow.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;

public abstract class ConfigProvider {

    protected final String id;
    protected final ConfigData data;

    protected String pkg;
    protected String[] mixins;

    public ConfigProvider(File file) throws IOException {
        this(file.getName().split("\\.")[0], new FileInputStream(file));
    }

    protected ConfigProvider(String id, InputStream stream) {
        throw new UnsupportedOperationException();
    }

    protected ConfigProvider(String id, Supplier<ConfigData> data) {
        this.id = id;
        this.data = data.get();
    }

    public String getId() {
        return this.id;
    }

    public String getPackage() {
        return this.pkg;
    }

    public String[] getMixins() {
        return this.mixins;
    }
}
