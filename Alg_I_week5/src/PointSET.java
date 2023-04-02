import edu.princeton.cs.algs4.LinkedQueue;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class PointSET {
    // SET red-black BST of points
    private final SET<Point2D> points;

    // construct an empty set of points
    public PointSET() {
        points = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.enableDoubleBuffering();
        for (Point2D pt : points)
            pt.draw();
        StdDraw.setPenRadius();
        StdDraw.show();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();

        LinkedQueue<Point2D> queue;
        queue = new LinkedQueue<Point2D>();
        for (Point2D pt : points)
            if (rect.contains(pt))
                queue.enqueue(pt);
        return queue;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        Point2D nearPt = null;
        double distSquare = Double.POSITIVE_INFINITY;
        for (Point2D pt : points) {
            double newDSQ = pt.distanceSquaredTo(p);
            if (distSquare > newDSQ) {
                nearPt = pt;
                distSquare = newDSQ;
            }
        }
        return nearPt;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        PointSET myPtSet = new PointSET();

        myPtSet.insert(new Point2D(0.7, 0.2));
        myPtSet.insert(new Point2D(0.5, 0.4));
        myPtSet.insert(new Point2D(0.2, 0.3));
        myPtSet.insert(new Point2D(0.2, 0.3));
        myPtSet.insert(new Point2D(0.9, 0.7));
        myPtSet.insert(new Point2D(0.4, 0.6));

        StdOut.println("size == " + myPtSet.size());
        myPtSet.draw();
        int i = 0;
        double halfSize = StdRandom.uniformDouble(0.0, 0.5);
        StdOut.println("halfSize " + halfSize);
        RectHV rectCheck = new RectHV(0.5 - halfSize, 0.5 - halfSize, 0.5 + halfSize, 0.5 + halfSize);
        StdDraw.setPenColor(StdDraw.MAGENTA);
        rectCheck.draw();
        StdDraw.show();
        for (Point2D pt : myPtSet.range(rectCheck))
            StdOut.println("" + (i++) + " = " + pt);
        Point2D pChk = new Point2D(0.5, 0.5);
        StdOut.println("Nearest point to " + pChk + " is " + myPtSet.nearest(pChk));
    }
}
