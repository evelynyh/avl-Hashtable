package datastructures.worklists;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.LIFOWorkList;

import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/worklists/LIFOWorkList.java
 * for method specifications.
 */
public class ArrayStack<E> extends LIFOWorkList<E> {

    E[] list;
    int size;


    public ArrayStack() {
        list = (E[]) new Object[10];
        size = 0;
    }

    @Override
    public void add(E work) {
        int len = list.length;
        size++;
        if (len == size) { //double the size of the array if full
            E[] currlist = list; // copy of list
            list = (E[]) new Object[2 * len];
            for (int i = list.length - 1; i > list.length - size; i--) {
                list[i] = currlist[len - 1]; // adds to front from back [   <-***]
                len--;
            }
        }
        list[list.length-size] = work;
    }

    @Override
    public E peek() {
        if (!hasWork()) {
            throw new NoSuchElementException();
        }
        return (E) list[list.length-size];
    }

    @Override
    public E next() { //pop
        if (!hasWork()) {
            throw new NoSuchElementException();
        }
        E val = (E) list[list.length-size];
        list[list.length-size] = null;
        size--;
        return val;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        size = 0;
        list = (E[]) new Object[10];
    }
}
