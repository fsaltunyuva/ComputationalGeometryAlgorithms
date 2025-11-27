import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        PrimitiveAlgorithms primitiveAlgorithms = new PrimitiveAlgorithms();

        // Line-Line Intersection
        Line l1 = new Line(1, 0, -2); // x - 2 = 0
        Line l2 = new Line(0, 1, -3); // y - 3 = 0

        System.out.println(primitiveAlgorithms.LineLineIntersection(l1, l2)); // Intersection at (2, 3)


        // Segment-Segment Intersection
        Segment s1 = new Segment(new Point(0, 0), new Point(4, 4)); // Diagonal from (0, 0) to (4, 4)
        Segment s2 = new Segment(new Point(0, 4), new Point(4, 0)); // Diagonal from (0, 4) to (4, 0)

        System.out.println(primitiveAlgorithms.SegmentSegmentIntersection(s1, s2)); // Should return true (they intersect)


        // Distance from point (2, 0) to line y = 3
        System.out.println(primitiveAlgorithms.PointLineDistance(new Point(2, 0), l2)); // Should return 3


        // Distance from point (0, 2) to line defined by points (0, 0) and (4, 4)
        System.out.println(primitiveAlgorithms.PointLineDistance(new Point(0, 2), new Point(0, 0),  new Point(4, 4))); // Should return approximately 1.414 (sqrt(2))


        // Disk-Disk Intersection
        Disk disk1 = new Disk(new Point(0, 0), 3); // Center at (0, 0) with radius 3
        Disk disk2 = new Disk(new Point(4, 0), 3); // Center at (4, 0) with radius 3

        System.out.println(primitiveAlgorithms.DiskDiskIntersection(disk1, disk2)); // Should return true (they intersect)


        // Triangle-Triangle Intersection
        Triangle triangle1 = new Triangle(new Point(0, 0), new Point(4, 0), new Point(2, 3)); // Triangle with vertices at (0, 0), (4, 0), (2, 3)
        Triangle triangle2 = new Triangle(new Point(1, 1), new Point(5, 1), new Point(3, 4)); // Triangle with vertices at (1, 1), (5, 1), (3, 4)

        System.out.println(primitiveAlgorithms.TriangleTriangleIntersection(triangle1, triangle2)); // Should return true (they intersect)


        // Convex Hull Algorithms
        ArrayList<Point> points = new ArrayList<>(Arrays.asList(
                new Point(0,0),
                new Point(4,1),
                new Point(3,3),
                new Point(3,1)
        ));

        ConvexHull naiveConvexHull = new ConvexHull(points, ConvexHullConstructionAlgorithm.Naive);
        System.out.println(naiveConvexHull.hullVertices);

        ConvexHull jarvisConvexHull = new ConvexHull(points, ConvexHullConstructionAlgorithm.JarvisMarch);
        System.out.println(jarvisConvexHull.hullVertices);

        ConvexHull grahamConvexHull = new ConvexHull(points, ConvexHullConstructionAlgorithm.GrahamScan);
        System.out.println(grahamConvexHull.hullVertices);


        // Sorting using Convex Hull
        ArrayList<Integer> numbersToBeSorted = new ArrayList<>(Arrays.asList(-4, 5, 3, 8, 1, -16, -3, 2));
        ArrayList<Integer> x2FunctionYValues = new ArrayList<>(); // x^2 = y

        for(Integer num : numbersToBeSorted) {
            x2FunctionYValues.add(num * num);
        }

        ArrayList<Point> CHPointsForSorting = new ArrayList<>();
        for(int i = 0; i < numbersToBeSorted.size(); i++) {
            CHPointsForSorting.add(new Point(numbersToBeSorted.get(i), x2FunctionYValues.get(i)));
        }

        ConvexHull sortingConvexHull = new ConvexHull(CHPointsForSorting, ConvexHullConstructionAlgorithm.GrahamScan);

        // Find the leftmost point to start printing from
        int start = 0;
        for (int i = 1; i < sortingConvexHull.hullVertices.size(); i++) {
            if (sortingConvexHull.hullVertices.get(i).x < sortingConvexHull.hullVertices.get(start).x) {
                start = i;
            }
        }

        // Print the x-coordinates of the convex hull vertices in order starting from the leftmost point
        for (int k = 0; k < sortingConvexHull.hullVertices.size(); k++) {
            Point p = sortingConvexHull.hullVertices.get((start + k) % sortingConvexHull.hullVertices.size());
            System.out.print(p.x + " ");
        }
    }
}