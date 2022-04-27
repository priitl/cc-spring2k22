package com.priitlaht.challenge.game.strategy;

import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.command.Command;
import com.priitlaht.challenge.game.model.Hero;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor(staticName = "of")
public class HarasserStrategy extends Strategy {
    @Override
    public Command resolve(Hero hero, GameState state) {
        Optional<Command> windCommand = commandFactory.windMonsters(hero, state);
        if (windCommand.isPresent()) {
            return windCommand.get();
        }
        Optional<Command> shieldClosestHeroCommand = commandFactory.shieldClosestHero(hero, state);
        if (shieldClosestHeroCommand.isPresent()) {
            return shieldClosestHeroCommand.get();
        }
        Optional<Command> stayAwayFromMonstersCommand = commandFactory.stayAwayFromMonsters(hero, state);
        if (stayAwayFromMonstersCommand.isPresent()) {
            return stayAwayFromMonstersCommand.get();
        }
        Optional<Command> harassEnemyBaseCommand = commandFactory.harassEnemyBase(hero, state);
        if (harassEnemyBaseCommand.isPresent()) {
            return harassEnemyBaseCommand.get();
        }
        Optional<Command> controlCommand = commandFactory.controlMonsterCommand(hero, state);
        if (controlCommand.isPresent()) {
            return controlCommand.get();
        }
        Optional<Command> moveToMonsterCommand = commandFactory.moveToMonster(hero, state);
        if (moveToMonsterCommand.isPresent()) {
            return moveToMonsterCommand.get();
        }
        return commandFactory.moveToRandomPositionNearOrigin(hero, state);
    }
}
