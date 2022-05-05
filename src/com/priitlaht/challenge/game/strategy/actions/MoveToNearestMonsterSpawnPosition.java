package com.priitlaht.challenge.game.strategy.actions;

import com.priitlaht.challenge.game.GameConstants;
import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.model.Hero;
import com.priitlaht.challenge.game.model.Vector;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Comparator;

@NoArgsConstructor(staticName = "of")
public class MoveToNearestMonsterSpawnPosition extends Routine {
    private static final Vector[] MOB_SPAWN_LOCATIONS = new Vector[]{
            Vector.of(GameConstants.FIELD_WIDTH / 2.0, -GameConstants.MAP_LIMIT + 1),
            Vector.of(GameConstants.FIELD_WIDTH / 2.0, GameConstants.FIELD_HEIGHT + GameConstants.MAP_LIMIT - 1),
            Vector.of((GameConstants.FIELD_WIDTH / 2.0) + 4000, -GameConstants.MAP_LIMIT + 1),
            Vector.of((GameConstants.FIELD_WIDTH / 2.0) - 4000, GameConstants.FIELD_HEIGHT + GameConstants.MAP_LIMIT - 1),
    };

    @Override
    public void play(int heroId) {
        Hero currentHero = GameState.instance().hero(heroId);
        Vector target = Arrays.stream(MOB_SPAWN_LOCATIONS)
                .min(Comparator.comparing(location -> location.distance(currentHero.location())))
                .orElse(MOB_SPAWN_LOCATIONS[0]);
        currentHero.moveTo(target);
        succeed();
    }
}
