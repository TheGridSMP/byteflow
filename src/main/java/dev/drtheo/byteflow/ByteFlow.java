package dev.drtheo.byteflow;

import org.spongepowered.asm.mixin.Mixin;
import dev.drtheo.byteflow.asm.ASM;
import dev.drtheo.byteflow.asm.MixinClass;
import dev.drtheo.byteflow.config.ConfigProvider;
import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;
import java.util.*;

public class ByteFlow {

    private final ClassLoader loader;
    private final ConfigProvider config;

    public ByteFlow(ClassLoader loader, ConfigProvider config) {
        this.loader = loader;
        this.config = config;
    }

    public String getId() {
        return this.config.getId();
    }

    public void apply() throws ClassNotFoundException, IOException {
        String pkg = this.config.getPackage();
        String[] mixins = this.config.getMixins();

        Map<String, ClassNode> nodeMap = new HashMap<>();
        Map<String, Set<MixinClass>> mixinMap = new HashMap<>();

        for (String mixinPath : mixins) {
            mixinPath = pkg + "." + mixinPath;

            Class<?> mixinClass = this.loader.loadClass(mixinPath);
            Mixin mixin = mixinClass.getAnnotation(Mixin.class);

            String target = mixin.value();
            nodeMap.put(mixin.value(), ASM.getClass(target));

            if (!mixinMap.containsKey(target))
                mixinMap.put(target, new HashSet<>());

            mixinMap.get(target).add(new MixinClass(ASM.getClass(mixinPath)));
        }

        for (String target : mixinMap.keySet()) {
            ClassNode node = nodeMap.get(target);
            for (MixinClass mixin : mixinMap.get(target)) {
                mixin.apply(node);
            }
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    static class Builder {

        private ClassLoader loader;
        private ConfigProvider config;

        public Builder withLoader(ClassLoader loader) {
            if (ClassLoader.getSystemClassLoader().getClass().isInstance(loader))
                throw new IllegalArgumentException("ClassLoader cannot be SystemClassLoader!");

            this.loader = loader;
            return this;
        }

        public Builder withConfig(ConfigProvider config) {
            this.config = config;
            return this;
        }

        public Optional<ByteFlow> build() {
            if (this.loader == null || this.config == null)
                return Optional.empty();

            return Optional.of(new ByteFlow(this.loader, this.config));
        }
    }
}