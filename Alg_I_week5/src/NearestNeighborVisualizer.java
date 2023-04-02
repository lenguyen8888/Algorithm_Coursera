
/******************************************************************************
 *  Compilation:  javac NearestNeighborVisualizer.java
 *  Execution:    java NearestNeighborVisualizer input.txt
 *  Dependencies: PointSET.java KdTree.java
 *
 *  Read points from a file (specified as a command-line argument) and
 *  draw to standard draw. Highlight the closest point to the mouse.
 *
 *  The nearest neighbor according to the brute-force algorithm is drawn
 *  in red; the nearest neighbor using the kd-tree algorithm is drawn in blue.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class NearestNeighborVisualizer {

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
        StdOut.println("kdtree.size " + kdtree.size());

        // process nearest neighbor queries
        StdDraw.enableDoubleBuffering();
        int errCnt = 0;
        final boolean USE_DEBUG = false;
        while (true) {

            Point2D brutePt, kdPt;
            // the location (x, y) of the mouse
            double x = StdDraw.mouseX();
            double y = StdDraw.mouseY();
            if (USE_DEBUG) {
                // (0.89453125, 0.390625)
                // 0.58203125, 0.2421875
                x = 0.58203125;
                y = 0.2421875;
            }
            Point2D query = new Point2D(x, y);

            // draw all of the points
            StdDraw.clear();
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            brute.draw();

            // draw in red the nearest neighbor (using brute-force algorithm)
            StdDraw.setPenRadius(0.03);
            StdDraw.setPenColor(StdDraw.RED);
            brutePt = brute.nearest(query);
            brutePt.draw();
            StdDraw.setPenRadius(0.02);

            // draw in blue the nearest neighbor (using kd-tree algorithm)
            StdDraw.setPenColor(StdDraw.BLUE);
            kdPt = kdtree.nearest(query);
            kdPt.draw();
            boolean testFailed = query.distanceSquaredTo(kdPt) != query.distanceSquaredTo(brutePt);
            if (!kdPt.equals(brutePt)) {
                if (testFailed) {
                    StdOut.print("errCnt == " + (errCnt++) + ": query " + query);
                    StdOut.print(",kdPt == " + kdPt + " kdDSQ == " + kdPt.distanceSquaredTo(query));
                    StdOut.println(",brutePt == " + brutePt + " bruteDSQ == " + brutePt.distanceSquaredTo(query));
                }
            }
            StdDraw.show();
            StdDraw.pause(40);
        }
    }
}
