package com.priitlaht.challenge.game.strategy;

import com.priitlaht.challenge.game.GameConstants;
import com.priitlaht.challenge.game.model.Hero;
import com.priitlaht.challenge.game.strategy.actions.MoveToGuardPosition;
import com.priitlaht.challenge.game.strategy.actions.MoveToNearestMonsterSpawnPosition;
import com.priitlaht.challenge.game.strategy.actions.StayInPlace;
import com.priitlaht.challenge.game.strategy.behaviours.*;
import com.priitlaht.challenge.game.strategy.conditions.IsHeroInRole;
import com.priitlaht.challenge.game.strategy.conditions.IsHeroWithinRangeOfEnemyBase;
import com.priitlaht.challenge.game.strategy.engine.*;

public class DefaultAi extends RepeatForever {
    public static final AiContext CONTEXT = AiContext.of();

    protected DefaultAi(Routine routine) {
        super(routine);
    }

    public static Routine of() {
        return new DefaultAi(Fallback.of(
                ShieldHero.of(),
                Sequence.of(
                        IsHeroInRole.of(Hero.Role.DEFENDER),
                        Fallback.of(
                                DefendBaseFromMonsters.of(),
                                DefendBaseFromEnemyHeroes.of(),
                                FarmMana.of(),
                                MoveToGuardPosition.of())),
                Sequence.of(IsHeroInRole.of(Hero.Role.JUNGLER),
                        Fallback.of(
                                AttackEnemyBase.of(),
                                Sequence.of(
                                        Inverter.of(IsHeroWithinRangeOfEnemyBase.of(GameConstants.MONSTER_BASE_TARGET_RADIUS)),
                                        FarmMana.of()),
                                MoveToNearestMonsterSpawnPosition.of())),
                StayInPlace.of()
        ));
    }

    @Override
    protected boolean debug() {
        return true;
    }
}
