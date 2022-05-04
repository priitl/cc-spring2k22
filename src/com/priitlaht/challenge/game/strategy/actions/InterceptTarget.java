package com.priitlaht.challenge.game.strategy.actions;

import com.priitlaht.challenge.game.GameConstants;
import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.model.Entity;
import com.priitlaht.challenge.game.model.Hero;
import com.priitlaht.challenge.game.model.Monster;
import com.priitlaht.challenge.game.model.Vector;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import lombok.NoArgsConstructor;

@NoArgsConstructor(staticName = "of")
public class InterceptTarget extends Routine {
    @Override
    public void play(int heroId) {
        Hero hero = GameState.instance().hero(heroId);
        Entity target = GameState.instance().hero(heroId).target();
        if (target instanceof Monster) {
            Monster monster = (Monster) target;
            Vector interceptLocation = monster.nextLocation();
            for (int turn = 1; turn < 50; turn++) {
                if (hero.location().distance(interceptLocation) < GameConstants.HERO_DISTANCE_PER_TURN * turn) {
                    break;
                }
                interceptLocation.add(monster.speed());
            }
            hero.intercept(monster, interceptLocation);
            succeed();
        } else {
            fail();
        }
    }
}
