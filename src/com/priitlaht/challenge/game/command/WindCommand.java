package com.priitlaht.challenge.game.command;

import com.priitlaht.challenge.game.GameConstants;
import com.priitlaht.challenge.game.model.Point;
import lombok.Value;

@Value(staticConstructor = "of")
public class WindCommand implements Command {
    public static final int RADIUS = 1280;
    public static final int FORCE = 2200;
    Point target;

    @Override
    public void execute() {
        System.out.printf("SPELL WIND %d %d%n", target.x(), target.y());
    }

    @Override
    public int manaCost() {
        return GameConstants.SPELL_MANA_COST;
    }
}
