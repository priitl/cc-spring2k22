package com.priitlaht.challenge.game.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Hero extends Entity {
    public static final Point[] ORIGINS = new Point[]{Point.of(10800, 5200), Point.of(7200, 1200), Point.of(4000, 6800)};
    final Point origin;
    Strategy strategy;

    public void updateStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public enum Strategy {
        DEFEND, FARM, HARASS
    }
}
