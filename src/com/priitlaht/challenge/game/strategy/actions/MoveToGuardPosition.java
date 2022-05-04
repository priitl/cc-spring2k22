package com.priitlaht.challenge.game.strategy.actions;

import com.priitlaht.challenge.game.GameConstants;
import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.model.Hero;
import com.priitlaht.challenge.game.model.Vector;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import lombok.NoArgsConstructor;

import java.util.*;

@NoArgsConstructor(staticName = "of")
public class MoveToGuardPosition extends Routine {
    Vector[] guardPositions = new Vector[]{Vector.of(4995, 3750), Vector.of(5581, 1439), Vector.of(1849, 5813)};

    @Override
    public void play(int heroId) {
        Hero currentHero = GameState.instance().hero(heroId);
        Collection<Hero> heroes = GameState.instance().heroes().values();
        List<Vector> closestToUnguarded = new ArrayList<>();
        for (Vector guardPosition : guardPositions) {
            double currentHeroDistance = currentHero.distance(guardPosition);
            boolean isClosest = heroes.stream().noneMatch(otherHero -> currentHero.id() != otherHero.id() && otherHero.distance(guardPosition) < currentHeroDistance);
            if (isClosest) {
                closestToUnguarded.add(guardPosition);
            }
        }
        Vector target = closestToUnguarded.stream()
                .min(Comparator.comparing(currentHero::distance))
                .orElseGet(() -> Arrays.stream(guardPositions)
                        .min(Comparator.comparing(currentHero::distance))
                        .orElse(guardPositions[0]));
        if (GameState.instance().myBase().location().x() > GameState.instance().myBase().location().x()) {
            currentHero.moveTo(Vector.of(GameConstants.FIELD_WIDTH - target.x(), GameConstants.FIELD_HEIGHT - target.y()));
        } else {
            currentHero.moveTo(target);
        }
        succeed();
    }
}
