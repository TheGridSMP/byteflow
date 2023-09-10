package dev.drtheo.byteflow.asm.tree;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

public class ClassTree extends Tree<ClassNode> {

    public ClassTree(ClassNode node) {
        super(node);
    }

    public int getAccess() {
        return this.node.access;
    }

    public String getName() {
        return this.node.name;
    }

    public void addMethod(MethodNode node) {
        this.node.methods.add(node);
    }

    public void addField(FieldNode node) {
        this.node.fields.add(node);
    }

    public boolean isStatic() {
        return (this.node.access & Opcodes.ACC_STATIC) != 0;
    }
}
