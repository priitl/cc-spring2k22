package com.priitlaht.challenge.game.strategy.conditions;

import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.model.Enemy;
import com.priitlaht.challenge.game.model.Entity;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

@RequiredArgsConstructor(staticName = "of")
public class IsEnemyHeroWithinRangeOfTarget extends Routine {
    private final int range;

    @Override
    public void play(int heroId) {
        Entity target = GameState.instance().hero(heroId).target();
        Collection<Enemy> enemies = GameState.instance().enemies().values();
        if (enemies.stream().anyMatch(enemy -> enemy.distance(target.location()) <= range)) {
            succeed();
        } else {
            fail();
        }
    }
}
