package com.priitlaht.challenge.game;

import com.priitlaht.challenge.game.command.Command;
import com.priitlaht.challenge.game.model.Base;
import com.priitlaht.challenge.game.model.Point;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Arrays;
import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Game {
    GameState state;
    StrategyExecutor strategyExecutor = new StrategyExecutor();

    public static Game of(int myBaseX, int myBaseY) {
        Point myBaseLocation = Point.of(myBaseX, myBaseY);
        int enemyBaseX = Math.abs(GameConstants.FIELD_WIDTH - myBaseLocation.x());
        int enemyBaseY = Math.abs(GameConstants.FIELD_HEIGHT - myBaseLocation.y());
        Point enemyBaseLocation = Point.of(enemyBaseX, enemyBaseY);
        GameState state = GameState.builder()
                .myBase(Base.of(myBaseLocation))
                .opponentBase(Base.of(enemyBaseLocation))
                .build();
        return new Game(state);
    }

    public void playRound(RoundInfo roundInfo) {
        state.update(roundInfo);
        strategyExecutor.execute(state).forEach(Command::execute);
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