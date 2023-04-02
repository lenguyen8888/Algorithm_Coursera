import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinkedStack;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private static final boolean DEBUG_ON = false;
    private LinkedStack<Board> mainSteps;
    private final Board mainBoard, twinBoard;
    private boolean solvable;
    private int mainQSize, twinQSize;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException();
        mainBoard = initial;
        twinBoard = mainBoard.twin();
        solvePuzzle();
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (mainSteps == null)
            return -1;
        else
            return mainSteps.size() - 1;

    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return mainSteps;
    }

    // test client (see below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        long startTime = System.currentTimeMillis();
        // solve the puzzle
        Solver solver = new Solver(initial);
        long endTime = System.currentTimeMillis();
        int printOption = args.length > 1 ? Integer.parseInt(args[1]) : -1;
        if (printOption > 0) {
            StdOut.println("Runtime == " + (1.0 * (endTime - startTime)) + " secs");
            StdOut.println("mainQSize == " + solver.mainQSize + "; twinQSize == " + solver.twinQSize);
        }

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            int step = 0;
            if (args.length <= 1 || printOption > 1)
                for (Board board : solver.solution()) {
                    StringBuilder s;
                    s = new StringBuilder();
                    s.append("M " + board.manhattan());
                    s.append(" : pri " + (board.manhattan() + step));
                    s.append("\tstep " + (step++));
                    StdOut.println(s);
                    StdOut.println(board);
                }
        }
    }

    private void solvePuzzle() {
        MinPQ<StepNode> mainQ, twinQ;
        mainQ = new MinPQ<StepNode>();
        twinQ = new MinPQ<StepNode>();

        StepNode firstMainStep = new StepNode(null, mainBoard, 0);
        mainQ.insert(firstMainStep);

        StepNode firstTwinStep = new StepNode(null, twinBoard, 0);
        twinQ.insert(firstTwinStep);
        boolean solverDone = false;
        StepNode mainSol = null, twinSol = null;
        for (int stepCount = 1; !solverDone; stepCount++) {
            if (DEBUG_ON)
                StdOut.println("Step == " + stepCount);
            mainSol = foundGoal(mainQ);
            twinSol = foundGoal(twinQ);
            if (mainSol != null || twinSol != null)
                solverDone = true;
        }
        mainQSize = mainQ.size();
        twinQSize = twinQ.size();
        solvable = mainSol != null;
        if (solvable) {
            mainSteps = new LinkedStack<Board>();
            StepNode stepTrace = mainSol;
            while (stepTrace != null) {
                mainSteps.push(stepTrace.getBoard());
                stepTrace = stepTrace.prev();
            }
        }
    }

    private StepNode foundGoal(MinPQ<StepNode> queue) {
        StepNode minNode = queue.delMin();
        if (minNode.isGoal()) {
            return minNode;
        }
        Board prevBoard = minNode.getPrevBoard();
        Board board = minNode.getBoard();
        for (Board neighbor : board.neighbors()) {
            if (!neighbor.equals(prevBoard)) {
                StepNode newNode = new StepNode(minNode, neighbor, minNode.moves + 1);
                queue.insert(newNode);
            }
        }
        return null;
    }

    private class StepNode implements Comparable<StepNode> {
        private final StepNode prevStep;
        private final Board board;
        private final int moves;
        private final boolean boardGoal;
        private final int manhattan;

        public StepNode(StepNode prevStep, Board board, int moves) {
            this.prevStep = prevStep;
            this.board = board;
            this.moves = moves;
            manhattan = board.manhattan();
            boardGoal = board.isGoal();
        }

        public Board getPrevBoard() {
            if (prevStep == null)
                return null;
            else
                return prevStep.getBoard();
        }

        public StepNode prev() {
            return prevStep;
        }

        public Board getBoard() {
            return board;
        }

        public boolean isGoal() {
            return boardGoal;
        }

        public int compareTo(StepNode that) {

            return manhattanPriority(that);
        }

        private int manhattanPriority(StepNode that) {
            return (manhattan + moves) - (that.manhattan + that.moves);
        }

        // string representation of this board
        public String toString() {
            StringBuilder s = new StringBuilder();
            s.append("\n");
            s.append(" priority " + manhattan);
            s.append(" moves " + moves);
            s.append(" Man " + (manhattan + moves));
            s.append("\n");
            s.append(board);
            return s.toString();
        }

    }

}
