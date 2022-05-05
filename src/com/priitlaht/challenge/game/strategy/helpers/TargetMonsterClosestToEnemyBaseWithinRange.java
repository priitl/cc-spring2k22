package com.priitlaht.challenge.game.strategy.helpers;

import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.model.Base;
import com.priitlaht.challenge.game.model.Hero;
import com.priitlaht.challenge.game.model.Monster;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

@RequiredArgsConstructor(staticName = "of")
public class TargetMonsterClosestToEnemyBaseWithinRange extends Routine {
    private final int range;

    @Override
    public void play(int heroId) {
        Collection<Monster> visibleMonsters = GameState.instance().monsters().values();
        Hero hero = GameState.instance().hero(heroId);
        Base opponentBase = GameState.instance().opponentBase();
        Optional<Monster> target = visibleMonsters.stream()
                .filter(monster -> !monster.isShielded() && monster.isThreateningBase(opponentBase)
                        && monster.health() > 10
                        && monster.distance(opponentBase.location()) <= range)
                .min(Comparator.comparing(monster -> monster.distance(hero)));
        if (target.isPresent()) {
            hero.updateTarget(target.get());
            succeed();
        } else {
            fail();
        }
    }
}