package com.priitlaht.challenge.game.strategy.actions;

import com.priitlaht.challenge.game.GameConstants;
import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.model.Hero;
import com.priitlaht.challenge.game.model.Monster;
import com.priitlaht.challenge.game.model.Vector;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor(staticName = "of")
public class AttackArea extends Routine {
    @Override
    public void play(int heroId) {
        Collection<Monster> visibleMonsters = GameState.instance().monsters().values();
        Hero hero = GameState.instance().hero(heroId);
        List<Vector> nextLocations = visibleMonsters.stream()
                .map(Monster::nextLocation)
                .filter(nextLocation -> hero.distance(nextLocation) <= GameConstants.HERO_DAMAGE_RADIUS + GameConstants.HERO_DISTANCE_PER_TURN)
                .collect(Collectors.toList());
        Optional<Vector> centerOfMassInDamageRange = centerOfMassInDamageRange(hero, nextLocations);
        if (centerOfMassInDamageRange.isPresent()) {
            hero.moveTo(centerOfMassInDamageRange.get());
            succeed();
        } else {
            fail();
        }
    }

    private Optional<Vector> centerOfMassInDamageRange(Hero hero, List<Vector> nextLocations) {
        if (nextLocations.isEmpty()) {
            return Optional.empty();
        }
        Vector centerOfMass = Vector.centerOfMass(nextLocations);
        if (centerOfMass.distance(hero.location()) <= GameConstants.HERO_DAMAGE_RADIUS) {
            return Optional.of(centerOfMass);
        } else {
            nextLocations.stream().max(Comparator.comparing(location -> location.distance(centerOfMass))).ifPresent(nextLocations::remove);
            return centerOfMassInDamageRange(hero, nextLocations);
        }
    }
}
