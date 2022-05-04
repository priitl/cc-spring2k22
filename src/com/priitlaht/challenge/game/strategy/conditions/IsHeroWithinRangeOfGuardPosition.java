package com.priitlaht.challenge.game.strategy.conditions;

import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.model.Hero;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
public class IsHeroWithinRangeOfGuardPosition extends Routine {
    private final int range;

    @Override
    public void play(int heroId) {
        Hero hero = GameState.instance().hero(heroId);
        if (hero.guardPosition() == null || hero.distance(hero.guardPosition()) <= range) {
            succeed();
        } else {
            fail();
        }
    }
}
