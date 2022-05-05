package com.priitlaht.challenge.game.strategy.actions;

import com.priitlaht.challenge.game.GameConstants;
import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.model.Hero;
import com.priitlaht.challenge.game.model.Vector;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

@NoArgsConstructor(staticName = "of")
public class MoveToGuardPosition extends Routine {
    private static final Vector[] OFFENSIVE_POSITIONS = new Vector[]{
            Vector.of(3600, 6500),
            Vector.of(6800, 2000)
    };
    private static final Vector[] DEFENSIVE_POSITIONS = new Vector[]{
            Vector.of(3600, 5500),
            Vector.of(5800, 1000)
    };

    @Override
    public void play(int heroId) {
        Vector[] guardPositions = Arrays.copyOf(GameState.instance().round() > 20 ? DEFENSIVE_POSITIONS : OFFENSIVE_POSITIONS, 2);
        if (GameState.instance().myBase().location().x() > GameState.instance().opponentBase().location().x()) {
            for (int i = 0; i < (guardPositions).length; i++) {
                guardPositions[i] = Vector.of(GameConstants.FIELD_WIDTH, GameConstants.FIELD_HEIGHT).subtract(guardPositions[i]);
            }
        }
        Vector baseLocation = GameState.instance().myBase().location();
        Hero currentHero = GameState.instance().hero(heroId);
        Collection<Hero> heroes = GameState.instance().heroes().values();
        Vector target = Arrays.stream(guardPositions)
                .filter(position -> heroes.stream().noneMatch(otherHero -> currentHero.id() != otherHero.id()
                        && otherHero.distance(position) <= GameConstants.HERO_VISION_RADIUS))
                .min(Comparator.comparing(position -> position.distance(baseLocation)))
                .orElse(guardPositions[0]);
        currentHero.updateGuardPosition(target);
        currentHero.moveTo(target);
        succeed();
    }
}
