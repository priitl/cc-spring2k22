package com.priitlaht.challenge.game.strategy.engine;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Repeat extends Routine {
    private final Routine routine;
    private final int times;
    private int timesLeft;

    public static Routine of(Routine routine, int times) {
        Repeat repeatNode = new Repeat(routine, times);
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
        routine.play(heroId);
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
            play(heroId);
        }
    }

    @Override
    public void reset() {
        this.timesLeft = times;
        this.start();
    }
}
