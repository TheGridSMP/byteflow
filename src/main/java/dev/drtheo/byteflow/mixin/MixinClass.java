package dev.drtheo.byteflow.mixin;

import dev.drtheo.byteflow.annotation.Mixin;
import dev.drtheo.byteflow.annotation.Overwrite;
import dev.drtheo.byteflow.annotation.Shadow;
import dev.drtheo.byteflow.annotation.injection.Inject;
import dev.drtheo.byteflow.mixin.method.CloneMixinMethod;
import dev.drtheo.byteflow.mixin.method.InjectMixinMethod;
import dev.drtheo.byteflow.mixin.method.OverwriteMixinMethod;
import dev.drtheo.byteflow.mixin.method.ShadowMixinMethod;
import dev.drtheo.byteflow.mixin.method.base.MixinMethod;
import dev.drtheo.byteflow.woid.ClazzPool;
import dev.drtheo.byteflow.woid.Clazz;
import woid.method.MethodNode;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class MixinClass {

    private final String id;
    private final Clazz source;
    private final Clazz target;

    public MixinClass(ClazzPool pool, ClassLoader loader, String id, String path) throws ClassNotFoundException {
        this.id = id;
        this.source = pool.readClass(loader, path);

        Mixin mixin = this.source.runtime().getAnnotation(Mixin.class);
        this.target = pool.readClass(loader, mixin.value());
    }

    public void patch() throws Exception {
        for (Method method : this.source.runtime().getDeclaredMethods()) {
            this.matchMethod(method).patch();
        }
    }

    private MixinMethod<?> matchMethod(Method method) {
        MethodNode node = this.source.getMethod(method);

        for (Annotation annotation : method.getDeclaredAnnotations()) {
            if (annotation instanceof Shadow shadow)
                return new ShadowMixinMethod(this, node, shadow);

            if (annotation instanceof Inject inject)
                return new InjectMixinMethod(this, node, method, inject);

            if (annotation instanceof Overwrite overwrite)
                return new OverwriteMixinMethod(this, node, overwrite);
        }

        return new CloneMixinMethod(this, node, null);
    }

    public String getId() {
        return this.id;
    }

    public Clazz getSource() {
        return this.source;
    }

    public Clazz getTarget() {
        return this.target;
    }
}
