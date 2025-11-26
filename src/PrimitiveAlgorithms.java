public class PrimitiveAlgorithms {

    public Point LineLineIntersection(Line line1, Line line2) {
        float x_numerator = line2.b * line1.c - line1.b * line2.c;
        float x_denominator = line2.a * line1.b - line1.a * line2.b;

        float y_numerator = line2.a * line1.c - line1.a * line2.c;
        float y_denominator = line2.b * line1.a - line1.b * line2.a;

        return new Point(x_numerator / x_denominator, y_numerator / y_denominator);
    }

    public boolean SegmentSegmentIntersection(Segment segment1, Segment segment2) {
        // Segment 1: pq
        // Segment 2: rs

        Direction pDirectionToRsLine = Orientation(segment2.start, segment2.end, segment1.start);
        Direction qDirectionToRsLine = Orientation(segment2.start, segment2.end, segment1.end);

        if (pDirectionToRsLine.equals(qDirectionToRsLine)) return false;

        Direction rDirectionToPqLine = Orientation(segment1.start, segment1.end, segment2.start);
        Direction sDirectionToPqLine = Orientation(segment1.start, segment1.end, segment2.end);

        if (rDirectionToPqLine.equals(sDirectionToPqLine)) return false;

        return true;
    }

    // Line is defined by ax + by + c = 0
    public float PointLineDistance(Point p, Line l){
        float numerator = l.a * p.x + l.b * p.y + l.c;
        float denominator = (float) Math.sqrt(l.a * l.a + l.b * l.b);

        return Math.abs(numerator / denominator);
    }

    // Line is defined by q, r
    public float PointLineDistance(Point p, Point line_q, Point line_r){
        Line l = TwoPointsToLine(line_q, line_r);

        Vector v = new Vector(line_q, line_r);
        Vector w = new Vector(line_q, p);

        return CrossProduct(v, w) / v.Magnitude();
    }

    public boolean DiskDiskIntersection(Disk disk1, Disk disk2) {
        return disk1.center.Distance(disk2.center) <= disk1.radius + disk2.radius;
    }

    public boolean TriangleTriangleIntersection(Triangle tri1, Triangle tri2) {
        // Check 9 pairs of edges to detect intersection on the boundaries
        for(Segment tri1Edge : tri1.edges){
            for (Segment tri2Edge : tri2.edges){
                if(SegmentSegmentIntersection(tri1Edge, tri2Edge)) return true;
            }
        }

        // Check if tri1 is inside the tri2 and vice versa
        // Is T1 inside T2
        if (PointInTriangle(tri1.a, tri2)) {
            return true;
        }

        // Is T1 inside T1
        if (PointInTriangle(tri2.a, tri1)) {
            return true;
        }

        return false;
    }

    public Direction Orientation(Point p, Point q, Point r) {
        Vector u = new Vector(p, q);
        Vector v = new Vector(p, r);

        return CrossProduct(u, v) > 0 ? Direction.LEFT : Direction.RIGHT;
    }

    public float CrossProduct(Vector v1, Vector v2) {
        return v1.x * v2.y - v1.y * v2.x;
    }

    public Line SegmentToLine(Segment segment) {
        float x1 = segment.start.x;
        float y1 = segment.start.y;
        float x2 = segment.end.x;
        float y2 = segment.end.y;

        float a = y1 - y2;
        float b = x2 - x1;
        float c = x1 * y2 - x2 * y1;

        return new Line(a, b, c);
    }

    public Line TwoPointsToLine(Point p1, Point p2) {
        float a = p1.y - p2.y;
        float b = p2.x - p1.x;
        float c = p1.x * p2.y - p2.x * p1.y;

        return new Line(a, b, c);
    }

    public boolean PointInTriangle(Point p, Triangle tri) {
        Direction o1 = Orientation(tri.a, tri.b, p);
        Direction o2 = Orientation(tri.b, tri.c, p);
        Direction o3 = Orientation(tri.c, tri.a, p);

        boolean allLeft = o1 == Direction.LEFT && o2 == Direction.LEFT && o3 == Direction.LEFT;
        boolean allRight = o1 == Direction.RIGHT && o2 == Direction.RIGHT && o3 == Direction.RIGHT;

        return allLeft || allRight;
    }

}
