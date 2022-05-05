package com.priitlaht.challenge.game.strategy.conditions;

import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.model.Hero;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
public class IsHeroInRole extends Routine {
    private final Hero.Role role;

    @Override
    public void play(int heroId) {
        Hero hero = GameState.instance().hero(heroId);
        if (hero.role() == role) {
            succeed();
        } else {
            fail();
        }
    }
}
