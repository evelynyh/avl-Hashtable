package p2.sorts;

import cse332.exceptions.NotYetImplementedException;

import java.util.Comparator;

public class QuickSort {
    public static <E extends Comparable<E>> void sort(E[] array) {
        QuickSort.sort(array, (x, y) -> x.compareTo(y));

    }

    public static <E> void sort(E[] array, Comparator<E> comparator) {
        array = separate(array, comparator, null);
    }

    public static <E> E[] separate(E[] array, Comparator<E> comparator, E pivot) {
        int size = array.length;
        if (size < 3) { // sorts array
            if (size > 1) {
                E[] a = (E[])new Object[size];
                if (comparator.compare(array[0], array[1]) > 0) {
                    a[0] = array[1];
                    a[1] = array[0];
                    array = a;
                }
            }
        } else {
            if ((comparator.compare(array[size - 1], array[size - 2]) > 0 && comparator.compare(array[size - 2], array[size - 3]) > 0)
                || (comparator.compare(array[size - 3], array[size - 2]) > 0 && comparator.compare(array[size - 2], array[size - 1]) > 0)){
                //size-1 is bigger than size-2 is bigger than size-3
                pivot = array[size - 2];
            } else if ((comparator.compare(array[size - 2], array[size - 1]) > 0 && comparator.compare(array[size - 1], array[size - 3]) > 0)
                        ||(comparator.compare(array[size - 3], array[size - 1]) > 0 && comparator.compare(array[size - 1], array[size - 2]) > 0)){
                //size-2 is bigger than size-1 is bigger than size-3
                pivot = array[size - 1];
            } else {
                pivot = array[size - 3];
            }

            //separate!!
            E[] a1 = (E[]) new Object[size];
            int index = 0;
            boolean foundPivot = false; // in case pivot stored as multiple values
            for (int i = 0; i < size; i++) {
                if (comparator.compare(pivot, array[i]) > 0 || (foundPivot && comparator.compare(pivot, array[i]) == 0)) {
                    a1[index] = array[i];
                    index++;
                } else if (comparator.compare(pivot, array[i]) == 0 && !foundPivot) {
                    foundPivot = true;
                }
            }

            E[] a = (E[]) new Object[index];
            for (int i = 0; i < index; i++) { // resize array a
                a[i] = a1[i];
            }

            E[] b = (E[]) new Object[size-index-1];
            index = 0;
            for (int i = 0; i < size; i++) {
                if (comparator.compare(pivot, array[i]) < 0) {
                    b[index] = array[i];
                    index++;
                }
            }
            E[] smaller = separate(a, comparator, pivot);
            E[] bigger = separate(b, comparator, pivot);
            for (int i = 0; i < smaller.length; i++) {
                array[i] = smaller[i];
            }
            array[smaller.length] = pivot;
            for (int i = smaller.length+1; i < size; i++) {
                array[i] = bigger[i-( smaller.length+1)];
            }
        }
        return array;
    }
}
