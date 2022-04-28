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
    Integer assignedHeroId;
    Integer closestHeroId;
    Integer closestDefensiveHeroId;

    public void update(Point location, int shieldLife, boolean isControlled) {
        this.location = location;
        this.shieldLife = shieldLife;
        this.isControlled = isControlled;
    }

    public boolean hasHeroAssigned() {
        return this.assignedHeroId != null;
    }

    public boolean isUnshielded() {
        return shieldLife <= 0;
    }

    public int distance(Entity other) {
        return this.location.distance(other.location);
    }

    public int distance(Point point) {
        return this.location.distance(point);
    }
}
