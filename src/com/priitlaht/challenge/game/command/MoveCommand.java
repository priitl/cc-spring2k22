package com.priitlaht.challenge.game.command;

import com.priitlaht.challenge.game.model.Point;
import lombok.Value;

@Value(staticConstructor = "of")
public class MoveCommand implements Command {
    Point target;

    @Override
    public void execute() {
        System.out.printf("MOVE %d %d%n", target().x(), target.y());
    }
}
