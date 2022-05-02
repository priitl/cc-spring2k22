package com.priitlaht.challenge.game.strategy.actions;

import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.model.Entity;
import com.priitlaht.challenge.game.model.Hero;
import com.priitlaht.challenge.game.model.Monster;
import com.priitlaht.challenge.game.model.Point;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import lombok.NoArgsConstructor;


@NoArgsConstructor(staticName = "of")
public class RedirectTargetAwayFromByBase extends Routine {
    @Override
    public void play(int heroId) {
        Hero hero = GameState.instance().hero(heroId);
        Point baseLocation = GameState.instance().myBase().location();
        Entity target = hero.target();
        Point nextLocation = target.nextLocation();
        Point monsterVector = nextLocation.subtract(baseLocation).normalize().multiply(Monster.DISTANCE_PER_TURN);
        Point redirectVelocity;
        if (baseLocation.x() == 0) {
            redirectVelocity = Point.dot(Point.of(1, 0), monsterVector) >= Point.dot(Point.of(1, 0), Point.of(1, 1))
                    ? monsterVector.clockwise()
                    : monsterVector.counterClockwise();
        } else {
            redirectVelocity = Point.dot(Point.of(-1, 0), monsterVector) >= Point.dot(Point.of(-1, 0), Point.of(-1, -1))
                    ? monsterVector.clockwise()
                    : monsterVector.counterClockwise();
        }
        hero.control(target, nextLocation.add(redirectVelocity));
        succeed();
    }
}
