package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A hash table-backed Map implementation. Provides amortized constant time
 * access to elements via get(), remove(), and put() in the best case.
 *
 * @author Your name here
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    private static final int DEFAULT_SIZE = 16;
    private static final double MAX_LF = 0.75;

    private ArrayMap<K, V>[] buckets;
    private int size;

    private int loadFactor() {
        return size / buckets.length;
    }

    public MyHashMap() {
        buckets = new ArrayMap[DEFAULT_SIZE];
        this.clear();
    }

    public MyHashMap(int initialSize) {
        buckets = new ArrayMap[initialSize];
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        this.size = 0;
        for (int i = 0; i < this.buckets.length; i += 1) {
            this.buckets[i] = new ArrayMap<>();
        }
    }

    /**
     * Computes the hash function of the given key. Consists of
     * computing the hashcode, followed by modding by the number of buckets.
     * To handle negative numbers properly, uses floorMod instead of %.
     */
    private int hash(K key) {
        if (key == null) {
            return 0;
        }

        int numBuckets = buckets.length;
        return Math.floorMod(key.hashCode(), numBuckets);
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        if (key == null) {
            return null;
        }
        return buckets[hash(key)].get(key);
    }

    /* Associates the specified value with the specified key in this map. */
    @Override
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Null keys are not allowed.");
        }
        if (value == null) {
            throw new IllegalArgumentException("Null values are not allowed.");
        }

        if (loadFactor() >= MAX_LF) {
            resize(size * 2);
        }
        int bucketNum = hash(key);
        size -= buckets[bucketNum].size();
        buckets[bucketNum].put(key, value);
        size += buckets[bucketNum].size();
    }

    private void resize(int capacity) {
        ArrayMap<K, V>[] newBuckets = new ArrayMap[capacity];
        for (int i = 0; i < newBuckets.length; i++) {
            newBuckets[i] = new ArrayMap<>();
        }
        for (ArrayMap<K, V> bucket : buckets) {
            for (K key : bucket) {
                int newBucketNum = Math.floorMod(key.hashCode(), capacity);
                V value = bucket.get(key);
                newBuckets[newBucketNum].put(key, value);
            }
        }
        buckets = newBuckets;
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
        for (ArrayMap<K, V> bucket : buckets) {
            keysSet.addAll(bucket.keySet());
        }
        return keysSet;
    }

    /* Removes the mapping for the specified key from this map if exists.
     * Not required for this lab. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public V remove(K key) {
        if (key == null) {
            return null;
        }
        int bucketNum = hash(key);
        size -= buckets[bucketNum].size();
        V removedVal = buckets[bucketNum].remove(key);
        size += buckets[bucketNum].size();
        return removedVal;
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for this lab. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    @Override
    public V remove(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Null keys are not allowed.");
        }
        if (value == null) {
            throw new IllegalArgumentException("Null values are not allowed.");
        }
        int bucketNum = hash(key);
        size -= buckets[bucketNum].size();
        V removedVal = buckets[bucketNum].remove(key, value);
        size += buckets[bucketNum].size();
        return removedVal;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }
}
