package datastructures.dictionaries;

import cse332.datastructures.trees.BinarySearchTree;
import datastructures.worklists.ArrayStack;

/**
 * AVLTree must be a subclass of BinarySearchTree<E> and must use
 * inheritance and calls to superclass methods to avoid unnecessary
 * duplication or copying of functionality.
 * <p>
 * 1. Create a subclass of BSTNode, perhaps named AVLNode.
 * 2. Override the insert method such that it creates AVLNode instances
 * instead of BSTNode instances.
 * 3. Do NOT "replace" the children array in BSTNode with a new
 * children array or left and right fields in AVLNode.  This will
 * instead mask the super-class fields (i.e., the resulting node
 * would actually have multiple copies of the node fields, with
 * code accessing one pair or the other depending on the type of
 * the references used to access the instance).  Such masking will
 * lead to highly perplexing and erroneous behavior. Instead,
 * continue using the existing BSTNode children array.
 * 4. Ensure that the class does not have redundant methods
 * 5. Cast a BSTNode to an AVLNode whenever necessary in your AVLTree.
 * This will result a lot of casts, so we recommend you make private methods
 * that encapsulate those casts.
 * 6. Do NOT override the toString method. It is used for grading.
 * 7. The internal structure of your AVLTree (from root to the leaves) must be correct
 */

public class AVLTree<K extends Comparable<? super K>, V> extends BinarySearchTree<K, V> {
    private class AVLNode extends BSTNode {
        int height;
        public AVLNode(K key, V value) {
            super(key, value);
            this.height = 0;
        }
    }

    public AVLTree() {
        root = null;
        size = 0;
    }

    public V insert(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException();
        }
        V val;
        AVLNode prev = null; // points to node before temp
        AVLNode temp = (AVLNode)root;
        int child;

        while (temp != null) {// sees if key still exists
            int direction = Integer.signum(key.compareTo(temp.key)); //-1=left, 1 = right

            if (direction == 0) {// We ran into the key while traversing
                val = temp.value;
                temp.value = value; // replace value
                return val; // return previous value
            }
            else {
                // direction + 1 = {0, 2} -> {0, 1} /*taken from bst*/
                temp = (AVLNode)temp.children[ Integer.signum(direction + 1)];
            }
        }

        ArrayStack array = new ArrayStack();
        temp = (AVLNode)root;
        while (temp != null) { // if key is not there we need to update height
            int direction = Integer.signum(key.compareTo(temp.key)); //-1=left, 1 = right
                // direction + 1 = {0, 2} -> {0, 1} /*taken from bst*/
            child = Integer.signum(direction + 1);
            prev = temp;
               // increments height of temp to be 1+ to accommodate the new node
            array.add(temp);// adds to stack for traversal later
            temp = (AVLNode)temp.children[child];
        }

        temp = new AVLNode(key, value); // add value, key, and height to new node
        temp.height = 0;
        if (this.root == null) {
            this.root = temp; // if root needs a first node; edge case
        } else if (prev != null) {
            prev.children[Integer.signum(Integer.signum(key.compareTo(prev.key)) + 1)] = temp;
        }

        size++;
        array.add(temp);
        balance(array,null, null,null,null); // checks if AVL is balanced`
        return null; // returns null when new node is made
    }

    public void imbalanceLL(AVLNode parent, AVLNode top, AVLNode middle) {
        AVLNode tempMC = (AVLNode)middle.children[1];

        if (parent == null) { // adds new top to the rest of the tree
            this.root = middle;
        } else if ((parent.children[1]).key.compareTo(top.key) == 0) { // if previous top was to the right
            parent.children[1] = middle;

        } else { // if previous top was to the left
            parent.children[0] = middle;
        }

        top.children[0] = null;
        if (tempMC != null) {
            top.children[0] = tempMC;
            top.height = tempMC.height+1;
        }
        middle.children[1] = top;

        int hr = -1;  //height update
        int hl = -1;
        if (top.children[0] != null) {
            hr = ((AVLNode)top.children[0]).height;
        }
        if (top.children[1] != null) {
            hl = ((AVLNode) top.children[1]).height;
        }
        top.height = Math.max(hl, hr) + 1;

        middle.height = top.height+1;
    }

    public void imbalanceRR(AVLNode parent, AVLNode top, AVLNode middle) {
        AVLNode tempMC = (AVLNode)middle.children[0];

        if (parent == null) { // adds new top to the rest of the tree
            this.root = middle;
        } else if ((parent.children[1]).key.compareTo(top.key) == 0) { // if previous top was to the right
            parent.children[1] = middle;

        } else { // if previous top was to the left
            parent.children[0] = middle;
        }

        top.children[1] = null;
        if (tempMC != null) {
            top.children[1] = tempMC;
            top.height = tempMC.height+1;
        }
        middle.children[0] = top;

        int hr = -1; //height update
        int hl = -1;
        if (top.children[0] != null) {
            hr = ((AVLNode) top.children[0]).height;
        }
        if (top.children[1] != null) {
            hl = ((AVLNode) top.children[1]).height;
        }
        top.height = Math.max(hl, hr) + 1;

        middle.height = top.height+1;
        if (parent != null) {
            parent.height = middle.height + 1;
        }
    }

    public void imbalanceRL(AVLNode parent, AVLNode top, AVLNode middle, AVLNode bottom) { //fixes LR imbalance
        imbalanceLL(top, middle, bottom);
        imbalanceRR(parent, top, bottom);
    }

    public void imbalanceLR(AVLNode parent, AVLNode top, AVLNode middle, AVLNode bottom) { //fixes RL imbalance
        imbalanceRR(top, middle, bottom);
        imbalanceLL(parent, top, bottom);
    }
    public void balance(ArrayStack array, AVLNode parent, AVLNode top, AVLNode middle, AVLNode bottom) {
       boolean b = true;
        while (array.hasWork() && b) {
           bottom = middle; // values shift as we traverse up
           middle = top;
           top = (AVLNode)array.next();
           if (middle != null) {
               top.height = Math.max(middle.height + 1, top.height);///////////////////////////////////////////////////////////////////////////////////////////////////////////
           }

           int hl = -1;  //null children case
           int hr = -1;
           if (top.children[0] != null || top.children[1] != null) { //if we can traverse down, check heights
               if (top.children[0] != null) {
                   hr = ((AVLNode)top.children[0]).height;
               }
               if (top.children[1] != null) {
                   hl = ((AVLNode) top.children[1]).height;
               }

               if (hl - hr < -1 || hl - hr > 1) {// checks top l and right
                   if (array.hasWork()) {
                       parent =  (AVLNode)array.next();
                   }
                   b = false;
                   calculate(parent, top, middle, bottom); //height difference too much at top node
               }
           }
       }
    }

    public void calculate(AVLNode parent, AVLNode top, AVLNode middle, AVLNode bottom) { //LL  RL  LR  RR
        AVLNode left = (AVLNode)top.children[0];
        AVLNode right = (AVLNode)top.children[1];
        AVLNode leftM = (AVLNode)middle.children[0];
        AVLNode rightM = (AVLNode)middle.children[1];

        if (left != null && leftM != null && (left.key.compareTo(middle.key) == 0)
            && (leftM.key.compareTo(bottom.key) == 0)) {
            //can traverse left twice and going left traverses to the correct keys
            imbalanceLL(parent, top, middle);
        } else if (right != null && leftM != null && (right.key.compareTo(middle.key) == 0)
                    && (leftM.key.compareTo(bottom.key) == 0)) {
            //can traverse right then left and going left traverses to the correct keys
            imbalanceRL(parent, top, middle, bottom);
        } else if  (left != null && rightM != null && (left.key.compareTo(middle.key) == 0)
                && (rightM.key.compareTo(bottom.key) == 0)) {
            //can traverse left then right and going left traverses to the correct keys
            imbalanceLR(parent, top, middle, bottom);
        } else { // must be a right-right imbalance otherwise
            imbalanceRR(parent, top, middle);
        }
    }
}
