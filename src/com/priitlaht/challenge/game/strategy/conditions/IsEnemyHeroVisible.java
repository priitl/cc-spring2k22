package com.priitlaht.challenge.game.strategy.conditions;

import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.model.Enemy;
import com.priitlaht.challenge.game.model.Hero;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(staticName = "of")
public class IsEnemyHeroVisible extends Routine {
    @Override
    public void play(int heroId) {
        Hero hero = GameState.instance().hero(heroId);
        List<Enemy> visibleEnemies = GameState.instance().visibleEnemies();
        // TODO: create visible enemies variable for hero
        if (visibleEnemies.stream().anyMatch(enemy -> enemy.distance(hero) <= Hero.VISION_RADIUS)) {
            succeed();
        }
        fail();
    }
}
