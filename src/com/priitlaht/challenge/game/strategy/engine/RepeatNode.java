package com.priitlaht.challenge.game.strategy.engine;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RepeatNode extends Routine {
    private final Routine routine;
    private final int times;
    private int timesLeft;

    public static Routine repeat(Routine routine, int times) {
        RepeatNode repeatNode = new RepeatNode(routine, times);
        repeatNode.timesLeft = times;
        return repeatNode;
    }

    @Override
    public void start() {
        super.start();
        this.routine.start();
    }

    @Override
    public void play(int heroId) {
        if (routine.failed()) {
            fail();
        } else if (routine.succeeded()) {
            if (timesLeft == 0) {
                succeed();
                return;
            }
            if (timesLeft > 0) {
                timesLeft--;
                routine.reset();
                routine.start();
            }
        }
        if (routine.isRunning()) {
            routine.play(heroId);
        }
    }

    @Override
    public void reset() {
        this.timesLeft = times;
    }
}
