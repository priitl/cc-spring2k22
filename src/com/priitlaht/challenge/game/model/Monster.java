package com.priitlaht.challenge.game.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Arrays;

@Getter
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Monster extends Entity {
    public static final int DISTANCE_PER_TURN = 400;
    public static final int BASE_TARGET_RADIUS = 5000;

    int health;
    Point velocity;
    Target target;
    Threat threat;
    Integer assignedHeroId;

    public Point nextLocation() {
        return this.location().add(velocity);
    }

    public int nextDistance(Entity other) {
        return this.nextLocation().distance(other.location);
    }

    public int nextDistance(Point point) {
        return this.nextLocation().distance(point);
    }

    public boolean hasHeroAssigned() {
        return this.assignedHeroId != null;
    }

    public boolean hasNoHeroAssignedOrAssignedTo(int heroId) {
        return !hasHeroAssigned() || assignedHeroId == heroId;
    }

    public void assignHero(int heroId) {
        this.assignedHeroId = heroId;
    }

    public boolean isTargetingBase(Base base) {
        return this.target() == Monster.Target.BASE
                && this.threat() == (base.isMine() ? Monster.Threat.MY_BASE : Monster.Threat.OPPONENT_BASE);
    }

    public boolean isThreateningBase(Base base) {
        return this.threat() == (base.isMine() ? Monster.Threat.MY_BASE : Monster.Threat.OPPONENT_BASE);
    }

    @RequiredArgsConstructor
    public enum Target {
        NO_TARGET(0), BASE(1);
        final int value;

        public static Target getByValue(int value) {
            return Arrays.stream(Target.values()).filter(type -> type.value == value).findFirst().orElse(null);
        }
    }

    @RequiredArgsConstructor
    public enum Threat {
        NONE(0), MY_BASE(1), OPPONENT_BASE(2);
        final int value;

        public static Threat getByValue(int value) {
            return Arrays.stream(Threat.values()).filter(type -> type.value == value).findFirst().orElse(null);
        }
    }
}