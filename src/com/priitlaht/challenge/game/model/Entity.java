package com.priitlaht.challenge.game.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@FieldDefaults(level = AccessLevel.PACKAGE)
public abstract class Entity {
    int id;
    Point location;
    int shieldLife;
    boolean isControlled;

    public void update(Point location, int shieldLife, boolean isControlled) {
        this.location = location;
        this.shieldLife = shieldLife;
        this.isControlled = isControlled;
    }

    public boolean isShielded() {
        return shieldLife > 0;
    }

    public void shield() {
        this.shieldLife = 12;
    }

    public void control() {
        this.isControlled = true;
    }

    public int distance(Entity other) {
        return this.location.distance(other.location);
    }

    public int distance(Point point) {
        return this.location.distance(point);
    }
}
