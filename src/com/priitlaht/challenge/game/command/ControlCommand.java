package com.priitlaht.challenge.game.command;

import com.priitlaht.challenge.game.GameConstants;
import com.priitlaht.challenge.game.model.Point;
import lombok.Value;

@Value(staticConstructor = "of")
public class ControlCommand implements Command {
    public static final int RADIUS = 2200;

    int entityId;
    Point target;

    @Override
    public void execute() {
        System.out.printf("SPELL CONTROL %d %d %d%n", entityId, target.x(), target.y());
    }

    @Override
    public int manaCost() {
        return GameConstants.SPELL_MANA_COST;
    }
}
