package com.priitlaht.challenge.game.strategy.conditions;

import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.model.Base;
import com.priitlaht.challenge.game.model.Entity;
import com.priitlaht.challenge.game.model.Monster;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import lombok.NoArgsConstructor;

@NoArgsConstructor(staticName = "of")
public class IsTargetThreateningMyBase extends Routine {
    @Override
    public void play(int heroId) {
        Entity target = GameState.instance().hero(heroId).target();
        Base base = GameState.instance().myBase();
        if (target != null && ((Monster) target).isThreateningBase(base)) {
            succeed();
        } else {
            fail();
        }
    }
}
