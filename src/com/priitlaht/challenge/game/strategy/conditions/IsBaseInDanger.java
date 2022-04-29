package com.priitlaht.challenge.game.strategy.conditions;

import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import lombok.NoArgsConstructor;

@NoArgsConstructor(staticName = "of")
public class IsBaseInDanger extends Routine {
    @Override
    public void play(int heroId) {
        if (GameState.instance().myBase().isInDanger()) {
            succeed();
        }
        fail();
    }
}
