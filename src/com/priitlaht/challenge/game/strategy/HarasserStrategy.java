package com.priitlaht.challenge.game.strategy;

import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.model.Hero;
import lombok.NoArgsConstructor;

@NoArgsConstructor(staticName = "of")
public class HarasserStrategy extends Strategy {
    @Override
    public void play(Hero hero, GameState state) {
        if (!hero.actionPerformed()) {
            score(hero, state);
        }
        if (!hero.actionPerformed()) {
            shieldClosestHero(hero, state);
        }
        if (!hero.actionPerformed()) {
            stayAwayFromMonsters(hero, state);
        }
        if (!hero.actionPerformed()) {
            windMonsters(hero, state);
        }
        if (!hero.actionPerformed()) {
            harassEnemyBase(hero, state);
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
