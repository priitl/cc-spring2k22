package com.priitlaht.challenge.game.strategy;

import com.priitlaht.challenge.game.strategy.actions.MoveToOrigin;
import com.priitlaht.challenge.game.strategy.actions.StayInPlace;
import com.priitlaht.challenge.game.strategy.behaviours.DefendBase;
import com.priitlaht.challenge.game.strategy.behaviours.FarmMana;
import com.priitlaht.challenge.game.strategy.behaviours.ShieldHero;
import com.priitlaht.challenge.game.strategy.engine.Fallback;
import com.priitlaht.challenge.game.strategy.engine.RepeatForever;
import com.priitlaht.challenge.game.strategy.engine.Routine;

public class DefaultAi extends RepeatForever {
    public static final AiContext CONTEXT = AiContext.of();

    protected DefaultAi(Routine routine) {
        super(routine);
    }

    public static Routine of() {
        return new DefaultAi(Fallback.of(
                ShieldHero.of(),
                DefendBase.of(),
                FarmMana.of(),
                MoveToOrigin.of(),
                StayInPlace.of()
        ));
    }

    @Override
    protected boolean debug() {
        return true;
    }
}
