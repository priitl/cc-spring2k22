package com.priitlaht.challenge.game.strategy.helpers;

import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.model.Base;
import com.priitlaht.challenge.game.model.Enemy;
import com.priitlaht.challenge.game.model.Hero;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

@RequiredArgsConstructor(staticName = "of")
public class TargetEnemyHeroWithinRangeOfMyBase extends Routine {
    private final int range;

    @Override
    public void play(int heroId) {
        Base myBase = GameState.instance().myBase();
        Collection<Enemy> enemies = GameState.instance().enemies().values();
        Hero hero = GameState.instance().hero(heroId);
        Optional<Enemy> target = enemies.stream()
                .filter(enemy -> enemy.distance(myBase.location()) <= range)
                .min(Comparator.comparing(enemy -> enemy.distance(myBase.location())));
        if (target.isPresent()) {
            hero.updateTarget(target.get());
            succeed();
        } else {
            fail();
        }
    }
}
