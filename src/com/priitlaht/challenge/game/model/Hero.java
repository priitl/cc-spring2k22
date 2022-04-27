package com.priitlaht.challenge.game.model;

import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.command.Command;
import com.priitlaht.challenge.game.strategy.HarasserStrategy;
import com.priitlaht.challenge.game.strategy.Strategy;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Getter
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Hero extends Entity {
    public static final int DAMAGE_RADIUS = 800;
    public static final int VISION_RADIUS = 2200;
    public static final int DAMAGE = 2;
    Type type;
    Strategy strategy;
    Point origin;

    public Command resolveCommand(GameState state) {
        return strategy.resolve(this, state);
    }

    public boolean isAssignedOrClosestTo(Monster monster) {
        return Objects.equals(monster.assignedHeroId(), this.id) || (!monster.hasHeroAssigned() && Objects.equals(monster.closestHeroId(), this.id));
    }

    public void updateStrategy(GameState state) {
        if (state.round() == GameState.Phase.MID.startingRound() && type == Type.HARASSER) {
            this.strategy = HarasserStrategy.of();
            this.origin = Point.of(state.myBase().isBlueBase() ? 13630 : 4000, 4500);
        }
    }

    @RequiredArgsConstructor
    public enum Type {
        HARASSER, DEFENDER, JUNGLER;
    }
}
