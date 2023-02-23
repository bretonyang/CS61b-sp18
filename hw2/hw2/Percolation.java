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

    // WQU with top and bottom virtual site.
    private final WeightedQuickUnionUF WQU;

    // WQU with only top virtual site.
    private final WeightedQuickUnionUF topOnlyWQU;

    // Arrays used to calculate a site's neighbors' row and col value.
    private static final int[] ROW_OFFSET = {-1, 0, 1, 0};
    private static final int[] COL_OFFSET = {0, 1, 0, -1};

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
        topOnlyWQU = new WeightedQuickUnionUF(N * N + 1);
    }

    // open the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (outOfRange(row, col)) {
            throw new IndexOutOfBoundsException("Invalid (row, col) xyTo1D");
        }

        // Return early if already opened before.
        if (isOpen(row, col)) {
            return;
        }

        openStatusOfSites[row][col] = true;
        numOfOpenSites++;
        connectToNeighbours(row, col);

        // Connect to topSentinel if a top site is opened.
        if (row == 0) {
            WQU.union(topSentinel, xyTo1D(row, col));
            topOnlyWQU.union(topSentinel, xyTo1D(row, col));
        }
        // Connect to bottomSentinel if a bottom site is opened.
        // Note: use if here for the edge case N = 1.
        if (row == gridSize - 1) {
            WQU.union(bottomSentinel, xyTo1D(row, col));
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (outOfRange(row, col)) {
            throw new IndexOutOfBoundsException("Invalid (row, col) xyTo1D");
        }
        return openStatusOfSites[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (outOfRange(row, col)) {
            throw new IndexOutOfBoundsException("Invalid (row, col) xyTo1D");
        }

        // A site is full if it's opened and is connected "only" to the topSentinel
        // Note: if we use WQU with bottom, then we'll have "backwash"
        return isOpen(row, col) && topOnlyWQU.connected(xyTo1D(row, col), topSentinel);
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

    private int xyTo1D(int row, int col) {
        return row * gridSize + col;
    }

    private void connectToNeighbours(int row, int col) {
        // Connect current site to its neighbor if the neighbor is in grid and open.
        for (int idx = 0; idx < 4; idx++) {
            int neighborRow = row + ROW_OFFSET[idx];
            int neighborCol = col + COL_OFFSET[idx];
            if (!outOfRange(neighborRow, neighborCol)
                    && isOpen(neighborRow, neighborCol)) {
                WQU.union(xyTo1D(row, col), xyTo1D(neighborRow, neighborCol));
                topOnlyWQU.union(xyTo1D(row, col), xyTo1D(neighborRow, neighborCol));
            }
        }
    }

    // use for unit testing (not required)
    public static void main(String[] args) {

    }

}
