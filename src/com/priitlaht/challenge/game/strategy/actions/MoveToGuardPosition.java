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
import java.util.Objects;

@NoArgsConstructor(staticName = "of")
public class MoveToGuardPosition extends Routine {
    Vector[] defaultPositions = new Vector[]{Vector.of(8800, 2200), Vector.of(3600, 6500), Vector.of(6800, 2000)};

    @Override
    public void play(int heroId) {
        Vector[] guardPositions = new Vector[3];
        if (GameState.instance().myBase().location().x() > GameState.instance().opponentBase().location().x()) {
            for (int i = 0; i < defaultPositions.length; i++) {
                guardPositions[i] = Vector.of(GameConstants.FIELD_WIDTH, GameConstants.FIELD_HEIGHT).subtract(defaultPositions[i]);
            }
        } else {
            guardPositions = defaultPositions;
        }
        Hero currentHero = GameState.instance().hero(heroId);
        Collection<Hero> heroes = GameState.instance().heroes().values();
        Vector target = Arrays.stream(guardPositions)
                .filter(position -> heroes.stream().noneMatch(otherHero -> currentHero.id() != otherHero.id()
                        && Objects.equals(position, otherHero.guardPosition())))
                .min(Comparator.comparing(currentHero::distance))
                .orElse(guardPositions[0]);
        currentHero.moveTo(target);
        currentHero.updateGuardPosition(target);
        succeed();
    }
}
