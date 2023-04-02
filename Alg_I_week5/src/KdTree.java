import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinkedQueue;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class KdTree {

    // DEBUG_ON = true: draw the tree
    // DEBUG_NEAREST = true: draw the nearest point
    // DEBUG_INSERT = true: draw the inserted point
    // DEBUG_FIND = true: draw the found point
    private static final boolean DEBUG_ON = false;
    private static final boolean DEBUG_NEAREST = false;
    private static final boolean DEBUG_INSERT = false;
    private static final boolean DEBUG_FIND = false;

    // TOP_RECT is the rectangle that contains all points
    private static final RectHV TOP_RECT = new RectHV(0.0, 0.0, 1.0, 1.0);

    // root of the tree
    private Node root;
    // number of nodes in the tree
    private int rootSize;

    // distance squared between the query point and the best point found so far
    private double bestDistSQ;

    // findNearestCallCount is the number of times findNearestNode() is called
    private int findNearestCallCount;

    // construct an empty set of points
    public KdTree() {
        root = null;
        rootSize = 0;
        resetBestDistSQ();
    }

    // is the set empty?
    public boolean isEmpty() {
        return (root == null);
    }

    // number of points in the set
    public int size() {
        return rootSize;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        root = insert(root, true, p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        Node kdN = findNode(root, true, p);
        return kdN != null && kdN.p.equals(p);
    }

    // draw all points to standard draw
    public void draw() {
        if (!DEBUG_ON)
            StdDraw.enableDoubleBuffering();
        else
            StdDraw.disableDoubleBuffering();
        draw(root, true, TOP_RECT);

        if (!DEBUG_ON)
            StdDraw.show();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();

        LinkedQueue<Point2D> queue;
        queue = new LinkedQueue<Point2D>();
        range(root, true, TOP_RECT, rect, queue);
        return queue;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();

        if (size() == 0)
            return null;
        resetBestDistSQ();
        Node nearestN = findNearestNode(root, true, p, TOP_RECT);
        return nearestN.p;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        // initialize the two data structures with point from file
        String filename = args[0];
        In in = new In(filename);
        PointSET brute = new PointSET();
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
            brute.insert(p);
        }

        Point2D query = new Point2D(0.5, 0.0); // (0.4, 0.7);
        // (0.45, 0.29);// (0.322265625, 0.322265625);
        // (0.626953125, 0.5);
        // (0.58203125, 0.2421875);

        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.ORANGE);
        query.draw();
        StdDraw.setPenRadius();
        StdDraw.show();

        kdtree.draw();
        StdOut.println("kdtree.size " + kdtree.size());
        for (Point2D pt : kdtree.range(TOP_RECT))
            StdOut.println(pt);
        StdOut.println("kdtree contains query " + query + " is " + kdtree.contains(query));

        Point2D brutePt = brute.nearest(query);
        Point2D kdPt = kdtree.nearest(query);

        StdOut.print("query " + query);
        StdOut.print(",kdPt == " + kdPt + " kdDSQ == " + kdPt.distanceSquaredTo(query));
        StdOut.println(",brutePt == " + brutePt + " bruteDSQ == " + brutePt.distanceSquaredTo(query));

        // oldTest();

    }

    // Private helper classes and methods

    // Node is a node in the KdTree data structure
    // Each node contains a point, and references to the left/bottom and right/top
    // subtrees
    private static class Node {
        // the point p stored in this node
        private final Point2D p;
        // the left/bottom subtree lb and right/top subtree rt
        private Node lb;
        private Node rt;

        public Node(Point2D p, Node lb, Node rt) {
            this.p = p;
            this.lb = lb;
            this.rt = rt;
        }
    }

    private Node insert(Node currNode, boolean cmpX, Point2D p) {

        // If the current node is null, then we've reached a leaf node
        if (currNode == null) {
            rootSize++;
            return new Node(p, null, null);
        }
        // compare current node's point so we can decide which subtree to insert
        int compareRes = kdCompare(cmpX, p, currNode.p);
        if (DEBUG_INSERT) {
            StdOut.print("(insert) p " + p + " currNode.p" + currNode.p);
            StdOut.println(" cmpX " + cmpX + " compareRes " + compareRes);
        }
        // If the point is less than the current node's point, then insert into the
        // left/bottom subtree
        if (compareRes < 0) {
            currNode.lb = insert(currNode.lb, !cmpX, p);

            // If the point is greater than the current node's point,
            // then insert into the right/top subtree
        } else if (compareRes > 0 || !currNode.p.equals(p)) {
            currNode.rt = insert(currNode.rt, !cmpX, p);
        }
        // Return the current node for the recursive calls
        return currNode;
    }

    private Node findNearestNode(Node currNode, boolean cmpX, Point2D p, RectHV currRect) {

        // Skip this node if it's null or if the distance from the bounding box is
        // greater or the same as the best/shortest distance so far
        if (currNode == null || currRect.distanceSquaredTo(p) >= getBestDistSQ())
            return null;

        findNearestCallCount++;
        // Compare the current node's point with the query point
        int compareRes = kdCompare(cmpX, p, currNode.p);
        RectHV rtRect = getRTRect(cmpX, currNode.p, currRect);
        RectHV lbRect = getLBRect(cmpX, currNode.p, currRect);

        double currDSQ = currNode.p.distanceSquaredTo(p);
        if (DEBUG_NEAREST)
            StdOut.println("count " + findNearestCallCount + " " + currNode.p + currDSQ);
        setbestDistSQ(currDSQ);
        double rtDSQ, lbDSQ;

        Node rtN = null;
        Node lbN = null;

        if (compareRes < 0) {
            // There's chances for a shorter distance
            lbN = findNearestNode(currNode.lb, !cmpX, p, lbRect);
            rtN = findNearestNode(currNode.rt, !cmpX, p, rtRect);
        } else {

            // Swap order base on compareRes
            rtN = findNearestNode(currNode.rt, !cmpX, p, rtRect);
            lbN = findNearestNode(currNode.lb, !cmpX, p, lbRect);
        }
        rtDSQ = Double.POSITIVE_INFINITY;
        if (rtN != null)
            rtDSQ = nodeDistanceSquaredTo(rtN, p);
        lbDSQ = Double.POSITIVE_INFINITY;
        if (lbN != null)
            lbDSQ = nodeDistanceSquaredTo(lbN, p);
        Node nearerN = null;
        double nearerDSQ;
        if (compareRes < 0) {
            // Prefer lb over rt Node
            if (lbDSQ <= rtDSQ) {
                nearerN = lbN;
                nearerDSQ = lbDSQ;
            } else {
                nearerN = rtN;
                nearerDSQ = rtDSQ;
            }
        } else {
            // Prefer rt over lb Node
            if (rtDSQ <= lbDSQ) {
                nearerN = rtN;
                nearerDSQ = rtDSQ;
            } else {
                nearerN = lbN;
                nearerDSQ = lbDSQ;
            }

        }

        if (nearerN != null && nearerDSQ <= currDSQ)
            return nearerN;
        else
            return currNode;

    }

    private double nodeDistanceSquaredTo(Node n, Point2D p) {
        return n.p.distanceSquaredTo(p);
    }

    private Node findNode(Node currNode, boolean cmpX, Point2D p) {
        if (currNode == null)
            return null;
        int compareRes = kdCompare(cmpX, p, currNode.p);
        if (DEBUG_FIND) {
            StdOut.print("(find) p " + p + " currNode.p" + currNode.p);
            StdOut.println(" cmpX " + cmpX + " compareRes " + compareRes);
        }
        if (compareRes < 0) {
            return findNode(currNode.lb, !cmpX, p);
        } else if (currNode.p.equals(p)) {
            return currNode;
        } else { // if (compareRes <= 0) {
            return findNode(currNode.rt, !cmpX, p);
        }
    }

    private void draw(Node currNode, boolean cmpX, RectHV rect) {
        if (currNode == null) {
            return;
        }
        drawPoint(currNode.p, cmpX, rect);

        RectHV rtRect = getRTRect(cmpX, currNode.p, rect);
        draw(currNode.rt, !cmpX, rtRect);

        RectHV lbRect = getLBRect(cmpX, currNode.p, rect);
        draw(currNode.lb, !cmpX, lbRect);
    }

    private void range(Node currNode, boolean cmpX, RectHV currRect, RectHV rect, LinkedQueue<Point2D> queue) {
        if (currNode == null || !currRect.intersects(rect)) {
            return;
        }

        if (rect.contains(currNode.p))
            queue.enqueue(currNode.p);

        RectHV rtRect = getRTRect(cmpX, currNode.p, currRect);
        range(currNode.rt, !cmpX, rtRect, rect, queue);

        RectHV lbRect = getLBRect(cmpX, currNode.p, currRect);
        range(currNode.lb, !cmpX, lbRect, rect, queue);
    }

    private void drawPoint(Point2D p, boolean cmpX, RectHV rect) {
        if (cmpX) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            StdDraw.point(p.x(), p.y());
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            StdDraw.line(p.x(), rect.ymin(), p.x(), rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            StdDraw.point(p.x(), p.y());
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            StdDraw.line(rect.xmin(), p.y(), rect.xmax(), p.y());
        }
    }

    private int kdCompare(boolean cmpX, Point2D p1, Point2D p2) {
        if (cmpX)
            return Point2D.X_ORDER.compare(p1, p2);
        else
            return Point2D.Y_ORDER.compare(p1, p2);
    }

    private RectHV getRTRect(boolean cmpX, Point2D p, RectHV rect) {
        // When the new point is to the Right or to the Top the root point
        // set the lower bound of the new rectangle
        if (rect == null)
            return null;
        if (cmpX)
            return new RectHV(p.x(), rect.ymin(), rect.xmax(), rect.ymax());
        else
            return new RectHV(rect.xmin(), p.y(), rect.xmax(), rect.ymax());
    }

    private RectHV getLBRect(boolean cmpX, Point2D p, RectHV rect) {
        // When the new point is to the Left or to the Bottom the root point
        // set the upper bound of the new rectangle
        if (rect == null)
            return null;
        if (cmpX)
            return new RectHV(rect.xmin(), rect.ymin(), p.x(), rect.ymax());
        else
            return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), p.y());
    }

    private void setbestDistSQ(double newVal) {
        if (newVal < bestDistSQ)
            bestDistSQ = newVal;
    }

    private void resetBestDistSQ() {
        findNearestCallCount = 0;
        bestDistSQ = Double.POSITIVE_INFINITY;
    }

    private double getBestDistSQ() {
        return bestDistSQ;
    }

}
