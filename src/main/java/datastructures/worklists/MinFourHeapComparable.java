package datastructures.worklists;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.PriorityWorkList;

import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/worklists/PriorityWorkList.java
 * for method specifications.
 */
public class MinFourHeapComparable<E extends Comparable<E>> extends PriorityWorkList<E> {
    /* Do not change the name of this field; the tests rely on it to work correctly. */
    private E[] data;
    private int size;
    //parent (k-1)/4
    // child 4*k+(n)

    public MinFourHeapComparable() {
        data = (E[])new Comparable[100];
        size = 0;
    }

    @Override
    public boolean hasWork() {
        return this.size() > 0;
    }

    @Override
    public void add(E work) {
        //min root ---> max leaf
        int len = data.length;
        size+=1;

        if (len == size) { //double the size of the array if full
            E[] currlist = data; // copy of list
            this.data = (E[])new Comparable[2*size];
            for (int i = 0; i < size; i++) {
                data[i] = currlist[i]; // adds back values
                len--;
            }
        }
        data[size-1] = work;
        int toRoot = size - 1;
        while (true) { //percolates up
            E child = data[toRoot];
            E parent = data[(toRoot-1)/4];//parent (k-1)/4

            int s = child.compareTo(parent);
            //smallest child,parent
            if (s < 0) {
                data[toRoot] = parent;
                data[(toRoot-1)/4] = child;
                toRoot = (toRoot-1)/4;

            } else {
                break;
            }
        }

    }

    @Override
    public E peek() {
        if (!hasWork()) {
            throw new NoSuchElementException();
        }
        return (E) data[0];
    }

    @Override
    public E next() { // pop root
        if (!hasWork()) {
            throw new NoSuchElementException();
        }
        E val = peek();
        data[0] = data[size - 1];
        data[size - 1] = null;
        int toLeaf = 0;

        while ((4*toLeaf+1 < size) && (data[4*toLeaf+1] != null)) { //percolates down
            E finalVal = data[4*toLeaf + (1)]; // for initialization purposes
            int fin = 1;

            for (int i = 1; i < 4; i++) {
                if (data[4*toLeaf+i+1] != null) {
                    int compar =  finalVal.compareTo(data[4*toLeaf + (i+1)]);
                    if (compar > 0) {
                        finalVal = data[4*toLeaf + (i+1)];
                        fin = i+1;
                    }
                }
            }

            E parent = data[toLeaf];
            E smallChild = finalVal;
            int index = fin;
            int s = smallChild.compareTo(parent);
            //smallest child,parent
            if (s < 0) {
                data[toLeaf] = smallChild;
                data[4*toLeaf + (fin)] = parent;
                toLeaf = 4*toLeaf + fin;

            } else {
                break;
            }
        }
        size-=1;
        return val;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        data = (E[]) new Comparable[100];
        size = 0;
    }
}
