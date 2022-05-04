package com.priitlaht.challenge.game.strategy.actions;

import com.priitlaht.challenge.game.GameConstants;
import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.model.Entity;
import com.priitlaht.challenge.game.model.Hero;
import com.priitlaht.challenge.game.model.Vector;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import lombok.NoArgsConstructor;


@NoArgsConstructor(staticName = "of")
public class RedirectTargetAwayFromByBase extends Routine {
    @Override
    public void play(int heroId) {
        Hero hero = GameState.instance().hero(heroId);
        Vector baseLocation = GameState.instance().myBase().location();
        Entity target = hero.target();
        Vector nextLocation = target.nextLocation();
        Vector monsterVector = nextLocation.subtract(baseLocation).normalize().multiply(GameConstants.MONSTER_DISTANCE_PER_TURN);
        Vector redirectVelocity;
        if (baseLocation.x() == 0) {
            redirectVelocity = Vector.of(1, 0).dot(monsterVector) >= Vector.of(1, 0).dot(Vector.of(1, 1))
                    ? monsterVector.cross(1)
                    : monsterVector.cross(-1);
        } else {
            redirectVelocity = Vector.of(-1, 0).dot(monsterVector) >= Vector.of(-1, 0).dot(Vector.of(-1, -1))
                    ? monsterVector.cross(1)
                    : monsterVector.cross(-1);
        }
        hero.control(target, nextLocation.add(redirectVelocity));
        succeed();
    }
}
