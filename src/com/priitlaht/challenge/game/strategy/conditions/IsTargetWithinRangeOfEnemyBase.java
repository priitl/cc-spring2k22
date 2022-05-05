package com.priitlaht.challenge.game.strategy.conditions;

import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.model.Base;
import com.priitlaht.challenge.game.model.Entity;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
public class IsTargetWithinRangeOfEnemyBase extends Routine {
    private final int range;

    @Override
    public void play(int heroId) {
        Entity target = GameState.instance().hero(heroId).target();
        Base base = GameState.instance().opponentBase();
        if (target != null && target.distance(base.location()) <= range) {
            succeed();
        } else {
            fail();
        }
    }
}