package com.priitlaht.challenge.game;

import com.priitlaht.challenge.game.model.Hero;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Arrays;
import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Game {
    GameState state;

    public static Game of(int myBaseX, int myBaseY) {
        int enemyBaseX = Math.abs(GameConstants.FIELD_WIDTH - myBaseX);
        int enemyBaseY = Math.abs(GameConstants.FIELD_HEIGHT - myBaseY);
        GameState state = GameState.instance();
        state.myBase().location(myBaseX, myBaseY);
        state.opponentBase().location(enemyBaseX, enemyBaseY);
        return new Game(state);
    }

    public void playRound(RoundInfo roundInfo) {
        state.update(roundInfo);
        state.heroes().values().forEach(Hero::playRound);
        state.heroes().values().forEach(hero -> System.out.println(hero.action()));
    }

    @Getter
    @Builder
    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    public static class RoundInfo {
        int myBaseHealth;
        int myBaseMana;
        int opponentBaseHealth;
        int opponentBaseMana;
        @Singular("addEntity")
        List<EntityInfo> entityInfos;

        @Getter
        @Builder
        @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
        public static class EntityInfo {
            int id;
            int type;
            int x;
            int y;
            int shieldLife;
            int health;
            int vx;
            int vy;
            int nearBase;
            int threatFor;
            boolean isControlled;

            @RequiredArgsConstructor
            public enum Type {
                MONSTER(0), HERO(1), ENEMY(2);
                final int value;

                public static Type getByValue(int value) {
                    return Arrays.stream(Type.values()).filter(type -> type.value == value).findFirst().orElse(null);
                }
            }
        }
    }
}
