package com.priitlaht.challenge.game.strategy;

import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.model.Hero;
import lombok.NoArgsConstructor;

@NoArgsConstructor(staticName = "of")
public class DefenderStrategy extends Strategy {
    @Override
    public void play(Hero hero, GameState state) {
        if (!hero.actionPerformed()) {
            shieldClosestHero(hero, state);
        }
        if (!hero.actionPerformed()) {
            defendBaseCommand(hero, state);
        }
        if (!hero.actionPerformed()) {
            windMonsters(hero, state);
        }
        if (!hero.actionPerformed()) {
            getRidOfEnemyCommand(hero, state);
        }
        if (!hero.actionPerformed()) {
            controlMonsterCommand(hero, state);
        }
        if (!hero.actionPerformed()) {
            moveToMonster(hero, state);
        }
        if (!hero.actionPerformed()) {
            moveToRandomPositionNearOrigin(hero, state);
        }
    }

}