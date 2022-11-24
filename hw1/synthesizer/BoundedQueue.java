package synthesizer;

import java.util.Iterator;

public interface BoundedQueue<T> extends Iterable<T> {
    /**
     * Return size of the buffer.
     */
    int capacity();

    /**
     * Return number of items currently in the buffer
     */
    int fillCount();

    /**
     * Add item x to the end of the queue.
     */
    void enqueue(T x);

    /**
     * Delete and return item from the front of the queue.
     */
    T dequeue();

    /**
     * Return (but do not delete) item from the front of the queue.
     */
    T peek();

    /**
     * Returns an Iterator to the BQ.
     */
    Iterator<T> iterator();

    /**
     * Returns whether the buffer is empty.
     */
    default boolean isEmpty() {
        return fillCount() == 0;
    }

    /**
     * Returns whether the buffer is full.
     */
    default boolean isFull() {
        return fillCount() == capacity();
    }
}
