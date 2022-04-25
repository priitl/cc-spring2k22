import lombok.Value;

@Value(staticConstructor = "of")
class Point {
    int x;
    int y;

    int distance(Point other) {
        double ac = Math.abs(this.y - other.y);
        double cb = Math.abs(this.x - other.x);
        return (int) Math.hypot(ac, cb);
    }

    Point add(Point other) {
        return new Point(this.x - other.x, this.y - other.y);
    }

    Point subtractAbs(Point other) {
        return new Point(Math.abs(this.x - other.x), Math.abs(this.y - other.y));
    }
}
