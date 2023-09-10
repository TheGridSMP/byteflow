package dev.drtheo.byteflow.asm;

import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class MixinClass {

    private final String mask;
    private final ClassNode self;

    public MixinClass(String mask, ClassNode self) {
        this.mask = mask;
        this.self = self;
    }

    public MixinClass(ClassNode self) {
        this(null, self);
    }

    public void apply(ClassNode node) {
        for (MethodNode method : node.methods) {

        }
    }
}
