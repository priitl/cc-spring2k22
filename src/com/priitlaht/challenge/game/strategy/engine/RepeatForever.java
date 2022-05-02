package com.priitlaht.challenge.game.strategy.engine;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class RepeatForever extends Routine {
    private final Routine routine;

    @Override
    public void start() {
        super.start();
        this.routine.start();
    }

    @Override
    public void play(int heroId) {
        if (routine.succeeded()) {
            routine.reset();
        }
        if (routine.failed()) {
            fail();
        }
        routine.play(heroId);
    }

    @Override
    public void reset() {
        this.routine.reset();
        this.start();
    }
}
