package datastructures.dictionaries;

import cse332.datastructures.containers.Item;
import cse332.datastructures.trees.BinarySearchTree;
import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.misc.Dictionary;
import cse332.interfaces.misc.SimpleIterator;
import cse332.interfaces.trie.TrieMap;
import cse332.types.BString;
import datastructures.dictionaries.MoveToFrontList;

//import java.util.HashMap;
import datastructures.dictionaries.ChainingHashTable;
import java.util.Iterator;////////////////////////////////////////////////NEED TO FIX BY THE END OF THE WEEK
//import java.util.Map;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;
import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/trie/TrieMap.java
 * and cse332/interfaces/misc/Dictionary.java
 * for method specifications.
 */
public class HashTrieMap<A extends Comparable<A>, K extends BString<A>, V> extends TrieMap<A, K, V> {

    public class HashTrieNode extends TrieNode<Dictionary<A, HashTrieNode>, HashTrieNode> {
        public HashTrieNode() {
            this(null);
        }

        public HashTrieNode(V value) {
            this.pointers = new ChainingHashTable<A, HashTrieNode>(() -> new MoveToFrontList());
            this.value = value;
        }

        @Override
        public Iterator<Entry<A, HashTrieMap<A, K, V>.HashTrieNode>> iterator() {
            return new IteratorW(pointers.iterator());

        }

        public class IteratorW implements Iterator<Entry<A, HashTrieMap<A, K, V>.HashTrieNode>> {
            Iterator<Item<A, HashTrieMap<A, K, V>.HashTrieNode>> itr;
            public IteratorW(Iterator<Item<A, HashTrieMap<A, K, V>.HashTrieNode>> itr) {
                this.itr = itr;
            }

            public boolean hasNext() {
                return itr.hasNext();
            }

            public SimpleEntry<A, HashTrieMap<A, K, V>.HashTrieNode> next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                Item<A, HashTrieMap<A, K, V>.HashTrieNode> i = itr.next();
                return new SimpleEntry<A, HashTrieMap<A, K, V>.HashTrieNode>(i.key, i.value);
            }

        }
    }

    public HashTrieMap(Class<K> KClass) {
        super(KClass);
        this.root = new HashTrieNode();
        this.size = 0;
    }

    @Override
    public V insert(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException();
        }

        HashTrieNode back = (HashTrieNode)root;
        Iterator itr = key.iterator();

        if (key.isEmpty()) {
            if (root.value == null) {
                size+=1;
                root.value = value;
                return null;
            }

            V finalVal = root.value; // store current value
            root.value = value; // old value is replaced
            return finalVal; // in case no new nodes need to be created
        }

        A curr = (A)itr.next(); // current v value

        while (itr.hasNext() && back != null && back.pointers.find(curr) != null) { // traverse to get to back
            back = back.pointers.find(curr);
            curr = (A) itr.next();
        }

        while (itr.hasNext()) { // make new nodes
            back.pointers.insert(curr, new HashTrieNode());
            back = (HashTrieNode)back.pointers.find(curr);
            curr = (A)itr.next(); // current v value
        }

        if (back.pointers.find(curr) == null) {
            back.pointers.insert(curr, new HashTrieNode(value));
            size+=1;
            return null;
        } else if (back.pointers.find(curr).value == null) {
            size+=1;
            back.pointers.find(curr).value = value;
            return null;
        }

        V finalVal = back.pointers.find(curr).value; // store current value
        back.pointers.find(curr).value = value; // old value is replaced
        return finalVal; // in case no new nodes need to be created
    }

    @Override
    public V find(K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }

        if (!findPrefix(key)) {
            return null;
        }

        if (key.isEmpty()) {
            return (root.value);
        }

        HashTrieNode back = (HashTrieNode)root;
        Iterator itr = key.iterator();
        A val = (A)itr.next();

        while (itr.hasNext()) {
            back = (HashTrieNode)back.pointers.find(val);
            val = (A)itr.next(); // current v value
        }

        V found = back.pointers.find(val).value; // keep printing all mapping
        return found;
    }

    @Override
    public boolean findPrefix(K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }

        HashTrieNode back = (HashTrieNode)root;

        if(key.isEmpty()) {
            return (root != null);
        }


        Iterator itr = key.iterator();
        A val = (A)itr.next();

        while (itr.hasNext() && back.pointers.find(val) != null) {
            back = (HashTrieNode)back.pointers.find(val);
            val = (A)itr.next();
        }

        return (!itr.hasNext() && back.pointers.find(val) != null);
    }



     ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////                                         Unsupported methods below...                                               //////////////
     ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    @Override
    public void delete(K key) {
        throw new UnsupportedOperationException();
//        if (key == null) {
//            throw new IllegalArgumentException();
//        }
//        if (key.isEmpty()) {
//            if (root.value != null) {
//                this.size = 0;
//            }
//            root.value = null;
//        } else if (findPrefix(key)) {
//            HashTrieNode back = (HashTrieNode)root; // tracks children
//            Iterator itr = key.iterator();
//            helper(null, itr, back, false);
//        }
    }

//    private boolean helper(A keyVal, Iterator itr, HashTrieNode back, boolean bool) {
//        if (back.pointers.isEmpty()) {
//            return true;
//
//        } else if (!itr.hasNext()) { // bool makes sure all values are not null
//            if (back.value != null) {
//                back.value = null;// sets value to null if cannot remove
//                size-=1;
//            }
//            bool = false;
//        } else {
//            keyVal = (A)itr.next(); // how to use itr values
//            bool = helper(keyVal, itr, back.pointers.get(keyVal), bool);
//
//            if (back == (HashTrieNode)root && bool) {
//                this.size = 0;
//                bool = false;
//            } else if (bool) {
//                back.pointers.remove(keyVal);
//                size-=1;
//            }
//            if (back.value != null || !back.pointers.isEmpty()) { // removes subsequent nodes after base
//                bool = false;
//            }
//        }
//        return bool;
//    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
//        this.root = new HashTrieNode(); // create new map
//        this.size = 0;
    }
}