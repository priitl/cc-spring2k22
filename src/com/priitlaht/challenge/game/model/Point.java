package com.priitlaht.challenge.game.model;

import lombok.Value;

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
}
