package com.priitlaht.challenge.game.strategy.conditions;

import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.model.Entity;
import com.priitlaht.challenge.game.model.Hero;
import com.priitlaht.challenge.game.model.Monster;
import com.priitlaht.challenge.game.model.Point;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
public class IsTargetKillableBeforeMyBase extends Routine {
    @Override
    public void play(int heroId) {
        Entity target = GameState.instance().hero(heroId).target();
        if (!(target instanceof Monster)) {
            fail();
            return;
        }
        Point baseLocation = GameState.instance().myBase().location();
        Hero hero = GameState.instance().hero(heroId);
        Monster monster = (Monster) target;
        int timeToKill = monster.health() / Hero.DAMAGE;
        double targetTimeToBase = Math.ceil(monster.distance(baseLocation) / Monster.DISTANCE_PER_TURN);
        double timeToTarget = Math.ceil(hero.distance(monster) / (Hero.DISTANCE_PER_TURN - Monster.DISTANCE_PER_TURN)) - 1;
        if (targetTimeToBase > timeToKill + timeToTarget) {
            succeed();
        } else {
            fail();
        }
    }
}
