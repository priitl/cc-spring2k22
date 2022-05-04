package com.priitlaht.challenge.game.strategy.actions;

import com.priitlaht.challenge.game.GameConstants;
import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.model.Entity;
import com.priitlaht.challenge.game.model.Hero;
import com.priitlaht.challenge.game.model.Monster;
import com.priitlaht.challenge.game.model.Vector;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(staticName = "of")
public class AttackArea extends Routine {
    @Override
    public void play(int heroId) {
        List<Monster> visibleMonsters = GameState.instance().visibleMonsters();
        Hero hero = GameState.instance().hero(heroId);
        List<Vector> nextLocations = visibleMonsters.stream()
                .map(Entity::nextLocation)
                .filter(nextLocation -> hero.distance(nextLocation) <= GameConstants.HERO_DAMAGE_RADIUS)
                .collect(Collectors.toList());
        if (nextLocations.isEmpty()) {
            fail();
        } else {
            hero.moveTo(Vector.centerOfMass(nextLocations));
            succeed();
        }
    }
}
