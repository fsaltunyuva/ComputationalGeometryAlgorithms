import java.util.ArrayList;

public class ConvexHull {
    ArrayList<Point> points;
    public ArrayList<Segment> hullEdges = new ArrayList<>();
    private PrimitiveAlgorithms primitiveAlgorithms = new PrimitiveAlgorithms();

    public ConvexHull(ArrayList<Point> points, ConvexHullConstructionAlgorithm algorithmChoice) {
        this.points = points;

        switch (algorithmChoice) {
            case Naive:
                NaiveAlgorithm();
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
}
