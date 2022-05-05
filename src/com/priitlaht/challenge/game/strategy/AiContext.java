package com.priitlaht.challenge.game.strategy;

import lombok.NoArgsConstructor;

import java.util.HashMap;

@NoArgsConstructor(staticName = "of")
public class AiContext extends HashMap<String, Object> {
    public static final int MAX_DEFENDERS = 2;
    public static final int MAX_HARASSERS = 1;

    public static final int MONSTER_BASE_DETECTION_THRESHOLD = 5000;
    public static final int NEAR_BASE_THRESHOLD = 7000;
    public static final int ENEMY_NEAR_BASE_THRESHOLD = 8000;
}
