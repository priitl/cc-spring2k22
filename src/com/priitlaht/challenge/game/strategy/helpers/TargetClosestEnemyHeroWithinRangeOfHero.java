package com.priitlaht.challenge.game.strategy.helpers;

import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.model.Enemy;
import com.priitlaht.challenge.game.model.Hero;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

@RequiredArgsConstructor(staticName = "of")
public class TargetClosestEnemyHeroWithinRangeOfHero extends Routine {
    private final int range;

    @Override
    public void play(int heroId) {
        Collection<Enemy> enemies = GameState.instance().enemies().values();
        Hero hero = GameState.instance().hero(heroId);
        Optional<Enemy> target = enemies.stream()
                .filter(enemy -> enemy.distance(hero) <= range)
                .min(Comparator.comparing(enemy -> enemy.distance(hero)));
        if (target.isPresent()) {
            hero.updateTarget(target.get());
            succeed();
        } else {
            fail();
        }
    }
}