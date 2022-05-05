package com.priitlaht.challenge.game.strategy.actions;

import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.model.Entity;
import com.priitlaht.challenge.game.model.Hero;
import com.priitlaht.challenge.game.model.Monster;
import com.priitlaht.challenge.game.model.Vector;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
public class RedirectTargetToEnemyBase extends Routine {
    private final int targetSpeed;

    @Override
    public void play(int heroId) {
        Hero hero = GameState.instance().hero(heroId);
        Vector baseLocation = GameState.instance().opponentBase().location();
        Entity target = hero.target();
        Vector nextLocation = (target instanceof Monster) ? ((Monster) target).nextLocation() : target.location();
        Vector redirectVelocity = nextLocation.subtract(baseLocation).normalize().multiply(targetSpeed);
        hero.control(target, nextLocation.subtract(redirectVelocity));
        succeed();
    }
}
