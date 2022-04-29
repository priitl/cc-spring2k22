package com.priitlaht.challenge.game.strategy.actions;

import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.model.Hero;
import com.priitlaht.challenge.game.model.Monster;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(staticName = "of")
public class DefendBase extends Routine {
    @Override
    public void play(int heroId) {
        Hero hero = GameState.instance().hero(heroId);
        List<Monster> endangeringMonsters = GameState.instance().myBase().endangeringMonsters();
        Optional<Monster> closestMonster = endangeringMonsters.stream()
                .min(Comparator.comparing(monster -> monster.distance(hero)));
        if (closestMonster.isPresent()) {
            hero.moveTo(closestMonster.get().nextLocation());
        } else {
            fail();
        }
    }
}
