package com.priitlaht.challenge.game.strategy.actions;

import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.model.Hero;
import com.priitlaht.challenge.game.model.Monster;
import com.priitlaht.challenge.game.model.Vector;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

@RequiredArgsConstructor(staticName = "of")
public class RedirectTargetAwayFromClosestMonster extends Routine {
    private final int targetSpeed;

    @Override
    public void play(int heroId) {
        Collection<Monster> visibleMonsters = GameState.instance().monsters().values();
        Hero hero = GameState.instance().hero(heroId);
        Vector targetLocation = hero.target().location();
        Optional<Monster> closestMonster = visibleMonsters.stream()
                .min(Comparator.comparing(monster -> monster.distance(targetLocation)));
        if (closestMonster.isPresent()) {
            Vector redirectVelocity = targetLocation.subtract(closestMonster.get().nextLocation()).normalize().multiply(targetSpeed);
            hero.control(hero.target(), targetLocation.add(redirectVelocity));
            succeed();
        } else {
            fail();
        }

    }
}