import java.util.*;

public class ConvexHull {
    ArrayList<Point> points;
    public ArrayList<Point> hullVertices = new ArrayList<>();
    private PrimitiveAlgorithms primitiveAlgorithms = new PrimitiveAlgorithms();

    public ConvexHull(ArrayList<Point> points, ConvexHullConstructionAlgorithm algorithmChoice) {
        this.points = points;
        hullVertices.clear();

        switch (algorithmChoice) {
            case Naive:
                NaiveAlgorithm();
                break;
            case JarvisMarch:
                JarvisMarch();
                break;
            case GrahamScan:
                GrahamScan();
                break;
            case Chans:
                ChansAlgorithm();
                break;
            default:
                throw new IllegalArgumentException("Invalid Convex Hull Algorithm Choice");
        }
    }

    //region Naive Algorithm
    private void NaiveAlgorithm(){
        HashSet<Point> hullPoints = new HashSet<>();

        for (int i = 0; i < points.size(); i++) {
            for (int j = 0; j < points.size(); j++) {
                if (i == j) continue;

                Point pi = points.get(i);
                Point pj = points.get(j);

                // Are all points on the left?
                boolean allLeft = true;

                for (int k = 0; k < points.size(); k++) {
                    if (k == i || k == j) continue;

                    Point pk = points.get(k);
                    Direction dir = primitiveAlgorithms.Orientation(pi, pj, pk);

                    if (dir == Direction.Right) {
                        allLeft = false;
                        break;
                    }
                }

                if (allLeft) {
                    hullPoints.add(pi);
                    hullPoints.add(pj);
                }
            }
        }
        hullVertices = new ArrayList<>(hullPoints);
    }
    //endregion

    //region Jarvis March
    private void JarvisMarch(){
        Point p_low = FindLowestPoint(points); // Starting point
        Point previousPoint = new Point(p_low.x - 1, p_low.y); // Imaginary point to the left of p_low
        Point currentPoint = p_low; // Current point on the hull

        Point p_next = FindMinimumAngleTurnPoint(previousPoint, currentPoint, points, currentPoint); // First hull point

        // Add the first two hull points
        hullVertices.add(p_low);
        hullVertices.add(p_next);

        previousPoint = currentPoint;
        currentPoint = p_next;

        while(true){
            p_next = FindMinimumAngleTurnPoint(previousPoint, currentPoint, points, currentPoint); // Next hull point
            if (p_next.equals(p_low)) break; // Completed the hull
            hullVertices.add(p_next); // Add next hull point

            // Update previous and current points
            previousPoint = currentPoint;
            currentPoint = p_next;
        }
    }

    private Point FindMinimumAngleTurnPoint(Point previousPoint, Point currentPoint, ArrayList<Point> points, Point exclude){
        float minAngle = Float.MAX_VALUE; // Initialize to maximum possible angle
        Point minAnglePoint = null; // Point with minimum angle

        Vector pq = new Vector(previousPoint, currentPoint); // Vector from previous to current point

        for(int i = 0; i < points.size(); i++){
            if(points.get(i).equals(exclude)) continue; // Skip the excluded point (e.g., current point)

            Vector qr = new Vector(currentPoint, points.get(i)); // Vector from current to candidate point

            float CosTheta = primitiveAlgorithms.DotProduct(pq, qr) / (pq.Magnitude() * qr.Magnitude()); // Cosine of the angle between pq and qr

            float theta = (float) Math.acos(CosTheta); // Angle in radians

            if(theta < minAngle){ // Update minimum angle and point if smaller angle found
                minAngle = theta;
                minAnglePoint = points.get(i);
            }
        }

        return minAnglePoint; // Return the point with the minimum angle
    }
//endregion

    //region Graham Scan
    private void GrahamScan(){
        Point p_low = FindLowestPoint(points); // Find the point with the lowest y-coordinate

        ArrayList<Point> pts = new ArrayList<>(points); // Copy of points to not modify original
        pts.remove(p_low); // Remove the lowest point from the list
        RadialSortAroundPoint(p_low, pts); // Sort points radially around p_low

        // Initialize the hull stack with the lowest point
        Stack<Point> hull = new Stack<>();
        hull.push(p_low);

        for(Point p : pts){
            hull.push(p); // Add the current point to the hull

            // While the last three points make a right turn, remove the middle point
            while (hull.size() > 2 &&
                    primitiveAlgorithms.Orientation(hull.get(hull.size() - 3), hull.get(hull.size() - 2), hull.get(hull.size() - 1)) == Direction.Right) {
                hull.remove(hull.size() - 2);
            }
        }

        hullVertices = new ArrayList<>(hull); // Convert stack to list for hull vertices
    }

    // Sort points radially around point p
    private ArrayList<Point> RadialSortAroundPoint(Point p, ArrayList<Point> points){
        points.sort((a, b) -> {
            double angleA = Math.atan2(a.y - p.y, a.x - p.x);
            double angleB = Math.atan2(b.y - p.y, b.x - p.x);
            return Double.compare(angleA, angleB);
        });
        return points;
    }
    //endregion

    //region Chan's Algorithm
    // TODO: There may be issues in some inputs
    private void ChansAlgorithm(){
        hullVertices.clear();

        for (int i = 0; i < 16; i++) {
            int m = (int) Math.pow(2, Math.pow(2, i)); // m = 2^(2^i)
            if (ChansAlgorithmStep(m)) break;
        }
    }

    private boolean ChansAlgorithmStep(int m) {
        hullVertices.clear();

        int n = points.size();
        ArrayList<Point> copiedPoints = new ArrayList<>(points);

        // r = ceil(n / m)
        int groupCount = (n + m - 1) / m;

        ArrayList<ArrayList<Point>> groups = new ArrayList<>();

        // Divide points into groups of size m
        for (int g = 0; g < groupCount; g++) {
            ArrayList<Point> group = new ArrayList<>();

            // Determine start and end indices for the group
            int start = g * m;
            int end = Math.min(start + m, n);

            // Add points to the group
            for (int idx = start; idx < end; idx++) {
                group.add(copiedPoints.get(idx));
            }

            // Add the group to the list of groups
            if (!group.isEmpty()) {
                groups.add(group);
            }
        }

        ArrayList<ConvexHull> convexHulls = new ArrayList<>();

        for (ArrayList<Point> group : groups)
            convexHulls.add(new ConvexHull(group, ConvexHullConstructionAlgorithm.GrahamScan)); // Compute convex hull of the group

        if(convexHulls.size() == 1){
            hullVertices.clear();
            hullVertices.addAll(convexHulls.get(0).hullVertices);
            return true;
        }

        Point p_low = FindLowestPoint(points); // Starting point
        Point previousPoint = new Point(p_low.x - 1, p_low.y); // Imaginary point to the left of p_low
        Point currentPoint = p_low; // Current point on the hull

        ArrayList<Point> currentTangentPoints = new ArrayList<>();

        for(ConvexHull convexHull : convexHulls){
            currentTangentPoints.add(BinarySearchTangentFromPoint(p_low, convexHull.hullVertices));
        }

        Point p_next = FindMinimumAngleTurnPoint(previousPoint, currentPoint, currentTangentPoints, currentPoint); // First hull point

        // Add the first two hull points
        hullVertices.add(p_low);
        hullVertices.add(p_next);

        previousPoint = currentPoint;
        currentPoint = p_next;

        int iterationCount = 0;

        while(iterationCount < m){
            currentTangentPoints.clear();
            currentTangentPoints = new ArrayList<>();

            for(ConvexHull convexHull : convexHulls){
                currentTangentPoints.add(BinarySearchTangentFromPoint(currentPoint, convexHull.hullVertices));
            }

            p_next = FindMinimumAngleTurnPoint(previousPoint, currentPoint, currentTangentPoints, currentPoint); // Next hull point
            if (p_next.equals(p_low)) return true; // Completed the hull
            hullVertices.add(p_next); // Add next hull point

            // Update previous and current points
            previousPoint = currentPoint;
            currentPoint = p_next;

            iterationCount++;
        }

        // Did not complete hull within m steps
        return false;
    }

    private Point BinarySearchTangentFromPoint(Point p, ArrayList<Point> hull) {
        int n = hull.size();
        if (n == 1) return hull.get(0);

        // Calculate angles from point p to all hull points
        float[] ang = new float[n];
        for (int i = 0; i < n; i++) {
            ang[i] = Angle(p, hull.get(i));
        }

        // Find the index of the minimum angle (rotation point) to start from the bitonic sequence
        int start = 0;
        for (int i = 1; i < n; i++) {
            if (ang[i] < ang[start]) start = i;
        }

        // Binary search on the bitonic sequence to find the maximum angle (tangent point)
        int low = 0, high = n - 1;
        while (low < high) {
            int mid = (low + high) / 2;

            int midIdx  = (start + mid) % n;
            int nextIdx = (start + mid + 1) % n;

            if (ang[midIdx] < ang[nextIdx]) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }

        int peakIdx = (start + low) % n;
        return hull.get(peakIdx);
    }

    // TODO: Using orientation might be better
    private float Angle(Point p1, Point p2){
        return (float) Math.atan2(p2.y - p1.y, p2.x - p1.x);
    }
    //endregion

    // Find the point with the lowest y-coordinate (and rightmost in case of degeneracy)
    private Point FindLowestPoint(ArrayList<Point> points){
        Point lowestPoint = points.get(0);
        for (Point p : points) {
            if (p.y < lowestPoint.y || (p.y == lowestPoint.y && p.x > lowestPoint.x)) {
                lowestPoint = p;
            }
        }
        return lowestPoint;
    }
}
