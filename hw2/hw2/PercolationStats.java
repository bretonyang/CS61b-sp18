package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {

    private final int numOfTests;
    private final double[] thresholds;

    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("N and T should be positive");
        }

        numOfTests = T;
        thresholds = new double[T];

        // Perform T experiments
        for (int counter = 0; counter < T; counter++) {
            Percolation percolation = pf.make(N);
            while (!percolation.percolates()) {
                int row, col;
                do {
                    row = StdRandom.uniform(0, N);
                    col = StdRandom.uniform(0, N);
                } while (percolation.isOpen(row, col));
                percolation.open(row, col);
            }

            // record percolation fraction
            thresholds[counter] = (double) percolation.numberOfOpenSites() / (N * N);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(thresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        // deviation is undefined when only one value
        if (numOfTests == 1) {
            return Double.NaN;
        }
        return StdStats.stddev(thresholds);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLow() {
        return mean() - (1.96 * stddev() / Math.sqrt(numOfTests));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        return mean() + (1.96 * stddev() / Math.sqrt(numOfTests));
    }
    
}
