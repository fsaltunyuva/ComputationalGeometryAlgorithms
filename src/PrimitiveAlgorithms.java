public class PrimitiveAlgorithms {

    // Line is defined by a, b, c in the equation ax + by + c = 0
    public Point LineLineIntersection(Line line1, Line line2) {
        // Cramer's Rule
        float x_numerator = line2.b * line1.c - line1.b * line2.c;
        float x_denominator = line2.a * line1.b - line1.a * line2.b;

        float y_numerator = line2.a * line1.c - line1.a * line2.c;
        float y_denominator = line2.b * line1.a - line1.b * line2.a;

        return new Point(x_numerator / x_denominator, y_numerator / y_denominator);
    }

    public boolean SegmentSegmentIntersection(Segment segment1, Segment segment2) {
        // Segment 1: pq
        // Segment 2: rs

        // Check orientations for p and q with respect to line rs
        Direction pDirectionToRsLine = Orientation(segment2.start, segment2.end, segment1.start);
        Direction qDirectionToRsLine = Orientation(segment2.start, segment2.end, segment1.end);

        if (pDirectionToRsLine.equals(qDirectionToRsLine)) return false; // Both points are on the same side of line rs

        // Check orientations for r and s with respect to line pq
        Direction rDirectionToPqLine = Orientation(segment1.start, segment1.end, segment2.start);
        Direction sDirectionToPqLine = Orientation(segment1.start, segment1.end, segment2.end);

        if (rDirectionToPqLine.equals(sDirectionToPqLine)) return false; // Both points are on the same side of line pq

        return true; // Segments intersect
    }

    // Line is defined by ax + by + c = 0
    public float PointLineDistance(Point p, Line l){
        // Distance = |ax0 + by0 + c| / sqrt(a^2 + b^2)
        float numerator = l.a * p.x + l.b * p.y + l.c;
        float denominator = (float) Math.sqrt(l.a * l.a + l.b * l.b);

        return Math.abs(numerator / denominator);
    }

    // Line is defined by q, r
    public float PointLineDistance(Point p, Point line_q, Point line_r){
        Vector v = new Vector(line_q, line_r); // Direction vector of the line
        Vector w = new Vector(line_q, p); // Vector from line point to point p

        return CrossProduct(v, w) / v.Magnitude(); // Area of parallelogram / base = height (distance)
    }

    // Disk defined by center point and radius
    public boolean DiskDiskIntersection(Disk disk1, Disk disk2) {
        // Check if the distance between centers is less than or equal to the sum of rs
        return disk1.center.Distance(disk2.center) <= disk1.radius + disk2.radius;
    }

    // Triangle defined by 3 points a, b, c
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

    // Check if point p is inside triangle tri
    public boolean PointInTriangle(Point p, Triangle tri) {
        // Get orientations of p with respect to each edge of the triangle
        Direction o1 = Orientation(tri.a, tri.b, p);
        Direction o2 = Orientation(tri.b, tri.c, p);
        Direction o3 = Orientation(tri.c, tri.a, p);

        // If all orientations are the same (all left or all right), point is inside the triangle
        boolean allLeft = o1 == Direction.Left && o2 == Direction.Left && o3 == Direction.Left;
        boolean allRight = o1 == Direction.Right && o2 == Direction.Right && o3 == Direction.Right;

        return allLeft || allRight;
    }

    // Given three points p, q, r, is r to the left or right of the directed line pq?
    public Direction Orientation(Point p, Point q, Point r) {
        Vector u = new Vector(p, q);
        Vector v = new Vector(p, r);

        return CrossProduct(u, v) > 0 ? Direction.Left : Direction.Right;
    }

    // Cross product of two vectors in 2D (returns scalar value)
    public float CrossProduct(Vector v1, Vector v2) {
        return v1.x * v2.y - v1.y * v2.x;
    }

    // Convert segment to line
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

    // Convert 2 points to line
    public Line TwoPointsToLine(Point p1, Point p2) {
        float a = p1.y - p2.y;
        float b = p2.x - p1.x;
        float c = p1.x * p2.y - p2.x * p1.y;

        return new Line(a, b, c);
    }
}
