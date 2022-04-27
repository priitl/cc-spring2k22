package com.priitlaht.challenge.game.model;

import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.command.Command;
import com.priitlaht.challenge.game.strategy.Strategy;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Hero extends Entity {
    public static final int DAMAGE_RADIUS = 300;
    public static final int VISION_RADIUS = 2200;
    Type type;
    Strategy strategy;

    public Point origin() {
        return type.origin;
    }

    public Command resolveCommand(GameState state) {
        return strategy.resolve(this, state);
    }

    public boolean isBestOffensiveFor(Monster monster) {
        return !monster.hasHeroAssigned() || monster.assignedHeroId() == this.id || monster.closestOffensiveHeroId() == this.id;
    }

    public boolean isBestDefensiveFor(Monster monster) {
        return !monster.hasHeroAssigned() || monster.assignedHeroId() == this.id || monster.closestDefensiveHeroId() == this.id;
    }

    @RequiredArgsConstructor
    public enum Type {
        HARASSER(Point.of(13800, 5200)),
        DEFENDER(Point.of(7200, 2200)),
        JUNGLER(Point.of(4000, 7800));
        final Point origin;
    }
}
