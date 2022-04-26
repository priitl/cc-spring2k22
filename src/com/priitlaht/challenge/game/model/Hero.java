package com.priitlaht.challenge.game.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Hero extends Entity {
    public static final int DAMAGE_RADIUS = 300;
    public static final int VISION_RADIUS = 2200;
    public static final Point[] ORIGINS = new Point[]{Point.of(13800, 5200), Point.of(7200, 2200), Point.of(4000, 7800)};
    public static final Type[] TYPES = new Type[]{Type.HARASSER, Type.DEFENDER, Type.DEFENDER};

    final Point origin;
    Type type;

    public enum Type {
        HARASSER, DEFENDER, JUNGLER
    }
}
