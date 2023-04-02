import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinkedStack;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {

    // private Point[] mPoints;
    private final Point[] mPoints;

    // collinear segments as LinkedStack
    private final LinkedStack<LineSegment> colinearSegs;

    // start and end points of the current line segment
    private Point startPt, endPt;

    // cached slopes to points before the current point
    private double[] prevStartSlope;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        validatePoints(points);

        // copy the points array to mPoints
        mPoints = new Point[points.length];
        for (int i = 0; i < mPoints.length; i++)
            mPoints[i] = points[i];
 
        // initialize colinearSegs
        colinearSegs = new LinkedStack<LineSegment>();

        findLineSegments();
    }

    // the number of line segments
    public int numberOfSegments() {
        return colinearSegs.size();
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] retVal = new LineSegment[numberOfSegments()];
        int i = colinearSegs.size() - 1; // take output in the reverse order
        for (LineSegment seg : colinearSegs)
            retVal[i--] = seg;

        return retVal;
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

    private void findLineSegments() {
        int numPoints = mPoints.length;
        for (int iIndex = 0; iIndex < (numPoints - 3); iIndex++) {
            // realign the order
            Arrays.sort(mPoints, iIndex, numPoints);
            // sort by slope order
            Arrays.sort(mPoints, iIndex, numPoints, mPoints[iIndex].slopeOrder());

            // save the start point
            startPt = mPoints[iIndex];
            // reset end point
            endPt = null;
            // find collinear points from iIndex + 3 to the end
            for (int lIndex = iIndex + 3; lIndex < numPoints; lIndex++) {
                findColinearPts(iIndex, lIndex);
            }
        }
    }

    // Reuse code from BruteCollinear
    private void findColinearPts(int iIndex, int lIndex) {
        // array of slopes from i to the last 3 points
        double[] slopes = new double[3];
        for (int i = 0; i < 3; i++) {
            slopes[i] = mPoints[iIndex].slopeTo(mPoints[lIndex - i]);
        }
        // return if the 3 slopes are not equal
        if (slopes[0] != slopes[1] || slopes[1] != slopes[2])
            return;

        if (findEarlierStartPt(iIndex, slopes[0]))
            return;

        LineSegment newSeg = new LineSegment(mPoints[iIndex], mPoints[lIndex]);
        if (endPt != null && startPt.slopeTo(endPt) == slopes[0]) {

            // Take out a shorter sub-segment
            if (mPoints[iIndex].equals(startPt) && mPoints[lIndex].compareTo(endPt) > 0)
                colinearSegs.pop();
        }
        colinearSegs.push(newSeg);
        endPt = mPoints[lIndex];
    }

    // find earlier start point that has the same slope
    private boolean findEarlierStartPt(int currIndex, double slope) {
        if (currIndex < 1) {
            // initialize prevStartSlope
            prevStartSlope = null;
            return false;
        }
        // recalculate prevStartSlope if needed
        if (prevStartSlope == null || prevStartSlope.length < currIndex) {
            // calculate prevStartSlope for all slopes to points before currIndex
            prevStartSlope = new double[currIndex];
            for (int i = 0; i < currIndex; i++) {
                prevStartSlope[i] = mPoints[currIndex].slopeTo(mPoints[i]);
            }
            // sort prevStartSlopes
            Arrays.sort(prevStartSlope);
        }
        return Arrays.binarySearch(prevStartSlope, slope) >= 0;
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
        // check for duplicate points
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0)
                    throw new IllegalArgumentException();
            }
        }
    }

}
