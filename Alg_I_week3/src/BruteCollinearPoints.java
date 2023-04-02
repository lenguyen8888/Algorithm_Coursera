import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
    private List<LineSegment> segments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        validatePoints(points);

        segments = new ArrayList<>();
        for (int iVal = 0; iVal < points.length; iVal++) {
            for (int jVal = iVal + 1; jVal < points.length; jVal++) {
                for (int kVal = jVal + 1; kVal < points.length; kVal++) {
                    for (int lvar = kVal + 1; lvar < points.length; lvar++) {
                        Point p = points[iVal];
                        Point q = points[jVal];
                        Point r = points[kVal];
                        Point s = points[lvar];
                        if (p.slopeTo(q) == p.slopeTo(r) && p.slopeTo(q) == p.slopeTo(s)) {
                            Point[] pointsInSegment = new Point[] { p, q, r, s };
                            Arrays.sort(pointsInSegment);
                            segments.add(new LineSegment(pointsInSegment[0], pointsInSegment[3]));
                        }
                    }
                }
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[segments.size()]);
    }

    /**
     * @param points
     */
    private void validatePoints(Point[] points) {
        if (points == null)
            throw new IllegalArgumentException();
        for (Point pt : points)
            if (pt == null)
                throw new IllegalArgumentException();
        // check for duplicates in the array
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0)
                    throw new IllegalArgumentException();
            }
        }
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
