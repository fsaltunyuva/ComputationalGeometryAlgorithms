public class Main {
    public static void main(String[] args) {
        PrimitiveAlgorithms primitiveAlgorithms = new PrimitiveAlgorithms();

        Line l1 = new Line(1, 0, -2);
        Line l2 = new Line(0, 1, -3);


        System.out.println(primitiveAlgorithms.LineLineIntersection(l1, l2));

        Segment s1 = new Segment(new Point(0, 0), new Point(4, 4));
        Segment s2 = new Segment(new Point(0, 4), new Point(4, 0));

        System.out.println(primitiveAlgorithms.SegmentSegmentIntersection(s1, s2));

        Line l3 = new Line(0, 1, -3);
        System.out.println(primitiveAlgorithms.PointLineDistance(new Point(2, 0), l3));

        System.out.println(primitiveAlgorithms.PointLineDistance(new Point(0, 2), new Point(0, 0),  new Point(4, 4)));

        Disk disk1 = new Disk(new Point(0, 0), 3);
        Disk disk2 = new Disk(new Point(4, 0), 3);

        System.out.println(primitiveAlgorithms.DiskDiskIntersection(disk1, disk2));

        Triangle triangle1 = new Triangle(new Point(0, 0), new Point(4, 0), new Point(2, 3));
        Triangle triangle2 = new Triangle(new Point(1, 1), new Point(5, 1), new Point(3, 4));

        System.out.println(primitiveAlgorithms.TriangleTriangleIntersection(triangle1, triangle2));
    }
}