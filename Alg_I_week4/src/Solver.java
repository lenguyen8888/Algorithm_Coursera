import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinkedStack;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    // stack to save the steps to solve the puzzle
    private LinkedStack<Board> mainSteps;
    // mainBoard is the initial board, twinBoard is the twin of the initial board
    // one of them is solvable, this is to detect if the initial board is solvable
    private final Board mainBoard, twinBoard;

    // solvable is true if the initial board is solvable
    private boolean solvable;

    // cache the size of the mainQ and twinQ
    private int mainQSize, twinQSize;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException();

        // initialize mainBoard and twinBoard
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

        // 2 priority queues, one for mainBoard, one for twinBoard
        MinPQ<StepNode> mainQ, twinQ;
        mainQ = new MinPQ<StepNode>();
        twinQ = new MinPQ<StepNode>();

        // insert the first step for mainBoard and twinBoard
        StepNode firstMainStep = new StepNode(null, mainBoard, 0);
        mainQ.insert(firstMainStep);

        // similar to the mainBoard, but for the twinBoard
        StepNode firstTwinStep = new StepNode(null, twinBoard, 0);
        twinQ.insert(firstTwinStep);
        
        boolean solverDone = false;
        StepNode mainSol = null, twinSol = null;
        for (int stepCount = 1; !solverDone; stepCount++) {
            mainSol = foundGoal(mainQ);
            twinSol = foundGoal(twinQ);

            // if either mainSol or twinSol is not null, then the puzzle is solved
            if (mainSol != null || twinSol != null)
                solverDone = true;
        }
        mainQSize = mainQ.size();
        twinQSize = twinQ.size();
        solvable = mainSol != null;

        // if the puzzle is solvable, then save the steps to solve the puzzle
        if (solvable) {
            mainSteps = new LinkedStack<Board>();
            StepNode stepTrace = mainSol;
            // save the steps to solve the puzzle in a stack
            while (stepTrace != null) {
                mainSteps.push(stepTrace.getBoard());
                stepTrace = stepTrace.prev();
            }
        }
    }

    // find the goal node in the queue, if found, return the goal node
    private StepNode foundGoal(MinPQ<StepNode> queue) {

        // get the node with the minimum priority (best solution)
        StepNode minNode = queue.delMin();
        if (minNode.isGoal()) {
            return minNode;
        }

        // get the previous board and the current board
        Board prevBoard = minNode.getPrevBoard();
        Board board = minNode.getBoard();
        // check all the neighbors of the current board
        for (Board neighbor : board.neighbors()) {
            // don't add the previous board to the queue again
            if (!neighbor.equals(prevBoard)) {
                StepNode newNode = new StepNode(minNode, neighbor, minNode.moves + 1);
                queue.insert(newNode);
            }
        }
        // if the goal is not found, return null
        return null;
    }

    private class StepNode implements Comparable<StepNode> {

        // the previous step
        private final StepNode prevStep;
        // the board of the current step
        private final Board board;

        // the number of moves to reach the current step for A* algorithm
        private final int moves;
        // whether the current board is the goal board
        private final boolean boardGoal;
        // the manhattan distance of the current board
        private final int manhattan;

        // create a new step node
        public StepNode(StepNode prevStep, Board board, int moves) {
            this.prevStep = prevStep;
            this.board = board;
            this.moves = moves;
            manhattan = board.manhattan();
            boardGoal = board.isGoal();
        }

        // get the previous board
        public Board getPrevBoard() {
            if (prevStep == null)
                return null;
            else
                return prevStep.getBoard();
        }

        // get the previous step
        public StepNode prev() {
            return prevStep;
        }

        // get the current board
        public Board getBoard() {
            return board;
        }

        // board is the goal board?
        public boolean isGoal() {
            return boardGoal;
        }

        // compare the priority of two step nodes
        public int compareTo(StepNode that) {
            return manhattanPriority(that);
        }

        //  manhattan priority
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
