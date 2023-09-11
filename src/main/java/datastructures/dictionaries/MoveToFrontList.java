package datastructures.dictionaries;

import cse332.datastructures.containers.Item;
import cse332.datastructures.trees.BinarySearchTree;
import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.misc.DeletelessDictionary;
import cse332.interfaces.misc.SimpleIterator;
import cse332.interfaces.worklists.WorkList;
import datastructures.worklists.ArrayStack;
import datastructures.worklists.ListFIFOQueue;

import java.util.Iterator;

/**
 * 1. The list is typically not sorted.
 * 2. Add new items to the front of the list.
 * 3. Whenever find or insert is called on an existing key, move it
 * to the front of the list. This means you remove the node from its
 * current position and make it the first node in the list.
 * 4. You need to implement an iterator. The iterator SHOULD NOT move
 * elements to the front.  The iterator should return elements in
 * the order they are stored in the list, starting with the first
 * element in the list. When implementing your iterator, you should
 * NOT copy every item to another dictionary/list and return that
 * dictionary/list's iterator.
 */
public class MoveToFrontList<K, V> extends DeletelessDictionary<K, V> {

    private class NewNode<K, V> extends Item<K, V> {

        private MoveToFrontList.NewNode next;  // next node in the list

        // constructs a node with the given name and a null link
        public NewNode(K key, V value) {
            this(key, value, null);
        }

        // constructs a node with the given name and link
        public NewNode(K key, V value, MoveToFrontList.NewNode next) {
            super(key, value);
            this.next = next;
        }
    }

    private MoveToFrontList.NewNode front;
    private int size;
    public MoveToFrontList() {
        front = new MoveToFrontList.NewNode(null, null); //front of list
        size = 0;
    }

    @Override
    public V insert(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException();
        }
        V val = null;
        MoveToFrontList.NewNode temp = front;
        boolean found = false;

        while (temp != null && temp.key != null && !found) {
            if ((temp.key.equals(key))) {
                found = true;
            } else {
                temp = temp.next;
            }
        }

        if (found) { // key mapping exists and need to change value
            val = (V)temp.value;
            temp.value = value;
        } else { // add to the back otherwise
            temp = front;
            front = new MoveToFrontList.NewNode(key, value); //new items are inserted at the front
            size+=1;
            front.next = temp;
        }


        return val;
    }

    @Override
    public V find(K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        V val = null;

        boolean found = false;
        MoveToFrontList.NewNode temp = front.next;
        MoveToFrontList.NewNode beforeTemp = front;

        if (beforeTemp != null && beforeTemp.key != null && beforeTemp.key.equals(key)) {// returns right away if front already points to key
            return (V)beforeTemp.value;
        }

        while (temp != null && temp.key != null && !found) {
            if ((temp.key.equals(key))) {
                found = true;
            } else {
                beforeTemp = beforeTemp.next;
                temp = temp.next;
            }
        }

        if (found) {
            val = (V)temp.value;
            MoveToFrontList.NewNode temp2 = front;
            front = temp; // item gets moved to the front when referenced
            beforeTemp.next = beforeTemp.next.next;
            front.next = temp2;
        }
        return val;
    }

    public int size() {
        return size;
    }

    @Override
    public Iterator<Item<K, V>> iterator() {
        return new MoveToFrontList.itr();
    }

    private class itr extends SimpleIterator<Item<K, V>> {
        private MoveToFrontList.NewNode curr;

        public itr() {
            this.curr = front;
        }

        @Override
        public boolean hasNext() {
            return (curr.next != null);
        }

        @Override
        public Item<K, V> next() {
            MoveToFrontList.NewNode val = curr;
            this.curr = curr.next;

            return val;
        }
    }
}
