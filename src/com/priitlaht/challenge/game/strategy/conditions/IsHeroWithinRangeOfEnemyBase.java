package com.priitlaht.challenge.game.strategy.conditions;

import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.model.Hero;
import com.priitlaht.challenge.game.model.Vector;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
public class IsHeroWithinRangeOfEnemyBase extends Routine {
    private final int range;

    @Override
    public void play(int heroId) {
        Vector baseLocation = GameState.instance().opponentBase().location();
        Hero hero = GameState.instance().hero(heroId);
        if (hero.distance(baseLocation) <= range) {
            succeed();
        } else {
            fail();
        }
    }
}
