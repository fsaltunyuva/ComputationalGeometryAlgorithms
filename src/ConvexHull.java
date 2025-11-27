import java.util.ArrayList;

public class ConvexHull {
    ArrayList<Point> points;
    public ArrayList<Segment> hullEdges = new ArrayList<>();
    public ArrayList<Point> hullVertices = new ArrayList<>();
    private PrimitiveAlgorithms primitiveAlgorithms = new PrimitiveAlgorithms();

    public ConvexHull(ArrayList<Point> points, ConvexHullConstructionAlgorithm algorithmChoice) {
        this.points = points;

        switch (algorithmChoice) {
            case Naive:
                NaiveAlgorithm();
                break;
            case JarvisMarch:
                //JarvisMarch();
                // TODO: Jarvis March
                break;
        }
    }

    private void NaiveAlgorithm(){
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
                    hullEdges.add(new Segment(pi, pj));
                }
            }
        }
    }

    private void JarvisMarch(){
        Point p_low = FindLowestPoint(points);
        Point initialNextPoint = new Point(p_low.x + 1, p_low.y); // Imaginary point (TODO: +1?)
        Point currentPoint = p_low;

        Point p_next = FindMinimumAngleTurnPoint(currentPoint, initialNextPoint, points, p_low);

        hullVertices.add(p_low);
        hullVertices.add(p_next);

        Point previousPoint = p_low;
        currentPoint = p_next;

        while(true){
            p_next = FindMinimumAngleTurnPoint(previousPoint, currentPoint, points, p_next);
            if (p_next.equals(p_low)) break;
            hullVertices.add(p_next);
            previousPoint = currentPoint;
            currentPoint = p_next;
        }
    }

    private Point FindMinimumAngleTurnPoint(Point previousPoint, Point currentPoint, ArrayList<Point> points, Point exclude){
        float squaredMaxCosValue = Float.MIN_VALUE;
        Point minAnglePoint = null;

        Vector pq = new Vector(previousPoint, currentPoint);

        for(int i = 0; i < points.size(); i++){
            if(points.get(i).equals(exclude)) continue;

            Vector qr = new Vector(currentPoint, points.get(i));

            // Sign of cosine
            boolean dotProductSign = primitiveAlgorithms.DotProduct(pq, qr) > 0; // true if positive

            // To avoid sqrt, we use squared cosθ
            float squaredCosTheta = (float) Math.pow(primitiveAlgorithms.DotProduct(pq, qr), 2) / (pq.SquaredLength() * qr.SquaredLength());

            if (!dotProductSign) squaredCosTheta *= -1;

            if(squaredCosTheta > squaredMaxCosValue){
                squaredMaxCosValue = squaredCosTheta;
                minAnglePoint = points.get(i);
            }
        }

        return minAnglePoint;
    }

    private Point FindLowestPoint(ArrayList<Point> points){
        float min = Float.MAX_VALUE;
        Point lowestPoint = points.get(0);

        for (int i = 0; i < points.size(); i++) {
            if(points.get(i).y < min){
                min = points.get(i).y;
                lowestPoint = points.get(i);
            }
        }

        return lowestPoint;
    }
}
