package com.priitlaht.challenge.game.strategy.conditions;

import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.model.Hero;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
public class HasReachedMaxRoles extends Routine {
    private final Hero.Role role;
    private final int maxAllowed;

    @Override
    public void play(int heroId) {
        long count = GameState.instance().heroes().values().stream()
                .filter(hero -> this.role == hero.role() && hero.id() != heroId)
                .count();
        if (count >= maxAllowed) {
            succeed();
        } else {
            fail();
        }
    }
}
