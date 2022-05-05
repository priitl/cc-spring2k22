package com.priitlaht.challenge.game.strategy.behaviours;

import com.priitlaht.challenge.game.GameConstants;
import com.priitlaht.challenge.game.strategy.AiContext;
import com.priitlaht.challenge.game.strategy.actions.InterceptTarget;
import com.priitlaht.challenge.game.strategy.actions.PushTargetAwayFromMyBase;
import com.priitlaht.challenge.game.strategy.actions.RedirectTargetAwayFromMyBase;
import com.priitlaht.challenge.game.strategy.conditions.*;
import com.priitlaht.challenge.game.strategy.engine.Fallback;
import com.priitlaht.challenge.game.strategy.engine.Inverter;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import com.priitlaht.challenge.game.strategy.engine.Sequence;
import com.priitlaht.challenge.game.strategy.helpers.TargetClosestMonsterThreateningMyBase;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DefendBaseFromMonsters extends Sequence {
    public static Routine of() {
        DefendBaseFromMonsters defendBase = new DefendBaseFromMonsters();
        defendBase
                .addRoutine(TargetClosestMonsterThreateningMyBase.of())
                .addRoutine(Fallback.of(
                        Sequence.of(
                                HasEnoughMana.of(),
                                Inverter.of(IsTargetShielded.of()),
                                Fallback.of(
                                        Inverter.of(IsTargetKillableBeforeMyBase.of()),
                                        IsEnemyHeroWithinRangeOfTarget.of(GameConstants.SPELL_WIND_RADIUS + GameConstants.MONSTER_DISTANCE_PER_TURN)),
                                IsTargetNextLocationWithinRangeOfMyBase.of(AiContext.MONSTER_BASE_DETECTION_THRESHOLD),
                                IsTargetWithinRangeOfHero.of(GameConstants.SPELL_WIND_RADIUS),
                                PushTargetAwayFromMyBase.of()),
                        Sequence.of(
                                HasEnoughMana.of(),
                                IsRoundAtLeast.of(95),
                                IsTargetThreateningMyBase.of(),
                                Inverter.of(IsTargetShielded.of()),
                                Inverter.of(IsTargetControlled.of()),
                                Inverter.of(IsTargetNextLocationWithinRangeOfMyBase.of(GameConstants.MONSTER_BASE_TARGET_RADIUS)),
                                IsTargetNextLocationWithinRangeOfMyBase.of(AiContext.NEAR_BASE_THRESHOLD),
                                IsTargetWithinRangeOfHero.of(GameConstants.SPELL_CONTROL_RADIUS),
                                RedirectTargetAwayFromMyBase.of(GameConstants.MONSTER_DISTANCE_PER_TURN)),
                        InterceptTarget.of()
                ));
        return defendBase;
    }
}
