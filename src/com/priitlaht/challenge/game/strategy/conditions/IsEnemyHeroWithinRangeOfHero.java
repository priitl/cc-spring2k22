package com.priitlaht.challenge.game.strategy.conditions;

import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.model.Hero;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
public class IsEnemyHeroWithinRangeOfHero extends Routine {
    private final int range;

    @Override
    public void play(int heroId) {
        Hero hero = GameState.instance().hero(heroId);
        if (GameState.instance().visibleEnemies().stream().anyMatch(enemy -> enemy.distance(hero.location()) <= range)) {
            succeed();
        } else {
            fail();
        }
    }
}
