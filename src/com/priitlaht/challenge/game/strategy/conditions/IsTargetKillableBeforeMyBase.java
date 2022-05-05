package com.priitlaht.challenge.game.strategy.conditions;

import com.priitlaht.challenge.game.GameConstants;
import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.model.Entity;
import com.priitlaht.challenge.game.model.Hero;
import com.priitlaht.challenge.game.model.Monster;
import com.priitlaht.challenge.game.model.Vector;
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
        Vector baseLocation = GameState.instance().myBase().location();
        Hero hero = GameState.instance().hero(heroId);
        Monster monster = (Monster) target;
        int timeToKill = monster.health() / GameConstants.HERO_DAMAGE;
        double targetTimeToBase = Math.ceil((monster.distance(baseLocation) - GameConstants.MONSTER_BASE_KILL_RADIUS) / GameConstants.MONSTER_DISTANCE_PER_TURN);
        double timeToTarget = Math.ceil(hero.distance(monster) / (GameConstants.HERO_DISTANCE_PER_TURN - GameConstants.MONSTER_DISTANCE_PER_TURN));
        if (targetTimeToBase > timeToKill + timeToTarget) {
            succeed();
        } else {
            fail();
        }
    }
}
