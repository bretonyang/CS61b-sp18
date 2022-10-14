public class LinkedListDeque<T> {

    private class Node {
        public T item;
        public Node prev;
        public Node next;
        public Node(T item, Node prev, Node next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node sentinel;
    private int size;

    /**
     * Constructor for empty linked list deque.
     */
    public LinkedListDeque() {
        sentinel = new Node(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    /**
     * Constructor for linked list deque with one item.
     * @param item The value of the item.
     */
    public LinkedListDeque(T item) {
        sentinel = new Node(null, null, null);
        Node newNode = new Node(item, sentinel, sentinel);
        sentinel.next = newNode;
        sentinel.prev = newNode;
        size = 1;
    }

    /**
     * Adds an item of type T to the front of the deque.
     * @param item The value of the item.
     */
    public void addFirst(T item) {
        size++;
        Node newNode = new Node(item, sentinel, sentinel.next);
        sentinel.next.prev = newNode;
        sentinel.next = newNode;
    }

    /**
     * Adds an item of type T to the back of the deque.
     * @param item The value of the item.
     */
    public void addLast(T item) {
        size++;
        Node newNode = new Node(item, sentinel.prev, sentinel);
        sentinel.prev.next = newNode;
        sentinel.prev = newNode;
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
     * @return an integer.
     */
    public int size() {
        return size;
    }

    /**
     * Prints the items in the deque from first to last,
     * separated by a space.
     */
    public void printDeque() {
        Node ptr = sentinel;
        while(ptr.next != sentinel) {
            System.out.print(ptr.next.item + " ");
            ptr = ptr.next;
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
        T removedItem = sentinel.next.item;
        sentinel.next.next.prev = sentinel;
        sentinel.next = sentinel.next.next;
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
        T removedItem = sentinel.prev.item;
        sentinel.prev.prev.next = sentinel;
        sentinel.prev = sentinel.prev.prev;
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
        if (index >= size) {
            return null;
        }

        Node ptr = sentinel;
        while (index >= 0) {
            ptr = ptr.next;
            index--;
        }

        return ptr.item;
    }

    /**
     * Gets the item at the given index using recursion, where 0 is the front, 1 is the next item, and so forth.
     * If no such item exists (if index >= size or index < 0), returns null.
     * Must not alter the deque!
     * @param index Index of the desired item in the deque, starts with 0.
     * @return Value of the item of type T at given index.
     */
    public T getRecursive(int index) {
        if (index >= size) {
            return null;
        }
        return getRecursiveHelper(index, sentinel);
    }

    /**
     * Helper function for getRecursive() to get ith item recursively.
     * @param index Index of the desired item in list.
     * @param ptr Node pointer to the list.
     * @return Item at position i in the list.
     */
    private T getRecursiveHelper(int index, Node ptr) {
        if (index < 0) {
            return ptr.item;
        }
        return getRecursiveHelper(index - 1, ptr.next);
    }

}
