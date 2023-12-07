package dev.drtheo.byteflow.commons.mixer;

import dev.drtheo.byteflow.commons.util.TransformQuery;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Map;

public class MixinClassTransformer implements ClassFileTransformer {

    private final AgentMixer mixer;
    private final Map<Class<?>, TransformQuery> transformations = new HashMap<>();

    private boolean initialized = false;

    public MixinClassTransformer(AgentMixer mixer) {
        this.mixer = mixer;
    }

    public void addQuery(TransformQuery query) {
        if (!this.initialized) {
            this.initialized = true;
        }

        this.transformations.put(query.clazz(), query);
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] bytes) {
        if (this.initialized && this.transformations.isEmpty()) {
            this.mixer.markFinished();
            return null;
        }

        TransformQuery query = this.transformations.remove(classBeingRedefined);

        if (query != null) {
            System.out.println("Transforming " + query.clazz().getName());
            return query.bytes();
        }

        return null;
    }
}
