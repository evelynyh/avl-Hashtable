package datastructures.worklists;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.FixedSizeFIFOWorkList;
import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/worklists/FixedSizeFIFOWorkList.java
 * for method specifications.
 */
public class CircularArrayFIFOQueue<E extends Comparable<E>> extends FixedSizeFIFOWorkList<E> {
    int size;
    E[] queue;

    int back;
    int front;
    public CircularArrayFIFOQueue(int capacity) {
        super(capacity);
        size = 0;
        queue = (E[])new Comparable[capacity];
        front = 0;
        back = 0;
    }

    @Override
    public void add(E work) {
        if (isFull()) {
            throw new IllegalStateException();
        }
        queue[back] = work; // add to back
        if ((back +1 ) % queue.length == 0) {
            back = 0;
        } else {
            back++;
        }
        size++;
    }

    @Override
    public E peek() {
        if (!hasWork()) {
            throw new NoSuchElementException();
        }

        if (size == 0) {
            throw new NoSuchElementException();
        }
        return (E) queue[front];
    }

    @Override
    public E peek(int i) {
        if (!hasWork()) {
            throw new NoSuchElementException();
        }

        if (i < 0 || i >= size) {
            throw new IndexOutOfBoundsException();
        }

        return (E) queue[(front + i) % queue.length];
    }

    @Override
    public E next() {
        if (queue[front] == null) {
            throw new NoSuchElementException();
        }
        E val = (E) queue[front];
        queue[front] = null;
        if ((front + 1) % (queue.length) == 0) {
            front = 0;
        } else {
            front++;
        }
        size--;
        return val;
    }

    @Override
    public void update(int i, E value) {
        if (!hasWork()) {
            throw new NoSuchElementException();
        }

        if (i < 0 || i >= size) {
            throw new IndexOutOfBoundsException();
        }

        queue[(front + i) % queue.length] = value;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        queue = (E[])new Comparable[queue.length];
        size = 0;
        front = 0;
        back = 0;
    }

    @Override
    public int compareTo(FixedSizeFIFOWorkList<E> other) {
        //this.compareTo(other)

        for (int i = 0; i < Math.min(this.size, other.size()); i++) {
            if (!this.peek(i).equals(other.peek(i))) { //compares attributes
                return this.peek(i).compareTo(other.peek(i));
            }
        }
        if (this.size != other.size()) {
            return this.size-other.size();
        }

        return 0;  // similar to equals
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof FixedSizeFIFOWorkList<?>)) {
            return false;
        } else {
            FixedSizeFIFOWorkList<E> other = (FixedSizeFIFOWorkList<E>) obj;

            if (this.size != other.size()) {
                return false;
            }

            for (int i = 0; i < size; i++) {
                if (!this.peek(i).equals(other.peek(i))) { //compares attributes
                    return false; //if 1 thing is false, break and return false
                }
            }
            return true;
        }
    }

    @Override
    public int hashCode() {

        //check if equal. if so, print out same hash value
        //returns hash code int value for the given objects

        int result = 0;
        int c = 0;
        if (size > 0) {
            c = queue[0].hashCode(); // initialize it to the hash code c for the first significant field in your object
            result = c;
            //this is wrong. how to do recursively?
            //what is significant data?
        }
        for (int i = 1; i < size; i++) { //For every remaining significant field f in your object, Compute an int hash code c for the field:
            c =queue[i].hashCode();
            result = 31 * result + c; //Combine the hash code c
        }
        return result;
    }
}
