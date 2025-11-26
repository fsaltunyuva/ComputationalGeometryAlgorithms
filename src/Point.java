public class Point {
    public float x, y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float Distance(Point otherPoint) {
        return (float) Math.sqrt(Math.pow(x - otherPoint.x, 2) + Math.pow(y - otherPoint.y, 2));
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
