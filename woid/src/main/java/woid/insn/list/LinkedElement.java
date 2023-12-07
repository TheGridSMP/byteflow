package woid.insn.list;

public class LinkedElement<T> {

    private int index = -1;

    private T next;
    private T previous;

    public int getIndex() {
        return index;
    }

    public T getNext() {
        return next;
    }

    public T getPrevious() {
        return previous;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setNext(T next) {
        this.next = next;
    }

    public void setPrevious(T previous) {
        this.previous = previous;
    }

    public void invalidate() {
        this.index = -1;
        this.previous = null;
        this.next = null;
    }
}
