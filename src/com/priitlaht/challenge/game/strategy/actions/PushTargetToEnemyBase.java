package com.priitlaht.challenge.game.strategy.actions;

import com.priitlaht.challenge.game.GameConstants;
import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.model.Hero;
import com.priitlaht.challenge.game.model.Vector;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import lombok.NoArgsConstructor;

@NoArgsConstructor(staticName = "of")
public class PushTargetToEnemyBase extends Routine {
    @Override
    public void play(int heroId) {
        Hero hero = GameState.instance().hero(heroId);
        Vector heroLocation = hero.location();
        Vector baseLocation = GameState.instance().opponentBase().location();
        Vector normalize = heroLocation.subtract(baseLocation).normalize();
        Vector windTowards = heroLocation.subtract(normalize.multiply(GameConstants.SPELL_WIND_RADIUS));

        hero.wind(hero.target(), windTowards);
        succeed();
    }
}