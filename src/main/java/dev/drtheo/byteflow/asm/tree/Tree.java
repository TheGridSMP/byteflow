package dev.drtheo.byteflow.asm.tree;

public abstract class Tree<T> {

    protected final T node;

    public Tree(T node) {
        this.node = node;
    }
}
