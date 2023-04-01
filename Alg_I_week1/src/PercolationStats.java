import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    // constant of 95% confidence interval
    private static final double CONFIDENCE_95 = 1.96;

    // declare an array to store the percolation threshold
    private double[] mPercolationThreshold;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        // perform trials independent experiments on an Percolation system of size n
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("n and trials must be greater than 0");
        }
        mPercolationThreshold = new double[trials];
        for (int i = 0; i < trials; i++) {
            // create a new Percolation system for each trial
            Percolation mPercolation = new Percolation(n);
            // open sites until the system percolates
            while (!mPercolation.percolates()) {
                // open a random site
                int row = StdRandom.uniformInt(1, n + 1);
                int col = StdRandom.uniformInt(1, n + 1);
                mPercolation.open(row, col);
            }
            // calculate the percolation threshold
            mPercolationThreshold[i] = (double) mPercolation.numberOfOpenSites() / (n * n);
        }

    }

    // sample mean of percolation threshold
    public double mean() {
        // calculate the mean of percolation threshold
        return StdStats.mean(mPercolationThreshold);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        // calculate the standard deviation of percolation threshold
        return StdStats.stddev(mPercolationThreshold);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        // calculate the low endpoint of 95% confidence interval
        return mean() - CONFIDENCE_95 * stddev() / Math.sqrt(mPercolationThreshold.length);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        // calculate the high endpoint of 95% confidence interval
        return mean() + CONFIDENCE_95 * stddev() / Math.sqrt(mPercolationThreshold.length);
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats mPercolationStats = new PercolationStats(n, trials);
        System.out.println("mean                    = " + mPercolationStats.mean());
        System.out.println("stddev                  = " + mPercolationStats.stddev());
        System.out.println("95% confidence interval = [" + mPercolationStats.confidenceLo() + ", "
                + mPercolationStats.confidenceHi() + "]");
    }

}