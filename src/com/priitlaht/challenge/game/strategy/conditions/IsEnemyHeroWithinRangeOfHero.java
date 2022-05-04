package com.priitlaht.challenge.game.strategy.conditions;

import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.model.Enemy;
import com.priitlaht.challenge.game.model.Hero;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

@RequiredArgsConstructor(staticName = "of")
public class IsEnemyHeroWithinRangeOfHero extends Routine {
    private final int range;

    @Override
    public void play(int heroId) {
        Hero hero = GameState.instance().hero(heroId);
        Collection<Enemy> enemies = GameState.instance().enemies().values();
        if (enemies.stream().anyMatch(enemy -> enemy.distance(hero.location()) <= range)) {
            succeed();
        } else {
            fail();
        }
    }
}
