package com.priitlaht.challenge.game.command;

import lombok.Value;

@Value(staticConstructor = "of")
public class ShieldCommand implements Command {
    public static final int RADIUS = 2200;
    int entityId;

    @Override
    public void execute() {
        System.out.printf("SPELL SHIELD %d%n", entityId);
    }
}
