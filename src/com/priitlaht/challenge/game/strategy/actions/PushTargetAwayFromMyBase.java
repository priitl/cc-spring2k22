package com.priitlaht.challenge.game.strategy.actions;

import com.priitlaht.challenge.game.GameConstants;
import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.model.Hero;
import com.priitlaht.challenge.game.model.Point;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import lombok.NoArgsConstructor;

@NoArgsConstructor(staticName = "of")
public class PushTargetAwayFromMyBase extends Routine {
    @Override
    public void play(int heroId) {
        Hero hero = GameState.instance().hero(heroId);
        Point heroLocation = hero.location();
        Point baseLocation = GameState.instance().myBase().location();
        Point normalize = heroLocation.subtract(baseLocation).normalize();
        System.err.println(normalize);
        Point windTowards = heroLocation.add(normalize.multiply(GameConstants.WIND_RADIUS));

        hero.wind(hero.target(), windTowards);
        succeed();
    }
}
