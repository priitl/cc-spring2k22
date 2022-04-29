package com.priitlaht.challenge.game.strategy.engine;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SequenceNode extends Routine {
    private Routine currentRoutine;
    private final List<Routine> routines = new LinkedList<>();
    private final Queue<Routine> routineQueue = new LinkedList<>();

    public static Routine sequence(Routine... routines) {
        SequenceNode sequenceNode = new SequenceNode();
        Arrays.stream(routines).forEach(sequenceNode::addRoutine);
        return sequenceNode;
    }

    public void addRoutine(Routine routine) {
        routines.add(routine);
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

        if (currentRoutine.failed()) {
            fail();
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
