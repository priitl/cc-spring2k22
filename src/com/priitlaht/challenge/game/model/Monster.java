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
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Monster extends Entity {
    int health;
    Vector velocity;
    Target target;
    Threat threat;

    public Monster mirror() {
        return Monster.builder()
                .id(this.id % 2 == 1 ? this.id - 1 : this.id + 1)
                .position(Vector.of(GameConstants.FIELD_WIDTH - this.position.x(), GameConstants.FIELD_HEIGHT - this.position().y()))
                .velocity(Vector.of(-this.velocity.x(), -this.velocity().y()))
                .threat(Threat.MY_BASE == this.threat ? Threat.OPPONENT_BASE : Threat.OPPONENT_BASE == this.threat ? Threat.MY_BASE : Threat.NONE)
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