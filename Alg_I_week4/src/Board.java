import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinkedQueue;
import edu.princeton.cs.algs4.StdOut;

public class Board {

    // int[][] tiles is a 2D array of tiles
    private final int[][] tiles;

    // board size
    private final int n;

    // cache value
    private final int hammingDist, manhattanDist;

    // cache blank tile position
    private int blankRow, blankCol;

    // board goal
    private final boolean boardGoal;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles == null)
            throw new IllegalArgumentException();
        n = tiles.length;
        if (n < 2 || n > 128)
            throw new IllegalArgumentException();

        this.tiles = copy2DArray(tiles);
        hammingDist = calcHammingDist();
        manhattanDist = calcMahattanDist();
        boardGoal = calcBoardGoal();
        findBlankTile();
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", getTile(i, j)));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        return hammingDist;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattanDist;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return boardGoal;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        boolean retVal;
        // basic checks
        if (this == y)
            return true;
        if (y == null)
            return false;
        if (this.getClass() != y.getClass())
            return false;
        Board that = (Board) y;

        // check if the board size is the same
        retVal = n == that.n;

        if (retVal) {
            // check if the tiles are the same
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    if (getTile(i, j) != that.getTile(i, j))
                        return false;
        }
        return retVal;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        return calcNeighborList();
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] twinTiles = copy2DArray(tiles);
        int row1 = 0, col1 = 0, row2 = 0, col2 = 0;
        boolean found = false;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (getTile(i, j) != 0) {
                    if (!found) {
                        row1 = i;
                        col1 = j;
                        found = true;
                    } else {
                        row2 = i;
                        col2 = j;
                        break;
                    }
                }
            }
        }
        int temp = twinTiles[row1][col1];
        twinTiles[row1][col1] = twinTiles[row2][col2];
        twinTiles[row2][col2] = temp;
        return new Board(twinTiles);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        Board mBoard;
        mBoard = readBoard(args[0]);

        StdOut.println("mBoard ==");
        printBoard(mBoard);
        StdOut.println("mBoard.twin");
        printBoard(mBoard.twin());

        for (Board mNeighbor : mBoard.neighbors())
            printBoard(mNeighbor);

    }

    // read board from file
    private static Board readBoard(String fileName) {
        In in = new In(fileName);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        return new Board(tiles);
    }

    // print Hamming, Manahattan, isGoal, size and board
    private static void printBoard(Board mBoard) {
        StdOut.println("Hamming " + mBoard.hamming() + " Manahattan " + mBoard.manhattan());
        StdOut.println("isGoal " + mBoard.isGoal() + " size " + mBoard.dimension());
        StdOut.println(mBoard);
    }

    // save the blank tile position
    private void findBlankTile() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (getTile(i, j) == 0) {
                    blankRow = i;
                    blankCol = j;
                    return;
                }
            }
        }
    }

    // copy 2D array
    private int[][] copy2DArray(int[][] source) {
        int[][] retVal = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                retVal[i][j] = source[i][j];
        return retVal;
    }


    // calculate value of a tile in the goal board
    private int boardGoalVal(int row, int col) {
        int tileBoardVal = row * n + col + 1;
        if (tileBoardVal == n * n)
            tileBoardVal = 0;
        return tileBoardVal;
    }

    // calculate manhattan distance for a tile from its goal position
    // if the tile is 0, then it is the blank tile and the distance is 0
    private int mahattanDist(int row, int col) {
        int retVal = 0;
        if (getTile(row, col) != 0) {
            int goalRow = (getTile(row, col) - 1) / n;
            int goalCol = (getTile(row, col) - 1) % n;
            retVal = Math.abs(goalRow - row) + Math.abs(goalCol - col);
        }
        return retVal;
    }

    // calculate hamming distance as the number of tiles out of place
    private int calcHammingDist() {
        int dist = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (getTile(i, j) != 0 && getTile(i, j) != boardGoalVal(i, j))
                    dist++;
        return dist;
    }

    // calculate manhattan distance as the sum of the manhattan distances
    // of all tiles from their goal positions
    private int calcMahattanDist() {
        int dist = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (getTile(i, j) != 0 && getTile(i, j) != boardGoalVal(i, j))
                    dist += mahattanDist(i, j);
        return dist;
    }

    // check if the board is the goal board
    private boolean calcBoardGoal() {
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (getTile(i, j) != 0 && getTile(i, j) != boardGoalVal(i, j))
                    return false;
        return true;
    }

    // calculate the neighbors of the board of 1 possible blank tile move
    private LinkedQueue<Board> calcNeighborList() {
        LinkedQueue<Board> neighborList = new LinkedQueue<Board>();
        // Right neighbor
        if (blankCol < (n - 1)) {
            int[][] rightTiles = copy2DArray(tiles);
            // swap blank tile to the right
            int origTileVal = rightTiles[blankRow][blankCol + 1];
            rightTiles[blankRow][blankCol + 1] = rightTiles[blankRow][blankCol];
            rightTiles[blankRow][blankCol] = origTileVal;
            Board newBoard = new Board(rightTiles);
            neighborList.enqueue(newBoard);
        }
        // Top neighbor
        if (blankRow > 0) {
            int[][] topTiles = copy2DArray(tiles);
            // swap blank tile to the right
            int origTileVal = topTiles[blankRow - 1][blankCol];
            topTiles[blankRow - 1][blankCol] = topTiles[blankRow][blankCol];
            topTiles[blankRow][blankCol] = origTileVal;
            Board newBoard = new Board(topTiles);
            neighborList.enqueue(newBoard);
        }

        // Left neighbor
        if (blankCol > 0) {
            int[][] leftTiles = copy2DArray(tiles);
            // swap blank tile to the right
            int origTileVal = leftTiles[blankRow][blankCol - 1];
            leftTiles[blankRow][blankCol - 1] = leftTiles[blankRow][blankCol];
            leftTiles[blankRow][blankCol] = origTileVal;
            Board newBoard = new Board(leftTiles);
            neighborList.enqueue(newBoard);
        }
        // Bottom neighbor
        if (blankRow < (n - 1)) {
            int[][] bottomTiles = copy2DArray(tiles);
            // swap blank tile to the right
            int origTileVal = bottomTiles[blankRow + 1][blankCol];
            bottomTiles[blankRow + 1][blankCol] = bottomTiles[blankRow][blankCol];
            bottomTiles[blankRow][blankCol] = origTileVal;
            Board newBoard = new Board(bottomTiles);
            neighborList.enqueue(newBoard);
        }
        return neighborList;
    }

    // get the value of a tile
    private int getTile(int row, int col) {
        return tiles[row][col];
    }
}
