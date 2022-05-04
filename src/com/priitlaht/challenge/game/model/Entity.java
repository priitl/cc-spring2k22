package com.priitlaht.challenge.game.model;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Getter
@SuperBuilder
@FieldDefaults(level = AccessLevel.PACKAGE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Entity {
    @EqualsAndHashCode.Include
    int id;
    Vector position;
    Vector speed;
    int shieldDuration;
    boolean isControlled;
    Integer assignedHeroId;
    Integer closestHeroId;

    public void update(Vector location, int shieldDuration, boolean isControlled) {
        this.position = location;
        this.shieldDuration = shieldDuration;
        this.isControlled = isControlled;
    }

    public boolean hasHeroAssigned() {
        return this.assignedHeroId != null;
    }

    public Vector nextLocation() {
        if (speed == null) {
            return this.position();
        }
        return this.position().add(speed);
    }

    public boolean isAtLocation(Vector location) {
        return Objects.equals(this.position, location);
    }

    public boolean isShielded() {
        return shieldDuration > 0;
    }

    public double distance(Entity other) {
        return this.position.distance(other.position);
    }

    public double distance(Vector point) {
        return this.position.distance(point);
    }
}
