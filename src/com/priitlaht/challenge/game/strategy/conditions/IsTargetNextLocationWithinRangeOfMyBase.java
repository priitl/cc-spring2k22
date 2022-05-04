package com.priitlaht.challenge.game.strategy.conditions;

import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.model.Entity;
import com.priitlaht.challenge.game.model.Hero;
import com.priitlaht.challenge.game.model.Monster;
import com.priitlaht.challenge.game.model.Vector;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
public class IsTargetNextLocationWithinRangeOfMyBase extends Routine {
    private final int range;

    @Override
    public void play(int heroId) {
        Vector baseLocation = GameState.instance().myBase().location();
        Hero hero = GameState.instance().hero(heroId);
        Entity target = hero.target();
        Vector nextLocation = (target instanceof Monster) ? ((Monster) target).nextLocation() : target.location();
        if (baseLocation.distance(nextLocation) <= range) {
            succeed();
        } else {
            fail();
        }
    }
}
