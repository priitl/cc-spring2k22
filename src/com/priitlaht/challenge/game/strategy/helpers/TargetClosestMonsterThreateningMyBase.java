package com.priitlaht.challenge.game.strategy.helpers;

import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.model.Base;
import com.priitlaht.challenge.game.model.Hero;
import com.priitlaht.challenge.game.model.Monster;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

@NoArgsConstructor(staticName = "of")
public class TargetClosestMonsterThreateningMyBase extends Routine {
    @Override
    public void play(int heroId) {
        Base myBase = GameState.instance().myBase();
        Collection<Monster> visibleMonsters = GameState.instance().monsters().values();
        Hero hero = GameState.instance().hero(heroId);
        Optional<Monster> target = visibleMonsters.stream()
                .filter(monster -> monster.isThreateningBase(myBase))
                .filter(hero::isAssignedOrClosestTo)
                .min(Comparator.comparing(monster -> monster.distance(myBase.location())));
        if (target.isPresent()) {
            hero.updateTarget(target.get());
            succeed();
        } else {
            fail();
        }
    }
}
