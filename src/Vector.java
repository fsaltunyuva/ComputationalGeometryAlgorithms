public class Vector {
    public Point start;
    public Point end;

    public float x, y;

    public Vector(Point start, Point end) {
        this.start = start;
        this.end = end;
        this.x = end.x - start.x;
        this.y = end.y - start.y;
    }

    public float Magnitude() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public float SquaredLength(){
        return x * x + y * y;
    }
}
