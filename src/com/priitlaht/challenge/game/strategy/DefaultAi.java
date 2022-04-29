package com.priitlaht.challenge.game.strategy;

import com.priitlaht.challenge.game.strategy.actions.DefendBase;
import com.priitlaht.challenge.game.strategy.actions.MoveToOrigin;
import com.priitlaht.challenge.game.strategy.actions.StayInPlace;
import com.priitlaht.challenge.game.strategy.conditions.IsBaseInDanger;
import com.priitlaht.challenge.game.strategy.engine.FallbackNode;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import com.priitlaht.challenge.game.strategy.engine.SequenceNode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DefaultAi extends FallbackNode {
    public static Routine of() {
        DefaultAi defaultAi = new DefaultAi();
        defaultAi
                .addRoutine(SequenceNode.sequence(IsBaseInDanger.of(), DefendBase.of()))
                .addRoutine(MoveToOrigin.of())
                .addRoutine(StayInPlace.of());
        return defaultAi;
    }

    @Override
    protected boolean debug() {
        return true;
    }
}
