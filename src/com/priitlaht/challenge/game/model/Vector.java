package com.priitlaht.challenge.game.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor(staticName = "of")
public class Vector {
    private final double x, y;

    public Vector(Vector a, Vector b) {
        this.x = b.x - a.x;
        this.y = b.y - a.y;
    }

    public Vector(double angle) {
        this.x = Math.cos(angle);
        this.y = Math.sin(angle);
    }

    public Vector rotate(double angle) {
        double nx = (x * Math.cos(angle)) - (y * Math.sin(angle));
        double ny = (x * Math.sin(angle)) + (y * Math.cos(angle));

        return new Vector(nx, ny);
    }

    public boolean equals(Vector v) {
        return v.x == x && v.y == y;
    }

    public Vector round() {
        return new Vector((int) Math.round(this.x), (int) Math.round(this.y));
    }

    public Vector truncate() {
        return new Vector((int) this.x, (int) this.y);
    }

    public double distance(Vector v) {
        return Math.sqrt((v.x - x) * (v.x - x) + (v.y - y) * (v.y - y));
    }

    public boolean inRange(Vector v, double range) {
        return (v.x - x) * (v.x - x) + (v.y - y) * (v.y - y) <= range * range;
    }

    public Vector add(Vector v) {
        return new Vector(x + v.x, y + v.y);
    }

    public Vector multiply(double factor) {
        return new Vector(x * factor, y * factor);
    }

    public Vector subtract(Vector v) {
        return new Vector(this.x - v.x, this.y - v.y);
    }

    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    public Vector stepTo(Vector destination, int speed) {
        Vector v = new Vector(this, destination);
        Vector target;
        if (v.lengthSquared() <= speed * speed) {
            target = v;
        } else {
            target = v.normalize().multiply(speed);
        }
        return this.add(target);
    }

    public double lengthSquared() {
        return x * x + y * y;
    }

    public Vector normalize() {
        double length = length();
        if (length == 0) {
            return new Vector(0, 0);
        }
        return new Vector(x / length, y / length);
    }

    public double dot(Vector v) {
        return x * v.x + y * v.y;
    }

    public double angle() {
        return Math.atan2(y, x);
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }

    public String toIntString() {
        return (int) x + " " + (int) y;
    }

    public Vector project(Vector force) {
        Vector normalize = this.normalize();
        return normalize.multiply(normalize.dot(force));
    }

    public final Vector cross(double s) {
        return new Vector(-s * y, s * x);
    }

    public Vector hsymmetric(double center) {
        return new Vector(2 * center - this.x, this.y);
    }

    public Vector vsymmetric(double center) {
        return new Vector(this.x, 2 * center - this.y);
    }

    public Vector vsymmetric() {
        return new Vector(this.x, -this.y);
    }

    public Vector hsymmetric() {
        return new Vector(-this.x, this.y);
    }

    public Vector symmetric() {
        return symmetric(new Vector(0, 0));
    }

    public Vector symmetric(Vector center) {
        return new Vector(center.x * 2 - this.x, center.y * 2 - this.y);
    }

    public boolean withinBounds(double minX, double minY, double maxX, double maxY) {
        return x >= minX && x <= maxX && y >= minY && y <= maxY;
    }

    public boolean isZero() {
        return x == 0 && y == 0;
    }

    public Vector symmetricTruncate(Vector origin) {
        return subtract(origin).truncate().add(origin);
    }

    public static Vector centerOfMass(List<Vector> vectors) {
        double centroidX = 0, centroidY = 0;
        for (Vector vector : vectors) {
            centroidX += vector.x;
            centroidY += vector.y;
        }
        return new Vector(centroidX / vectors.size(), centroidY / vectors.size());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Vector other = (Vector) obj;
        if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x)) {
            return false;
        }
        return Double.doubleToLongBits(y) == Double.doubleToLongBits(other.y);
    }
}
