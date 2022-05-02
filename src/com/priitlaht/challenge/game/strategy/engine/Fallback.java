package com.priitlaht.challenge.game.strategy.engine;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Fallback extends Routine {
    protected Routine currentRoutine;
    protected final List<Routine> routines = new LinkedList<>();
    protected final Queue<Routine> routineQueue = new LinkedList<>();

    public static Routine of(Routine... routines) {
        Fallback selector = new Fallback();
        Arrays.stream(routines).forEach(selector::addRoutine);
        return selector;
    }

    public Fallback addRoutine(Routine routine) {
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
        if (currentRoutine.succeeded() || routineQueue.peek() == null) {
            updateStatus(currentRoutine.status);
            return;
        }
        currentRoutine = routineQueue.poll();
        currentRoutine.start();
        play(heroId);
    }

    @Override
    public void reset() {
        routines.forEach(Routine::reset);
        this.start();
    }
}
