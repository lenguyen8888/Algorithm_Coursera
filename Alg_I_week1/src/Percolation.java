
// The model. We model a percolation system using an n-by-n grid of sites. 
// Each site is either open or blocked. A full site is an open site that can 
// be connected to an open site in the top row via a chain of neighboring 
// (left, right, up, down) open sites. We say the system percolates if there 
// is a full site in the bottom row. In other words, a system percolates 
// if we fill all open sites connected to the top row and that process fills 
// some open site on the bottom row. (For the insulating/metallic materials example,
// the open sites correspond to metallic materials, so that a system that percolates
// has a metallic path from top to bottom, with full sites conducting. For the 
// porous substance example, the open sites correspond to empty space through 
// which water might flow, so that a system that percolates lets water fill 
// open sites, flowing from top to bottom.)
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // declare constant int to track top virtual site
    private static final int TOP_VIRTUAL_SITE = 0;

    // declare WeightedQuickUnionUF mUF2VirtualSites, mUFNoBottomVirtualSite
    // the first WeightedQuickUnionUF has 2 virtual sites, one at the top and one at the bottom
    // the second WeightedQuickUnionUF has only 1 virtual site at the top to avoid backwash
    private WeightedQuickUnionUF mUF2VirtualSites, mUFNoBottomVirtualSite;
    // declare 2-D boolean array to track open sites
    private boolean[][] grid;
    // declare int to track number of open sites
    private int mOpenSiteCount;
    // declare int to track size of grid
    private int mSize;
    // declare constant int to track bottom virtual site
    private int mBottomVirtualSite;

    public Percolation(int n) {
        // check if n is valid
        checkValidN(n);
        // initialize mSize
        mSize = n;
        // initialize mOpenSiteCount
        mOpenSiteCount = 0;
        // initialize mUF2VirtualSites
        mUF2VirtualSites = new WeightedQuickUnionUF(n * n + 2);
        // initialize mUFNoBottomVirtualSite
        mUFNoBottomVirtualSite = new WeightedQuickUnionUF(n * n + 1);
        // initialize grid
        grid = new boolean[n][n];
        // initialize mBottomVirtualSite
        mBottomVirtualSite = n * n + 1;
    }

    // declare helper method to mapt col and row index to nUF index
    // row and col are 1-based index of 1st row and 1st col == 1
    // for example, if n = 3, then the 1st row and 1st col == 1 => (1, 1) => 1
    // the 2nd row and 2nd col == 2 => (2, 2) => 4
    // the 3rd row and 3rd col == 3 => (3, 3) => 9
    private int getRowColIndex(int row, int col) {
        return (row - 1) * mSize + col;
    }

    // private helper method to check if n is valid
    private void checkValidN(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be greater than 0");
        }
    }

    // private helper method to check if row and col are valid
    private void checkValidRowCol(int row, int col) {
        if (row < 1 || row > mSize || col < 1 || col > mSize) {
            throw new IllegalArgumentException("row or col is out of range");
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        checkValidRowCol(row, col);
        // if site is already open, return
        if (isOpen(row, col)) {
            return;
        }
        // set site to open
        grid[row - 1][col - 1] = true;
        // increment open site count
        mOpenSiteCount++;
        // if row is 1, connect to top virtual site
        if (row == 1) {
            mUF2VirtualSites.union(TOP_VIRTUAL_SITE, getRowColIndex(row, col));
            mUFNoBottomVirtualSite.union(TOP_VIRTUAL_SITE, getRowColIndex(row, col));
        }
        // if row is n, connect to bottom virtual site
        if (row == mSize) {
            mUF2VirtualSites.union(mBottomVirtualSite, getRowColIndex(row, col));
        }
        // if row is not 1, connect to site above it if open
        if (row > 1 && isOpen(row - 1, col)) {
            mUF2VirtualSites.union(getRowColIndex(row, col), getRowColIndex(row - 1, col));
            mUFNoBottomVirtualSite.union(getRowColIndex(row, col), getRowColIndex(row - 1, col));
        }
        // if row is not n, connect to site below it if open
        if (row < mSize && isOpen(row + 1, col)) {
            mUF2VirtualSites.union(getRowColIndex(row, col), getRowColIndex(row + 1, col));
            mUFNoBottomVirtualSite.union(getRowColIndex(row, col), getRowColIndex(row + 1, col));
        }
        // if col is not 1, connect to site to the left if open
        if (col > 1 && isOpen(row, col - 1)) {
            mUF2VirtualSites.union(getRowColIndex(row, col), getRowColIndex(row, col - 1));
            mUFNoBottomVirtualSite.union(getRowColIndex(row, col), getRowColIndex(row, col - 1));
        }
        // if col is not n, connect to site to the right if open
        if (col < mSize && isOpen(row, col + 1)) {
            mUF2VirtualSites.union(getRowColIndex(row, col), getRowColIndex(row, col + 1));
            mUFNoBottomVirtualSite.union(getRowColIndex(row, col), getRowColIndex(row, col + 1));
        }

    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkValidRowCol(row, col);
        return grid[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        checkValidRowCol(row, col);
        return mUFNoBottomVirtualSite.find(TOP_VIRTUAL_SITE) == mUFNoBottomVirtualSite.find(getRowColIndex(row, col));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return mOpenSiteCount;
    }

    // does the system percolate?
    public boolean percolates() {
        return mUF2VirtualSites.find(TOP_VIRTUAL_SITE) == mUF2VirtualSites.find(mBottomVirtualSite);
    }

    // test client (optional)
    public static void main(String[] args) {
        // test client Percolation of size n
        int n = Integer.parseInt(args[0]);
        Percolation p = new Percolation(n);
        // open a random col all the way down
        int col = StdRandom.uniformInt(1, n + 1);
        for (int i = 1; i <= n; i++) {
            p.open(i, col);
        }
        // print message if system percolates
        if (p.percolates()) {
            System.out.println("System percolates");
        } else {
            System.out.println("System does not percolate");
        }
    }
}
