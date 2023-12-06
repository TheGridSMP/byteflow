package dev.drtheo.byteflow;

import dev.drtheo.byteflow.config.ConfigProvider;
import dev.drtheo.byteflow.meta.MixinClass;
import javassist.ClassPool;
import javassist.LoaderClassPath;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ByteFlow {

    private final ConfigProvider[] configs;

    public ByteFlow(ConfigProvider[] configs) {
        this.configs = configs;
    }

    public void apply() throws Exception {
        ClassPool pool = ClassPool.getDefault();
        pool.appendSystemPath();

        for (ConfigProvider config : this.configs) {
            pool.appendClassPath(new LoaderClassPath(config.getClassLoader()));

            for (String mixinName : config.getMixins()) {
                new MixinClass(config.getId(), pool, config.getPackage() + "." + mixinName).patch();
            }
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final Set<ConfigProvider> configs = new HashSet<>();

        public Builder withConfig(ConfigProvider config) {
            this.configs.add(config);
            return this;
        }

        public Optional<ByteFlow> build() {
            if (this.configs.isEmpty())
                return Optional.empty();

            return Optional.of(new ByteFlow(this.configs.toArray(new ConfigProvider[0])));
        }
    }
}