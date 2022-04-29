package com.priitlaht.challenge.game.strategy.engine;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FallbackNode extends Routine {
    private Routine currentRoutine;
    private final List<Routine> routines = new LinkedList<>();
    private final Queue<Routine> routineQueue = new LinkedList<>();

    public static Routine selector(Routine... routines) {
        FallbackNode selector = new FallbackNode();
        Arrays.stream(routines).forEach(selector::addRoutine);
        return selector;
    }

    public FallbackNode addRoutine(Routine routine) {
        routines.add(routine);
        return this;
    }

    @Override
    public void start() {
        super.start();
        routineQueue.clear();
        routineQueue.addAll(routines);
        currentRoutine = routineQueue.poll();
        if (currentRoutine != null) {
            currentRoutine.start();
        }
    }

    @Override
    public void play(int heroId) {
        currentRoutine.play(heroId);
        if (currentRoutine.isRunning()) {
            return;
        }

        if (currentRoutine.succeeded()) {
            succeed();
            return;
        }

        if (routineQueue.peek() == null) {
            this.status = currentRoutine.status;
        } else {
            currentRoutine = routineQueue.poll();
            currentRoutine.start();
            currentRoutine.play(heroId);
        }
    }

    @Override
    public void reset() {
        routines.forEach(Routine::reset);
    }
}
