public class Main {
    public static void main(String[] args) {
        PrimitiveAlgorithms primitiveAlgorithms = new PrimitiveAlgorithms();

        Line l1 = new Line(1, 0, -2); // x - 2 = 0
        Line l2 = new Line(0, 1, -3); // y - 3 = 0

        System.out.println(primitiveAlgorithms.LineLineIntersection(l1, l2)); // Intersection at (2, 3)

        Segment s1 = new Segment(new Point(0, 0), new Point(4, 4)); // Diagonal from (0, 0) to (4, 4)
        Segment s2 = new Segment(new Point(0, 4), new Point(4, 0)); // Diagonal from (0, 4) to (4, 0)

        System.out.println(primitiveAlgorithms.SegmentSegmentIntersection(s1, s2)); // Should return true (they intersect)

        System.out.println(primitiveAlgorithms.PointLineDistance(new Point(2, 0), l2)); // Should return 3 (distance from (2, 0) to y = 3)

        // Distance from point (0, 2) to line defined by points (0, 0) and (4, 4)
        System.out.println(primitiveAlgorithms.PointLineDistance(new Point(0, 2), new Point(0, 0),  new Point(4, 4))); // Should return approximately 1.414 (sqrt(2))

        Disk disk1 = new Disk(new Point(0, 0), 3); // Center at (0, 0) with radius 3
        Disk disk2 = new Disk(new Point(4, 0), 3); // Center at (4, 0) with radius 3

        System.out.println(primitiveAlgorithms.DiskDiskIntersection(disk1, disk2)); // Should return true (they intersect)

        Triangle triangle1 = new Triangle(new Point(0, 0), new Point(4, 0), new Point(2, 3)); // Triangle with vertices at (0, 0), (4, 0), (2, 3)
        Triangle triangle2 = new Triangle(new Point(1, 1), new Point(5, 1), new Point(3, 4)); // Triangle with vertices at (1, 1), (5, 1), (3, 4)

        System.out.println(primitiveAlgorithms.TriangleTriangleIntersection(triangle1, triangle2)); // Should return true (they intersect)
    }
}