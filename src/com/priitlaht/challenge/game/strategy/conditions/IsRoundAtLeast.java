package com.priitlaht.challenge.game.strategy.conditions;

import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
public class IsRoundAtLeast extends Routine {
    private final int min;

    @Override
    public void play(int heroId) {
        if (GameState.instance().round() >= min) {
            succeed();
        } else {
            fail();
        }
    }
}
