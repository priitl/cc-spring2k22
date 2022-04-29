package com.priitlaht.challenge.game.strategy.actions;

import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.model.Hero;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import lombok.NoArgsConstructor;

@NoArgsConstructor(staticName = "of")
public class MoveToOrigin extends Routine {
    @Override
    public void play(int heroId) {
        Hero hero = GameState.instance().hero(heroId);
        if (!isRunning()) {
            return;
        }
        if (hero.isAtOrigin()) {
            succeed();
        } else {
            hero.moveToOrigin();
        }
    }
}
