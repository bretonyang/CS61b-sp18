package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int gridSize;

    // For caching.
    private int numOfOpenSites;

    // All top sites should be connected to this sentinel.
    // Note: why not a bottom sentinel? --> to avoid water backwash at the bottom
    private final int topSentinel;

    private final int bottomSentinel;

    // 2D array recording whether a site on the grid is opened.
    private final boolean[][] openStatusOfSites;

    private final WeightedQuickUnionUF WQU;

    // Arrays used to calculate a site's neighbors' row and col value.
    private static int[] dr = {-1, 0, 1, 0};
    private static int[] dc = {0, 1, 0, -1};

    // create N-by-N grid, with all sites initially blocked
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("N should be positive");
        }

        gridSize = N;
        numOfOpenSites = 0;
        topSentinel = N * N; // Since N*N is not in the grid, so it's perfect for sentinel
        bottomSentinel = N * N + 1; // Similar reason

        openStatusOfSites = new boolean[N][N];

        WQU = new WeightedQuickUnionUF(N * N + 2);
    }

    // open the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (outOfRange(row, col)) {
            throw new IndexOutOfBoundsException("Invalid (row, col) index");
        }

        // Return early if already opened before.
        if (openStatusOfSites[row][col]) {
            return;
        }

        openStatusOfSites[row][col] = true;
        numOfOpenSites++;

        // Connect current site to its neighbor if the neighbor is in grid and open.
        for (int idx = 0; idx < 4; idx++) {
            int neighborRow = row + dr[idx];
            int neighborCol = col + dc[idx];
            if (!outOfRange(neighborRow, neighborCol)
                    && openStatusOfSites[neighborRow][neighborCol]) {
                WQU.union(index(row, col), index(neighborRow, neighborCol));
            }
        }

        // Connect to topSentinel if a top site is opened.
        if (row == 0) {
            WQU.union(topSentinel, index(row, col));
        }
        // Connect to bottomSentinel if a bottom site is opened.
        // Note: use if here for the edge case N = 1.
        if (row == gridSize - 1) {
            WQU.union(bottomSentinel, index(row, col));
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (outOfRange(row, col)) {
            throw new IndexOutOfBoundsException("Invalid (row, col) index");
        }
        return openStatusOfSites[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (outOfRange(row, col)) {
            throw new IndexOutOfBoundsException("Invalid (row, col) index");
        }

        // A site is full if it's opened and is connected to top (topSentinel)
        return openStatusOfSites[row][col] && WQU.connected(index(row, col), topSentinel);
    }

    // number of open sites
    public int numberOfOpenSites() {
        return numOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return WQU.connected(topSentinel, bottomSentinel);
    }

    private boolean outOfRange(int row, int col) {
        return row < 0 || row >= gridSize || col < 0 || col >= gridSize;
    }

    private int index(int row, int col) {
        return row * gridSize + col;
    }

    // use for unit testing (not required)
    public static void main(String[] args) {

    }

}
