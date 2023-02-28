package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author Breton Yang
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private class Node {
        /* (K, V) pair stored in this Node. */
        private K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private Node root;  /* Root node of the tree. */
    private int size; /* The number of key-value pairs in the tree */

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Returns the value mapped to by KEY in the subtree rooted in P.
     * or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node p) {
        if (p == null) {
            return null;
        }
        int comparison = key.compareTo(p.key);
        if (comparison == 0) {
            return p.value;
        } else if (comparison < 0) {
            return getHelper(key, p.left);
        } else {
            return getHelper(key, p.right);
        }
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        if (key == null) {
            return null;
        }
        return getHelper(key, root);
    }

    /**
     * Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
     * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p) {
        if (p == null) {
            return new Node(key, value);
        }

        int comparison = key.compareTo(p.key);
        if (comparison == 0) {
            p.value = value;
        } else if (comparison < 0) {
            p.left = putHelper(key, value, p.left);
        } else {
            p.right = putHelper(key, value, p.right);
        }
        return p;
    }

    /**
     * Inserts the key.
     * If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Null keys are not allowed.");
        }
        if (value == null) {
            throw new IllegalArgumentException("Null values are not allowed.");
        }
        root = putHelper(key, value, root);
        size++;
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> keysSet = new HashSet<>();
        dfsForAllKeys(root, keysSet);
        return keysSet;
    }

    private void dfsForAllKeys(Node rootNode, Set<K> keysSet) {
        if (rootNode == null) {
            return;
        }
        keysSet.add(rootNode.key);
        dfsForAllKeys(rootNode.left, keysSet);
        dfsForAllKeys(rootNode.right, keysSet);
    }

    /**
     * Removes KEY from the tree if present
     * returns VALUE removed,
     * null on failed removal.
     */
    @Override
    public V remove(K key) {
        if (key == null) {
            return null;
        }

        V valToRemove = get(key);
        if (valToRemove != null) {
            root = deleteKeyHelper(key, root);
            size--;
        }
        return valToRemove;
    }

    /**
     * Removes the key-value entry for the specified key only if it is
     * currently mapped to the specified value.  Returns the VALUE removed,
     * null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Null keys are not allowed.");
        }
        if (value == null) {
            throw new IllegalArgumentException("Null values are not allowed.");
        }

        V valToRemove = get(key);
        if (valToRemove != null && valToRemove.equals(value)) {
            root = deleteKeyHelper(key, root);
            size--;
        } else { // if valToRemove is null or does not equal to target value
            valToRemove = null;
        }
        return valToRemove;
    }

    /**
     * Helper method to delete a key from BST, but does not return its value.
     */
    private Node deleteKeyHelper(K key, Node rootNode) {
        if (rootNode == null) {
            return null;
        }

        int comparison = key.compareTo(rootNode.key);
        if (comparison < 0) {
            rootNode = deleteKeyHelper(key, rootNode.left);
        } else if (comparison > 0) {
            rootNode = deleteKeyHelper(key, rootNode.right);
        } else {
            // 0 or 1 child
            if (rootNode.left == null) {
                return rootNode.right;
            }
            if (rootNode.right == null) {
                return rootNode.left;
            }

            // 2 children
            Node nodeToDelete = rootNode;
            rootNode = findMax(nodeToDelete.left);
            rootNode.left = deleteMax(nodeToDelete.left);
            rootNode.right = nodeToDelete.right;
        }
        return rootNode;
    }

    /**
     * Returns the node with maximum key in the subtree.
     */
    private Node findMax(Node rootNode) {
        if (rootNode.right == null) {
            return rootNode;
        }
        return findMax(rootNode.right);
    }

    private Node deleteMax(Node rootNode) {
        if (rootNode.right == null) {
            return rootNode.left;
        }
        rootNode.right = deleteMax(rootNode.right);
        return rootNode;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }
}
