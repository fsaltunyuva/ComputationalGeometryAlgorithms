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
                // TODO: Jarvis March
                break;
            case GrahamScan:
                GrahamScan();
                break;
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
        Point p_low = FindLowestPoint(points);
        Point previousPoint = new Point(p_low.x - 1, p_low.y);
        Point currentPoint = p_low;

        Point p_next = FindMinimumAngleTurnPoint(previousPoint, currentPoint, points, currentPoint);

        hullVertices.add(p_low);
        hullVertices.add(p_next);

        previousPoint = currentPoint;
        currentPoint = p_next;

        while(true){
            p_next = FindMinimumAngleTurnPoint(previousPoint, currentPoint, points, currentPoint);
            if (p_next.equals(p_low)) break;
            hullVertices.add(p_next);
            previousPoint = currentPoint;
            currentPoint = p_next;
        }
    }

    private Point FindMinimumAngleTurnPoint(Point previousPoint, Point currentPoint, ArrayList<Point> points, Point exclude){
        float minAngle = Float.MAX_VALUE;
        Point minAnglePoint = null;

        Vector pq = new Vector(previousPoint, currentPoint);

        for(int i = 0; i < points.size(); i++){
            if(points.get(i).equals(exclude)) continue;

            Vector qr = new Vector(currentPoint, points.get(i));

            float CosTheta = primitiveAlgorithms.DotProduct(pq, qr) / (pq.Magnitude() * qr.Magnitude());

            float theta = (float) Math.acos(CosTheta);

            if(theta < minAngle){
                minAngle = theta;
                minAnglePoint = points.get(i);
            }
        }

        return minAnglePoint;
    }

    private Point FindLowestPoint(ArrayList<Point> points){
        Point lowestPoint = points.get(0);
        for (Point p : points) {
            if (p.y < lowestPoint.y ||
                    (p.y == lowestPoint.y && p.x < lowestPoint.x)) {
                lowestPoint = p;
            }
        }
        return lowestPoint;
    }
//endregion

    //region Graham Scan
    private void GrahamScan(){
        Point p_low = FindLowestPoint(points);

        ArrayList<Point> pts = new ArrayList<>(points);
        pts.remove(p_low);
        RadialSortAroundPoint(p_low, pts);

        Stack<Point> hull = new Stack<>();
        hull.push(p_low);

        for(Point p : pts){
            hull.push(p);

            while (hull.size() > 2 &&
                    primitiveAlgorithms.Orientation(hull.get(hull.size() - 3), hull.get(hull.size() - 2), hull.get(hull.size() - 1)) == Direction.Right) {
                hull.remove(hull.size() - 2);
            }
        }

        hullVertices = new ArrayList<>(hull);
    }

    private ArrayList<Point> RadialSortAroundPoint(Point p, ArrayList<Point> points){
        points.sort((a, b) -> {
            double angleA = Math.atan2(a.y - p.y, a.x - p.x);
            double angleB = Math.atan2(b.y - p.y, b.x - p.x);
            return Double.compare(angleA, angleB);
        });
        return points;
    }
    //endregion
}
