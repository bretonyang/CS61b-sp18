/**
 * Tests the ArrayRingBuffer class.
 * @author Josh Hug
 * @author Breton Yang
 */

package synthesizer;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestArrayRingBuffer {
    @Test
    public void testEnqueue() {
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer<Integer>(3);
        arb.enqueue(1);
        assertEquals(1, arb.fillCount());
        arb.enqueue(2);
        assertEquals(2, arb.fillCount());
    }

    @Test
    public void testDequeue() {
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer<Integer>(3);
        arb.enqueue(1);
        arb.enqueue(2);
        arb.enqueue(3);
        int item = arb.dequeue();
        int item2 = arb.dequeue();

        assertEquals(1, item);
        assertEquals(2, item2);
        assertEquals(1, arb.fillCount());
    }

    @Test
    public void testPeek() {
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer<Integer>(3);
        arb.enqueue(1);
        arb.enqueue(2);
        arb.enqueue(3);
        int item = arb.peek();
        arb.dequeue();
        int item2 = arb.peek();

        assertEquals(1, item);
        assertEquals(2, item2);
    }

    /** Calls tests for ArrayRingBuffer. */
//    public static void main(String[] args) {
//        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
//    }
} 
