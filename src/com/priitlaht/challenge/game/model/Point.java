package com.priitlaht.challenge.game.model;

import lombok.Value;

import java.util.List;

@Value(staticConstructor = "of")
public class Point {
    int x;
    int y;

    public int distance(Point other) {
        double ac = Math.abs(this.y - other.y);
        double cb = Math.abs(this.x - other.x);
        return (int) Math.hypot(ac, cb);
    }

    public Point add(Point other) {
        return new Point(this.x - other.x, this.y - other.y);
    }

    public Point subtractAbs(Point other) {
        return new Point(Math.abs(this.x - other.x), Math.abs(this.y - other.y));
    }

    public static Point centerOfMass(List<Point> points) {
        int centroidX = 0, centroidY = 0;
        for (Point point : points) {
            centroidX += point.x;
            centroidY += point.y;
        }
        return new Point(centroidX / points.size(), centroidY / points.size());
    }
}
