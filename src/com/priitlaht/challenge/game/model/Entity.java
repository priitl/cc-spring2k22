package com.priitlaht.challenge.game.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Getter
@SuperBuilder
@FieldDefaults(level = AccessLevel.PACKAGE)
public class Entity {
    int id;
    Point location;
    Point velocity;
    int shieldLife;
    boolean isControlled;
    Integer assignedHeroId;
    Integer closestHeroId;

    public void update(Point location, int shieldLife, boolean isControlled) {
        this.location = location;
        this.shieldLife = shieldLife;
        this.isControlled = isControlled;
    }

    public boolean hasHeroAssigned() {
        return this.assignedHeroId != null;
    }

    public Point nextLocation() {
        if (velocity == null) {
            return this.location();
        }
        return this.location().add(velocity);
    }

    public boolean isAtLocation(Point location) {
        return Objects.equals(this.location, location);
    }

    public boolean isShielded() {
        return shieldLife > 0;
    }

    public double distance(Entity other) {
        return this.location.distance(other.location);
    }

    public double distance(Point point) {
        return this.location.distance(point);
    }
}
