public class ArrayDeque<T> {

    private T[] items;
    private int size;
    private int nextFirst;
    private int nextLast;

    private static final int RFACTOR = 2;
    private static final double MIN_USAGE_FACTOR = 0.25;

    /**
     * Constructor for empty array deque.
     */
    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        nextFirst = 4;
        nextLast = 5;
    }

    /**
     * Adds an item of type T to the front of the deque.
     * @param item The value of the item.
     */
    public void addFirst(T item) {
        if (size == items.length) {
            resize(size * RFACTOR);
        }

        size++;
        items[nextFirst] = item;
        nextFirst = prevIndex(nextFirst);
    }

    /**
     * Adds an item of type T to the back of the deque.
     * @param item The value of the item.
     */
    public void addLast(T item) {
        if (size == items.length) {
            resize(size * RFACTOR);
        }

        size++;
        items[nextLast] = item;
        nextLast = nextIndex(nextLast);
    }

    /**
     * Checks whether the deque is empty.
     * @return true if deque is empty, false otherwise.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the number of items in the deque.
     * @return an integer indicating the size of the deque.
     */
    public int size() {
        return size;
    }

    /**
     * Prints the items in the deque from first to last,
     * separated by a space.
     */
    public void printDeque() {
        int curIdx = nextIndex(nextFirst);
        int count = size;
        while (count > 0) {
            System.out.print(items[curIdx] + " ");
            curIdx = nextIndex(curIdx);
            count--;
        }
        System.out.println();
    }

    /**
     * Removes and returns the item at the front of the deque.
     * If no such item exists, returns null.
     * @return Item of type T, if it exists, otherwise null.
     */
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }

        size--;
        nextFirst = nextIndex(nextFirst);
        T removedItem = items[nextFirst];
        items[nextFirst] = null;

        if (isInvalidUsageFactor()) {
            resize(size * RFACTOR);
        }

        return removedItem;
    }

    /**
     * Removes and returns the item at the back of the deque.
     * If no such item exists, returns null.
     * @return Item of type T, if it exists, otherwise null.
     */
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }

        size--;
        nextLast = prevIndex(nextLast);
        T removedItem = items[nextLast];
        items[nextLast] = null;

        if (isInvalidUsageFactor()) {
            resize(size * RFACTOR);
        }

        return removedItem;
    }

    /**
     * Gets the item at the given index using iteration, where 0 is the front, 1 is the next item, and so forth.
     * If no such item exists (if index >= size or index < 0), returns null.
     * Must not alter the deque!
     * @param index Index of the item in the deque, starts with 0.
     * @return Value of the item of type T at given index.
     */
    public T get(int index) {
        if (index >= size || index < 0) {
            return null;
        }

        index = nextIndex(nextFirst + index);
        return items[index];
    }

    /**
     * Resize the items[] array to the given capacity.
     * @param capacity The new size of the items[] array.
     */
    private void resize(int capacity) {
        T[] newItems = (T[]) new Object[capacity];

        // Copy elements in items[] to newItems[]
        for (int i = 0, curIdx = nextIndex(nextFirst); i < size; i++) {
            newItems[i] = items[curIdx];
            curIdx = nextIndex(curIdx);
        }

        items = newItems;
        nextFirst = capacity - 1;
        nextLast = size;
    }

    private boolean isInvalidUsageFactor() {
        return items.length >= 16 && (double) size / items.length < MIN_USAGE_FACTOR;
    }

    /**
     * Private helper method for calculating next index in the deque.
     * @param idx The current index.
     * @return Index of the next index of idx.
     */
    private int nextIndex(int idx) {
        return (idx + 1) % items.length;
    }

    /**
     * Private helper method for calculating previous index in the deque.
     * @param idx The current index
     * @return Index of the previous index of idx.
     */
    private int prevIndex(int idx) {
        return (items.length + idx - 1) % items.length;
    }

}
