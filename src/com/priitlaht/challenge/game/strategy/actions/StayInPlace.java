package com.priitlaht.challenge.game.strategy.actions;

import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import lombok.NoArgsConstructor;

@NoArgsConstructor(staticName = "of")
public class StayInPlace extends Routine {
    @Override
    public void play(int heroId) {
        GameState.instance().hero(heroId).stayInPlace();
    }
}
