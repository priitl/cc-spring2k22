package com.priitlaht.challenge.game.strategy.engine;

import lombok.Getter;

@Getter
public abstract class Routine {
    protected Status status = Status.IDLE;

    public abstract void play(int heroId);

    public void reset() {
        this.start();
    }

    public boolean isIdle() {
        return Status.IDLE == this.status;
    }

    public boolean isRunning() {
        return Status.RUNNING == this.status;
    }

    public boolean succeeded() {
        return Status.SUCCESS == this.status;
    }

    public boolean failed() {
        return Status.FAILURE == this.status;
    }

    public void start() {
        this.status = Status.RUNNING;
        if (debug()) {
            System.err.printf("Routine: %s started%n", this.getClass().getSimpleName());
        }
    }

    protected void succeed() {
        this.status = Status.SUCCESS;
        if (debug()) {
            System.err.printf("Routine: %s succeeded%n", this.getClass().getSimpleName());
        }
    }

    protected void fail() {
        this.status = Status.FAILURE;
        if (debug()) {
            System.err.printf("Routine: %s failed%n", this.getClass().getSimpleName());
        }
    }

    public enum Status {
        IDLE, RUNNING, SUCCESS, FAILURE
    }

    protected boolean debug() {
        return false;
    }
}
