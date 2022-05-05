package com.priitlaht.challenge.game;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GameConstants {
    public static final int FIELD_WIDTH = 17630;
    public static final int FIELD_HEIGHT = 9000;
    public static int MAP_LIMIT = 800;

    public static final int SPELL_MANA_COST = 10;
    public static final int SPELL_CONTROL_RADIUS = 2200;
    public static final int SPELL_WIND_RADIUS = 1280;
    public static final int SPELL_WIND_FORCE = 2200;
    public static final int SPELL_SHIELD_RADIUS = 2200;
    public static final int SPELL_SHIELD_DURATION = 12;

    public static final int BASE_VISION_RADIUS = 6000;
    public static final int BASE_INITIAL_HEALTH = 3;
    public static final int BASE_INITIAL_MANA = 0;

    public static final int HERO_DAMAGE_RADIUS = 800;
    public static final int HERO_DISTANCE_PER_TURN = 800;
    public static final int HERO_VISION_RADIUS = 2200;
    public static final int HERO_DAMAGE = 2;

    public static final int MONSTER_DISTANCE_PER_TURN = 400;
    public static final int MONSTER_BASE_TARGET_RADIUS = 5000;
    public static final int MONSTER_BASE_KILL_RADIUS = 300;
}
