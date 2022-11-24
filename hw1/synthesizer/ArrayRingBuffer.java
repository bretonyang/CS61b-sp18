package synthesizer;

import java.util.Iterator;

public class ArrayRingBuffer<T> extends AbstractBoundedQueue<T> {
    /* Index for the next dequeue or peek. (the least recently inserted item). */
    private int first;

    /* Index for the next enqueue (one beyond the most recently inserted item). */
    private int last;

    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        this.capacity = capacity;
        this.rb = (T[]) new Object[capacity];
        this.first = 0;
        this.last = 0;
        this.fillCount = 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayRingBufferIterator();
    }

    private class ArrayRingBufferIterator implements Iterator<T> {
        private int curIndex;
        private int seenItems;

        ArrayRingBufferIterator() {
            curIndex = first;
            seenItems = 0;
        }

        @Override
        public boolean hasNext() {
            return seenItems < fillCount;
        }

        @Override
        public T next() {
            T returnItem = rb[curIndex];
            curIndex = nextIndex(curIndex);
            seenItems++;
            return returnItem;
        }
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    @Override
    public void enqueue(T x) {
        if (isFull()) {
            throw new RuntimeException("Ring Buffer Overflow");
        }

        rb[last] = x;
        last = nextIndex(last);
        fillCount++;
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    @Override
    public T dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Ring Buffer Underflow");
        }

        T returnItem = rb[first];
        rb[first] = null;
        first = nextIndex(first);
        fillCount--;
        return returnItem;
    }

    /**
     * Return oldest item, but don't remove it.
     */
    @Override
    public T peek() {
        if (isEmpty()) {
            throw new RuntimeException("Ring buffer empty");
        }
        return rb[first];
    }

    private int nextIndex(int index) {
        return (index + 1) % capacity;
    }
}
