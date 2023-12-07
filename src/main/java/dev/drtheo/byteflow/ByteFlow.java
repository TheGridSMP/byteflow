package dev.drtheo.byteflow;

import dev.drtheo.byteflow.config.ConfigProvider;
import dev.drtheo.byteflow.mixin.MixinClass;
import dev.drtheo.byteflow.mixer.Mixer;
import dev.drtheo.byteflow.woid.ClazzPool;
import dev.drtheo.byteflow.woid.Clazz;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public class ByteFlow {

    private final Mixer mixer;
    private final Collection<ConfigProvider> configs;

    public ByteFlow(Supplier<Mixer> mixer, Collection<ConfigProvider> configs) {
        this.mixer = mixer.get();
        this.configs = configs;
    }

    public void apply() throws Exception {
        this.mixer.prepare();
        Set<Clazz> targets = new HashSet<>();
        ClazzPool pool = new ClazzPool();

        for (ConfigProvider config : this.configs) {
            config.forEachMixin(mixinName -> {
                MixinClass mixinClass = new MixinClass(pool, config.getClassLoader(), config.getId(), mixinName);

                targets.add(mixinClass.getTarget());
                mixinClass.patch();
            });
        }

        for (Clazz target : targets) {
            target.accept(new TraceClassVisitor(new PrintWriter(System.out)));
            this.mixer.mix(target.runtime(), target.toBytes());
        }

        this.mixer.finish();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final Set<ConfigProvider> configs = new HashSet<>();
        private Supplier<Mixer> mixer;

        public Builder withConfig(ConfigProvider config) {
            this.configs.add(config);
            return this;
        }

        public Builder withMixer(Supplier<Mixer> mixer) {
            this.mixer = mixer;
            return this;
        }

        public Optional<ByteFlow> build() {
            if (this.configs.isEmpty())
                return Optional.empty();

            if (this.mixer == null)
                return Optional.empty();

            return Optional.of(new ByteFlow(this.mixer, this.configs));
        }
    }
}