package com.priitlaht.challenge.game.strategy;

import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.command.Command;
import com.priitlaht.challenge.game.model.Hero;

public abstract class Strategy {
    final StrategyCommandFactory commandFactory = StrategyCommandFactory.getInstance();

    public abstract Command resolve(Hero hero, GameState state);
}
