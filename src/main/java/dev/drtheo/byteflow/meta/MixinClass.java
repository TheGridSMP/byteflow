package dev.drtheo.byteflow.meta;

import dev.drtheo.byteflow.annotation.Mixin;
import dev.drtheo.byteflow.annotation.Shadow;
import dev.drtheo.byteflow.annotation.injection.Inject;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

public class MixinClass implements Patchable {

    private final String id;
    private final CtClass source;
    private final CtClass target;

    public MixinClass(String id, ClassPool pool, String path) throws ClassNotFoundException {
        this.id = id;
        this.source = Util.exception(() -> pool.get(path));

        Mixin mixin = (Mixin) this.source.getAnnotation(Mixin.class);
        this.target = Util.exception(() -> pool.get(mixin.value()));
    }

    @Override
    public void patch() throws Exception {
        for (CtMethod method : this.target.getMethods()) {
            this.matchMethod(method).patch();
        }
    }

    private MixinMethod<?> matchMethod(CtMethod method) throws ClassNotFoundException {
        for (Object annotation : method.getAnnotations()) {
            if (annotation instanceof Shadow shadow)
                return new ShadowMixinMethod(this, method, shadow);

            if (annotation instanceof Inject inject) {
                return Util.exception(() -> new InjectMixinMethod(this, method, inject));
            }
        }

        return new CloneMixinMethod(this, method, null);
    }

    public String getId() {
        return this.id;
    }

    public CtClass getSource() {
        return this.source;
    }

    public CtClass getTarget() {
        return this.target;
    }
}
