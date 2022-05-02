package com.priitlaht.challenge.game.strategy.conditions;

import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.model.Hero;
import com.priitlaht.challenge.game.model.Point;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
public class IsTargetNextLocationWithinRangeOfMyBase extends Routine {
    private final int range;

    @Override
    public void play(int heroId) {
        Point baseLocation = GameState.instance().myBase().location();
        Hero hero = GameState.instance().hero(heroId);
        if (hero.target() != null && baseLocation.distance(hero.target().nextLocation()) <= range) {
            succeed();
        } else {
            fail();
        }
    }
}
