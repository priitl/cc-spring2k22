package com.priitlaht.challenge.game.strategy.helpers;

import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.model.Hero;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
public class UpdateHeroRole extends Routine {
    private final Hero.Role role;

    @Override
    public void play(int heroId) {
        GameState.instance().hero(heroId).updateRole(role);
        succeed();
    }
}
