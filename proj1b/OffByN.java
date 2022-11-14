public class OffByN implements CharacterComparator {
    private final int N;

    /**
     * Constructor for an object whose equalChars method returns true
     * for characters that are off by N.
     */
    public OffByN(int N) {
        this.N = N;
    }

    @Override
    public boolean equalChars(char x, char y) {
        int diff = Math.abs(x - y);
        return diff == N;
    }

}
