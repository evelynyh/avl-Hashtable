package datastructures.worklists;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.FIFOWorkList;

import java.util.NoSuchElementException;

    /**
     * See cse332/interfaces/worklists/FIFOWorkList.java
     * for method specifications.
     */

    public class ListFIFOQueue<E> extends FIFOWorkList<E> {
        private class NewNode<E> {
            public E name;        // this person's name
            public NewNode next;  // next node in the list

            // constructs a node with the given name and a null link
            public NewNode(E name) {
                this(name, null);
            }

            // constructs a node with the given name and link
            public NewNode(E name, NewNode next) {
                this.name = name;
                this.next = next;
            }
        }

        private NewNode front;
        private NewNode back;
        private int size;
        public ListFIFOQueue() {
            front = new NewNode(null); //queue and front pointer
            back = front;
            size = 0;
        }

        @Override
        public void add(E work) {
            if (front != null && front.name == null) { // if front node can be filled, it is filled
                front.name = work;
            } else { // add to the back otherwise
                //f -> [5] -> [] - > []
                //     b^
                back.next = new NewNode(work);
                back = back.next;
            }
            size+=1;
        }

        @Override
        public E peek() {
            if (!hasWork()) {
                throw new NoSuchElementException();
            }
            return (E) front.name; // shows front name

        }

        @Override
        public E next() {
            if (!hasWork()) {
                throw new NoSuchElementException();
            }
            E curr = peek();
            if (front != null && front.next != null) {
                front = front.next;
                size-=1;
            } else {
                clear();
            }
            return curr; // removes front and returns value
        }

        @Override
        public int size() {
            return size; // size of queue
        }

        @Override
        public void clear() {
            this.front = new NewNode(null); // clears entire queue
            back = front;
            size = 0;
        }
    }
