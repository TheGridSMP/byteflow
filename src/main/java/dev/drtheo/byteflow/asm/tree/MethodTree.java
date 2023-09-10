package dev.drtheo.byteflow.asm.tree;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

public class MethodTree extends Tree<MethodNode> {

    public MethodTree(MethodNode node) {
        super(node);
    }

    public int getAccess() {
        return this.node.access;
    }

    public String getName() {
        return this.node.name;
    }

    public String getDescriptor() {
        return this.node.desc;
    }

    public boolean isStatic() {
        return (this.node.access & Opcodes.ACC_STATIC) != 0;
    }

    public InsnList getInstructions() {
        return this.node.instructions;
    }

    public boolean hasAnnotation(Class<?> annotation) {
        String descriptor = Type.getDescriptor(annotation);
        for (AnnotationNode annotationNode : this.node.visibleAnnotations) {
            if (annotationNode.desc.equals(descriptor))
                return true;
        }

        return false;
    }
}
