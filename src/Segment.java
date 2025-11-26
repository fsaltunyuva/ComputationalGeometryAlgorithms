public class Segment {

    public Point start, end; // Start and end point

    public Segment(Point start, Point end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return start + " " + end;
    }
}
