package com.priitlaht.challenge.game.command;

import lombok.Value;

@Value(staticConstructor = "of")
public class WaitCommand implements Command {
    @Override
    public void execute() {
        System.out.println("WAIT");
    }

    @Override
    public int manaCost() {
        return 0;
    }
}
