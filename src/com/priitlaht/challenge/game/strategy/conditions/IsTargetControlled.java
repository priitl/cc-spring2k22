package com.priitlaht.challenge.game.strategy.conditions;

import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.model.Entity;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import lombok.NoArgsConstructor;

@NoArgsConstructor(staticName = "of")
public class IsTargetControlled extends Routine {
    @Override
    public void play(int heroId) {
        Entity target = GameState.instance().hero(heroId).target();
        if (target != null && target.isControlled()) {
            succeed();
        } else {
            fail();
        }
    }
}
