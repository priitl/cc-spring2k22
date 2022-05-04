package com.priitlaht.challenge.game.model;

import com.priitlaht.challenge.game.GameConstants;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

@Getter
@SuperBuilder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Monster extends Entity {
    int health;
    Vector speed;
    Target target;
    Threat threat;

    public Vector nextLocation() {
        if (speed == null) {
            return this.location();
        }
        return this.location().add(speed);
    }

    public Monster mirror() {
        return Monster.builder()
                .id(this.id % 2 == 1 ? this.id - 1 : this.id + 1)
                .location(Vector.of(GameConstants.FIELD_WIDTH - this.location.x(), GameConstants.FIELD_HEIGHT - this.location().y()))
                .speed(Vector.of(-this.speed.x(), -this.speed().y()))
                .threat(Threat.MY_BASE == this.threat ? Threat.OPPONENT_BASE : Threat.OPPONENT_BASE == this.threat ? Threat.MY_BASE : Threat.NONE)
                .target(this.target)
                .build();
    }

    public boolean isTargetingBase(Base base) {
        return this.target() == Monster.Target.BASE
                && this.threat() == (base.isMine() ? Monster.Threat.MY_BASE : Monster.Threat.OPPONENT_BASE);
    }

    public boolean isThreateningBase(Base base) {
        return this.threat() == (base.isMine() ? Monster.Threat.MY_BASE : Monster.Threat.OPPONENT_BASE);
    }

    public void updateClosestHeroes(Collection<Hero> heroes) {
        closestHeroId = heroes.stream().min(Comparator.comparing(this::distance)).map(Hero::id).orElse(null);
    }

    public Monster moveToNextLocation() {
        this.location = this.nextLocation();
        return this;
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