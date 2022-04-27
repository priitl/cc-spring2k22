package com.priitlaht.challenge.game.strategy;

import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.command.Command;
import com.priitlaht.challenge.game.command.MoveCommand;
import com.priitlaht.challenge.game.model.Hero;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor(staticName = "of")
public class DefenderStrategy extends Strategy {
    @Override
    public Command resolve(Hero hero, GameState state) {
        Optional<Command> defendBaseCommand = commandFactory.defendBaseCommand(hero, state);
        if (defendBaseCommand.isPresent()) {
            return defendBaseCommand.get();
        }
        Optional<Command> windCommand = commandFactory.windMonsters(hero, state);
        if (windCommand.isPresent()) {
            return windCommand.get();
        }
        Optional<Command> controlCommand = commandFactory.controlCommand(hero, state);
        if (controlCommand.isPresent()) {
            return controlCommand.get();
        }
        Optional<Command> moveToMonsterCommand = commandFactory.moveToMonster(hero, state);
        if (moveToMonsterCommand.isPresent()) {
            return moveToMonsterCommand.get();
        }
        return MoveCommand.of(hero.origin());
    }
}