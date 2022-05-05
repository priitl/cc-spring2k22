package com.priitlaht.challenge.game.strategy.behaviours;

import com.priitlaht.challenge.game.GameConstants;
import com.priitlaht.challenge.game.strategy.AiContext;
import com.priitlaht.challenge.game.strategy.actions.InterceptTarget;
import com.priitlaht.challenge.game.strategy.actions.PushTargetAwayFromMyBase;
import com.priitlaht.challenge.game.strategy.conditions.HasEnoughMana;
import com.priitlaht.challenge.game.strategy.conditions.IsTargetShielded;
import com.priitlaht.challenge.game.strategy.conditions.IsTargetWithinRangeOfHero;
import com.priitlaht.challenge.game.strategy.engine.Fallback;
import com.priitlaht.challenge.game.strategy.engine.Inverter;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import com.priitlaht.challenge.game.strategy.engine.Sequence;
import com.priitlaht.challenge.game.strategy.helpers.TargetClosestEnemyHeroWithinRangeOfMyBase;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DefendBaseFromEnemyHeroes extends Sequence {
    public static Routine of() {
        DefendBaseFromEnemyHeroes defendBase = new DefendBaseFromEnemyHeroes();
        defendBase
                .addRoutine(TargetClosestEnemyHeroWithinRangeOfMyBase.of(AiContext.NEAR_BASE_THRESHOLD))
                .addRoutine(Fallback.of(
                        Sequence.of(
                                HasEnoughMana.of(),
                                Inverter.of(IsTargetShielded.of()),
                                IsTargetWithinRangeOfHero.of(GameConstants.SPELL_WIND_RADIUS - GameConstants.HERO_DISTANCE_PER_TURN),
                                PushTargetAwayFromMyBase.of()),
                        InterceptTarget.of()));
        return defendBase;
    }
}
