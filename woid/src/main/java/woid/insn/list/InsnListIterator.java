package woid.insn.list;

import woid.insn.base.AbstractInsnNode;

import java.util.ListIterator;
import java.util.NoSuchElementException;

// Note: this class is not generified because it would create bridges.
@SuppressWarnings("rawtypes")
public class InsnListIterator implements ListIterator {

    private AbstractInsnNode nextInsn;
    private AbstractInsnNode previousInsn;
    private AbstractInsnNode remove;

    private final InsnList list;

    public InsnListIterator(InsnList list, int index) {
        this.list = list;

        if (index < 0 || index > this.list.size())
            throw new IndexOutOfBoundsException();

        if (index == this.list.size()) {
            this.previousInsn = this.list.getLast();
            this.nextInsn = null;
            return;
        }

        AbstractInsnNode current = this.list.getFirst();

        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }

        this.nextInsn = current;
        this.previousInsn = current.getPrevious();
    }

    @Override
    public boolean hasNext() {
        return nextInsn != null;
    }

    @Override
    public Object next() {
        if (this.nextInsn == null)
            throw new NoSuchElementException();

        AbstractInsnNode result = this.nextInsn;

        this.previousInsn = result;
        this.nextInsn = result.getNext();
        this.remove = result;

        return result;
    }

    @Override
    public void remove() {
        if (this.remove == null)
            throw new IllegalStateException();

        if (this.remove == this.nextInsn) {
            this.nextInsn = this.nextInsn.getNext();
        } else {
            this.previousInsn = this.previousInsn.getPrevious();
        }

        this.list.remove(this.remove);
        this.remove = null;
    }

    @Override
    public boolean hasPrevious() {
        return previousInsn != null;
    }

    @Override
    public Object previous() {
        if (this.previousInsn == null)
            throw new NoSuchElementException();


        AbstractInsnNode result = this.previousInsn;

        this.nextInsn = result;
        this.previousInsn = result.getPrevious();
        this.remove = result;

        return result;
    }

    @Override
    public int nextIndex() {
        if (this.nextInsn == null)
            return this.list.size();

        this.list.recache();
        return this.nextInsn.getIndex();
    }

    @Override
    public int previousIndex() {
        if (this.previousInsn == null)
            return -1;

        this.list.recache();
        return this.previousInsn.getIndex();
    }

    @Override
    public void add(Object o) {
        if (this.nextInsn != null) {
            this.list.insertBefore(this.nextInsn, (AbstractInsnNode) o);
        } else if (this.previousInsn != null) {
            this.list.insert(this.previousInsn, (AbstractInsnNode) o);
        } else {
            this.list.add((AbstractInsnNode) o);
        }

        this.previousInsn = (AbstractInsnNode) o;
        this.remove = null;
    }

    @Override
    public void set(final Object o) {
        if (this.remove == null)
            throw new IllegalStateException();

        this.list.set(this.remove, (AbstractInsnNode) o);

        if (this.remove == this.previousInsn) {
            this.previousInsn = (AbstractInsnNode) o;
        } else {
            this.nextInsn = (AbstractInsnNode) o;
        }
    }
}
