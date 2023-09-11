package datastructures.dictionaries;

import cse332.datastructures.containers.Item;
import cse332.interfaces.misc.DeletelessDictionary;
import cse332.interfaces.misc.Dictionary;
import cse332.interfaces.misc.SimpleIterator;
import datastructures.worklists.ListFIFOQueue;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

/**
 * 1. You must implement a generic chaining hashtable. You may not
 * restrict the size of the input domain (i.e., it must accept
 * any key) or the number of inputs (i.e., it must grow as necessary).
 * 3. Your HashTable should rehash as appropriate (use load factor as
 * shown in class!).
 * 5. HashTable should be able to resize its capacity to prime numbers for more
 * than 200,000 elements. After more than 200,000 elements, it should
 * continue to resize using some other mechanism.
 * 6. We suggest you hard code some prime numbers. You can use this
 * list: http://primes.utm.edu/lists/small/100000.txt
 * NOTE: Do NOT copy the whole list!
 * 7. When implementing your iterator, you should NOT copy every item to another
 * dictionary/list and return that dictionary/list's iterator.
 */
public class ChainingHashTable<K, V> extends DeletelessDictionary<K, V> {

    private Supplier<Dictionary<K, V>> newChain;
    private ListFIFOQueue primes;
    private Dictionary<K, V>[] hashTable;
    int tableSize;


    public ChainingHashTable(Supplier<Dictionary<K, V>> newChain) {
        this.newChain = newChain;
        primes = new ListFIFOQueue();
        initializePrimes();
        tableSize = 31;
        this.size = 0;
        hashTable = new Dictionary[tableSize];
    }

    private void initializePrimes() {
        int[] a = new int[] {61, 127, 257, 317, 631, 1277, 2579, 5051, 10181, 20011};
        for (int i = 0; i < a.length; i++) {
            this.primes.add(a[i]);
        }
    }

    @Override
    public V insert(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException();
        }
        int code = Math.abs(key.hashCode());
        //load factor = size/tableSize

        if (2 == size/tableSize) { // check if list size is good
            reHash();
        }
        V val = null; //tableSize may change if rehashed
        if (hashTable[code % tableSize] != null) {
          val = hashTable[code % tableSize].find(key);
        } else {
            hashTable[code % tableSize] = newChain.get(); // puts a chain in the index if it doesn't exist yet
        }
        int prevSize = hashTable[code % tableSize].size();
        hashTable[code % tableSize].insert(key, value);
        if (prevSize != hashTable[code % tableSize].size()) {
            // increments size when needed; in case insert updates items instead of adds
            size++;
        }
        return val;

    }

    private void reHash() {
        int oldSize = tableSize;
        if (primes.hasWork()) { // doubles table size
            tableSize = (int) primes.next();
        } else {
            tableSize = 2 * tableSize + 1;
        }

        Dictionary<K, V>[] newTable = new Dictionary[tableSize];//updates table to new size

        for (int d = 0; d < oldSize; d++) {
            if (hashTable[d] != null) {
                Iterator<Item<K, V>> itr = hashTable[d].iterator(); // iterate through the chain
                while (itr.hasNext()) {
                    Item<K, V> i = itr.next();
                    int index = Math.abs((i.key).hashCode()) % tableSize; // rehash

                    if (newTable[index] == null) {
                        newTable[index] = newChain.get(); // puts a chain in the index if it doesn't exist yet
                    }// insert value into new hashTable
                    newTable[index].insert(i.key, i.value);
                }
            }
        }

        this.hashTable = newTable;// our hashTable is now the new table
    }

    @Override
    public V find(K key) {
        if (key == null ) {
            throw new IllegalArgumentException();
        }
        int code = Math.abs(key.hashCode()) % tableSize;
        if (hashTable[code] == null) {
            return null;
        }
        return hashTable[code].find(key);
    }

    @Override
    public Iterator<Item<K, V>> iterator() {
        return new ChainingHashTable.itr();
    }
    private class itr extends SimpleIterator<Item<K, V>> {
        private int i;
        private boolean no;
        private Iterator<Item<K, V>> itr;

        public itr() {
            i = 0;
            no = false;
            while (hashTable[i] == null && i < tableSize-1) {
                i++;
            }
            if (hashTable[i] != null) {
                itr = hashTable[i].iterator();
            } else if (i >= tableSize-1) {
                no = true; // stops iterator
            }
        }


        @Override
        public boolean hasNext() {
            return (!no && (itr.hasNext())); // if size is 0, no more work can be done
        }

        @Override
        public Item<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item<K, V> item = null;
            if (itr.hasNext()) {
                item = itr.next();
            }
            if (!itr.hasNext()) {
                i++;
                while (i < tableSize-1 && hashTable[i] == null) {
                    i++;
                }
                if (hashTable[i] != null) {
                    itr = hashTable[i].iterator();
                } else if (i >= tableSize-1) {
                    no = true;
                }
            }
            return item;
        }
    }

    /**
     * Temporary fix so that you can debug on IntelliJ properly despite a broken iterator
     * Remove to see proper String representation (inherited from Dictionary)
     */
    @Override
    public String toString() {
//        String s = "";
//        for (Dictionary<K, V> i : hashTable) {
//            s+=i.toString();
//        }
//        return s;
        return " ";
    }
}
