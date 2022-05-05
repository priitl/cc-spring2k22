package com.priitlaht.challenge.game.strategy.actions;

import com.priitlaht.challenge.game.GameConstants;
import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.model.Hero;
import com.priitlaht.challenge.game.model.Vector;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@NoArgsConstructor(staticName = "of")
public class PatrolNearEnemyBase extends Routine {
    private static final Vector[] POSITIONS = new Vector[]{
            Vector.of(6001, 300),
            Vector.of(4000, 4000),
            Vector.of(300, 6001)
    };

    @Override
    public void play(int heroId) {
        Vector[] positions = Arrays.copyOf(POSITIONS, POSITIONS.length);
        if (GameState.instance().myBase().location().x() < GameState.instance().opponentBase().location().x()) {
            for (int i = 0; i < (positions).length; i++) {
                positions[i] = Vector.of(GameConstants.FIELD_WIDTH, GameConstants.FIELD_HEIGHT).subtract(positions[i]);
            }
        }
        Hero hero = GameState.instance().hero(heroId);
        hero.patrolBetween(positions);
        succeed();
    }
}
