package com.priitlaht.challenge.game.model;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Getter
@SuperBuilder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PACKAGE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Entity {
    @EqualsAndHashCode.Include
    int id;
    Vector location;
    int shieldDuration;
    boolean isControlled;
    Integer assignedHeroId;
    Integer closestHeroId;

    public boolean hasHeroAssigned() {
        return this.assignedHeroId != null;
    }

    public boolean isAtLocation(Vector location) {
        return Objects.equals(this.location, location);
    }

    public boolean isShielded() {
        return shieldDuration > 0;
    }

    public double distance(Entity other) {
        return this.location.distance(other.location);
    }

    public double distance(Vector point) {
        return this.location.distance(point);
    }
}
