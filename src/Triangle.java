import java.util.ArrayList;

public class Triangle {
    public Point a, b, c;
    public Segment ab, bc, ca;
    public ArrayList<Segment> edges = new ArrayList<Segment>();

    public Triangle(Point a, Point b, Point c) {
        this.a = a;
        this.b = b;
        this.c = c;
        ab = new Segment(a, b);
        bc = new Segment(b, c);
        ca = new Segment(c, a);
    }
}
