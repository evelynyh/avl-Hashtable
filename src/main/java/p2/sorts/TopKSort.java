package p2.sorts;

import datastructures.worklists.MinFourHeap;

import java.util.Comparator;

public class TopKSort {
    public static <E extends Comparable<E>> void sort(E[] array, int k) {
        sort(array, k, (x, y) -> x.compareTo(y));
    }

    /**
     * Behaviour is undefined when k > array.length
     */
    public static <E> void sort(E[] array, int k, Comparator<E> comparator) {

    if (k <= array.length) {
        MinFourHeap heap = new MinFourHeap(comparator);

        for (int i = 0; i < k; i++) {
            heap.add(array[i]); // Insert each element to be sorted into a heap (MinFourHeap)
        }

        for (int i = k; i < array.length; i++) {
            int c = comparator.compare((E)heap.peek(), array[i]);
            if (c < 0) {
                heap.next();
                heap.add(array[i]);
            }
        }

        for (int i = 0; i < k; i++) {
            array[i] = (E)heap.next(); // push values to the array
        }

        for (int i = k; i < array.length; i++) {
            array[i] = null; // set rest of values to null
        }
    }

    }
}
