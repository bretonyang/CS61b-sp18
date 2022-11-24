package synthesizer;

public class GuitarString {
    private static final int SR = 44100; // Sampling Rate
    private static final double DECAY = .996; // energy decay factor

    /* Buffer for storing sound data. */
    private BoundedQueue<Double> buffer;

    /** Create a guitar string of the given frequency.  */
    public GuitarString(double frequency) {
        int capacity = (int) Math.round(SR / frequency);
        buffer = new ArrayRingBuffer<>(capacity);

        // fill buffer with 0
        while (!buffer.isFull()) {
            buffer.enqueue(0.0);
        }
    }

    /** Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {
        // dequeue everything in the buffer
        while (!buffer.isEmpty()) {
            buffer.dequeue();
        }

        // fill buffer with random noises between -0.5 and 0.5
        while (!buffer.isFull()) {
            double randNum = Math.random() - 0.5; // randNum: [-0.5, 0.5)
            buffer.enqueue(randNum);
        }
    }

    /**
     * Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm. 
     */
    public void tic() {
        double frontSample = buffer.dequeue();
        double newSample = (frontSample + buffer.peek()) * DECAY / 2;
        buffer.enqueue(newSample);
    }

    /** Return the double at the front of the buffer. */
    public double sample() {
        return buffer.peek();
    }
}
