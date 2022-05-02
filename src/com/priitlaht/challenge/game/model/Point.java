package com.priitlaht.challenge.game.model;

import lombok.Value;

import java.util.List;

@Value(staticConstructor = "of")
public class Point {
    double x;
    double y;

    public double distance(Point other) {
        double ac = Math.abs(this.y - other.y);
        double cb = Math.abs(this.x - other.x);
        return Math.hypot(ac, cb);
    }

    public double length() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public Point add(Point other) {
        return new Point(this.x + other.x, this.y + other.y);
    }

    public Point subtractAbs(Point other) {
        return new Point(Math.abs(this.x - other.x), Math.abs(this.y - other.y));
    }

    public Point subtract(Point other) {
        return new Point(this.x - other.x, this.y - other.y);
    }

    public Point multiply(int ratio) {
        return new Point(this.x * ratio, this.y * ratio);
    }

    public Point normalize() {
        double length = this.length();
        System.err.println(length);
        return Point.of(x / length, y / length);
    }

    public Point clockwise() {
        return Point.of(y * -1, x);
    }

    public Point counterClockwise() {
        return Point.of(y, x * -1);
    }

    public static double dot(Point first, Point second) {
        Point normalizedFirst = first.normalize();
        Point normalizedSecond = second.normalize();
        return normalizedFirst.x * normalizedSecond.x + normalizedFirst.y * normalizedSecond.y;
    }

    public static Point centerOfMass(List<Point> points) {
        double centroidX = 0, centroidY = 0;
        for (Point point : points) {
            centroidX += point.x;
            centroidY += point.y;
        }
        return new Point(centroidX / points.size(), centroidY / points.size());
    }
}
