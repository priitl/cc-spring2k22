package com.priitlaht.challenge.game.strategy.engine;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
public class Inverter extends Routine {
    private final Routine routine;

    @Override
    public void start() {
        super.start();
        if (routine != null) {
            routine.start();
        }
    }

    @Override
    public void play(int heroId) {
        routine.play(heroId);
        if (routine.failed()) {
            succeed();
        }
        if (routine.succeeded()) {
            fail();
        }
    }
}
