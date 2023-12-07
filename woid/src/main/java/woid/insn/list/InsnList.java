package woid.insn.list;

import org.objectweb.asm.MethodVisitor;
import woid.insn.base.AbstractInsnNode;
import woid.insn.LabelNode;

import java.util.ListIterator;

public class InsnList implements Iterable<AbstractInsnNode> {

    /**
     * The number of instructions in this list.
     */
    private int size;

    /**
     * The first instruction in this list. May be {@literal null}.
     */
    private AbstractInsnNode first;

    /**
     * The last instruction in this list. May be {@literal null}.
     */
    private AbstractInsnNode last;

    /**
     * A cache of the instructions of this list. This cache is used to improve the performance of the
     * {@link #get} method.
     */
    private AbstractInsnNode[] cache;

    /**
     * Returns the number of instructions in this list.
     *
     * @return the number of instructions in this list.
     */
    public int size() {
        return size;
    }

    /**
     * Returns the first instruction in this list.
     *
     * @return the first instruction in this list, or {@literal null} if the list is empty.
     */
    public AbstractInsnNode getFirst() {
        return first;
    }

    /**
     * Returns the last instruction in this list.
     *
     * @return the last instruction in this list, or {@literal null} if the list is empty.
     */
    public AbstractInsnNode getLast() {
        return last;
    }

    public void recache() {
        if (this.cache == null)
            this.cache = this.toArray();
    }

    /**
     * Returns the instruction whose index is given. This method builds a cache of the instructions in
     * this list to avoid scanning the whole list each time it is called. Once the cache is built,
     * this method runs in constant time. This cache is invalidated by all the methods that modify the
     * list.
     *
     * @param index the index of the instruction that must be returned.
     * @return the instruction whose index is given.
     * @throws IndexOutOfBoundsException if (index &lt; 0 || index &gt;= size()).
     */
    public AbstractInsnNode get(int index) {
        if (index < 0 || index >= this.size)
            throw new IndexOutOfBoundsException();

        this.recache();
        return cache[index];
    }

    /**
     * Returns {@literal true} if the given instruction belongs to this list. This method always scans
     * the instructions of this list until it finds the given instruction or reaches the end of the
     * list.
     *
     * @param node an instruction.
     * @return {@literal true} if the given instruction belongs to this list.
     */
    public boolean contains(AbstractInsnNode node) {
        this.recache();
        AbstractInsnNode current = this.first;

        while (current != null && current != node) {
            current = current.getNext();
        }

        return current != null;
    }

    /**
     * Returns the index of the given instruction in this list. This method builds a cache of the
     * instruction indexes to avoid scanning the whole list each time it is called. Once the cache is
     * built, this method run in constant time. The cache is invalidated by all the methods that
     * modify the list.
     *
     * @param insnNode an instruction <i>of this list</i>.
     * @return the index of the given instruction in this list. <i>The result of this method is
     * undefined if the given instruction does not belong to this list</i>. Use {@link #contains }
     * to test if an instruction belongs to an instruction list or not.
     */
    public int indexOf(final AbstractInsnNode insnNode) {
        this.recache();
        return insnNode.getIndex();
    }

    /**
     * Makes the given visitor visit all the instructions in this list.
     *
     * @param visitor the method visitor that must visit the instructions.
     */
    public void accept(MethodVisitor visitor) {
        AbstractInsnNode current = this.first;

        while (current != null) {
            current.accept(visitor);
            current = current.getNext();
        }
    }

    /**
     * Returns an iterator over the instructions in this list.
     *
     * @return an iterator over the instructions in this list.
     */
    @Override
    @SuppressWarnings("unchecked")
    public ListIterator<AbstractInsnNode> iterator() {
        return new InsnListIterator(this, 0);
    }

    /**
     * Returns an array containing all the instructions in this list.
     *
     * @return an array containing all the instructions in this list.
     */
    public AbstractInsnNode[] toArray() {
        AbstractInsnNode current = this.first;
        AbstractInsnNode[] array = new AbstractInsnNode[this.size];

        int index = 0;
        while (current != null) {
            array[index] = current;

            current.setIndex(index++);
            current = current.getNext();
        }

        return array;
    }

    /**
     * Replaces an instruction of this list with another instruction.
     *
     * @param oldNode an instruction <i>of this list</i>.
     * @param newNode another instruction, <i>which must not belong to any {@link InsnList}</i>.
     */
    public void set(AbstractInsnNode oldNode, final AbstractInsnNode newNode) {
        AbstractInsnNode next = oldNode.getNext();
        AbstractInsnNode previous = oldNode.getPrevious();

        newNode.setNext(next);
        newNode.setPrevious(previous);

        if (next != null) {
            next.setPrevious(newNode);
        } else {
            this.last = newNode;
        }

        if (previous != null) {
            previous.setNext(newNode);
        } else {
            this.first = newNode;
        }

        if (this.cache != null) {
            int index = oldNode.getIndex();
            this.cache[index] = newNode;
            newNode.setIndex(index);
        } else {
            newNode.setIndex(0);
        }

        oldNode.invalidate();
    }

    /**
     * Adds the given instruction to the end of this list.
     *
     * @param node an instruction, <i>which must not belong to any {@link InsnList}</i>.
     */
    public void add(AbstractInsnNode node) {
        this.size++;

        if (this.last == null) {
            this.first = node;
        } else {
            this.last.setNext(node);
            node.setPrevious(this.last);
        }

        this.last = node;
        this.cache = null;

        node.setIndex(0);
    }

    /**
     * Adds the given instructions to the end of this list.
     *
     * @param other an instruction list, which is cleared during the process. This list must be
     *                 different from 'this'.
     */
    public void add(InsnList other) {
        if (other.isEmpty())
            return;

        this.size += other.size;

        if (this.last == null) {
            this.first = other.first;
        } else {
            AbstractInsnNode first = other.first;
            this.last.setNext(first);

            first.setPrevious(this.last);
        }

        this.last = other.last;
        this.cache = null;

        other.clear();
    }

    /**
     * Inserts the given instruction at the beginning of this list.
     *
     * @param node an instruction, <i>which must not belong to any {@link InsnList}</i>.
     */
    public void insert(AbstractInsnNode node) {
        this.size++;

        if (this.first == null) {
            this.last = node;
        } else {
            this.first.setPrevious(node);
            node.setNext(this.first);
        }

        this.first = node;
        this.cache = null;

        node.setIndex(0); // insnNode now belongs to an InsnList.
    }

    /**
     * Inserts the given instructions at the beginning of this list.
     *
     * @param other an instruction list, which is cleared during the process. This list must be
     *                 different from 'this'.
     */
    public void insert(InsnList other) {
        if (other.isEmpty())
            return;

        this.size += other.size;

        if (this.first == null) {
            this.last = other.last;
        } else {
            AbstractInsnNode last = other.last;
            this.first.setPrevious(last);

            last.setNext(this.first);
        }

        this.first = other.first;
        this.cache = null;

        other.clear();
    }

    /**
     * Inserts the given instruction after the specified instruction.
     *
     * @param previous an instruction <i>of this list</i> after which insnNode must be inserted.
     * @param node     the instruction to be inserted, <i>which must not belong to any {@link
     *                     InsnList}</i>.
     */
    public void insert(AbstractInsnNode previous, AbstractInsnNode node) {
        this.size++;
        AbstractInsnNode next = previous.getNext();

        if (next == null) {
            this.last = node;
        } else {
            next.setPrevious(node);
        }

        node.setNext(next);
        node.setPrevious(previous);
        previous.setNext(node);

        this.cache = null;
        node.setIndex(0);
    }

    /**
     * Inserts the given instructions after the specified instruction.
     *
     * @param previous an instruction <i>of this list</i> after which the instructions must be
     *                     inserted.
     * @param other     the instruction list to be inserted, which is cleared during the process. This
     *                     list must be different from 'this'.
     */
    public void insert(AbstractInsnNode previous, InsnList other) {
        if (other.isEmpty())
            return;

        this.size += other.size;

        AbstractInsnNode first = other.first;
        AbstractInsnNode last = other.last;

        AbstractInsnNode next = previous.getNext();

        if (next == null) {
            this.last = last;
        } else {
            next.setPrevious(last);
        }

        last.setNext(next);
        first.setPrevious(previous);
        previous.setNext(first);

        this.cache = null;
        other.clear();
    }

    /**
     * Inserts the given instruction before the specified instruction.
     *
     * @param next an instruction <i>of this list</i> before which insnNode must be inserted.
     * @param node the instruction to be inserted, <i>which must not belong to any {@link
     *                 InsnList}</i>.
     */
    public void insertBefore(AbstractInsnNode next, AbstractInsnNode node) {
        this.size++;
        AbstractInsnNode previous = next.getPrevious();

        if (previous == null) {
            this.first = node;
        } else {
            previous.setNext(node);
        }

        node.setPrevious(previous);
        next.setPrevious(node);
        node.setNext(next);

        this.cache = null;
        node.setIndex(0);
    }

    /**
     * Inserts the given instructions before the specified instruction.
     *
     * @param next an instruction <i>of this list</i> before which the instructions must be
     *                 inserted.
     * @param other the instruction list to be inserted, which is cleared during the process. This
     *                 list must be different from 'this'.
     */
    public void insertBefore(AbstractInsnNode next, InsnList other) {
        if (other.isEmpty())
            return;

        this.size += other.size;
        AbstractInsnNode first = other.first;
        AbstractInsnNode last = other.last;

        AbstractInsnNode previous = next.getPrevious();

        if (previous == null) {
            this.first = first;
        } else {
            previous.setNext(first);
        }

        next.setPrevious(last);

        last.setNext(next);
        first.setPrevious(previous);

        this.cache = null;
        other.clear();
    }

    /**
     * Removes the given instruction from this list.
     *
     * @param node the instruction <i>of this list</i> that must be removed.
     */
    public void remove(AbstractInsnNode node) {
        this.size--;

        AbstractInsnNode next = node.getNext();
        AbstractInsnNode previous = node.getPrevious();

        if (next == null) {
            if (previous == null) {
                this.first = null;
                this.last = null;
            } else {
                previous.setNext(null);
                this.last = previous;
            }
        } else {
            if (previous == null) {
                this.first = next;
                next.setPrevious(null);
            } else {
                previous.setNext(next);
                next.setPrevious(previous);
            }
        }

        this.cache = null;
        node.invalidate();
    }

    public void overwrite(InsnList other) {
        this.clear();
        this.add(other);
    }

    /**
     * Removes all the instructions of this list.
     */
    public void clear() {
        this.size = 0;
        this.first = null;
        this.last = null;
        this.cache = null;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    /**
     * Resets all the labels in the instruction list. This method should be called before reusing an
     * instruction list between several <code>ClassWriter</code>s.
     */
    public void resetLabels() {
        AbstractInsnNode current = this.first;

        while (current != null) {
            if (current instanceof LabelNode node) {
                node.resetLabel();
            }

            current = current.getNext();
        }
    }
}
