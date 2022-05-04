package com.priitlaht.challenge.game.strategy.helpers;

import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.model.Hero;
import com.priitlaht.challenge.game.model.Monster;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

@NoArgsConstructor(staticName = "of")
public class TargetMonsterClosestToHero extends Routine {
    @Override
    public void play(int heroId) {
        Collection<Monster> visibleMonsters = GameState.instance().monsters().values();
        Hero hero = GameState.instance().hero(heroId);
        Optional<Monster> target = visibleMonsters.stream()
                .filter(hero::isAssignedOrClosestTo)
                .min(Comparator.comparing(monster -> monster.distance(hero)));
        if (target.isPresent()) {
            hero.updateTarget(target.get());
            succeed();
        } else {
            fail();
        }
    }
}
