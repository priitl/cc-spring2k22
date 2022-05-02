package com.priitlaht.challenge.game.strategy.conditions;

import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class HasEnoughMana extends Routine {
    private final int reserved;

    public static HasEnoughMana of() {
        return new HasEnoughMana(0);
    }

    public static HasEnoughMana of(int reserved) {
        return new HasEnoughMana(reserved);
    }

    @Override
    public void play(int heroId) {
        int mana = GameState.instance().myBase().mana();
        if (mana >= reserved) {
            succeed();
        } else {
            fail();
        }
    }
}
