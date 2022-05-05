package com.priitlaht.challenge.game.strategy.behaviours;

import com.priitlaht.challenge.game.GameConstants;
import com.priitlaht.challenge.game.model.Hero;
import com.priitlaht.challenge.game.strategy.AiContext;
import com.priitlaht.challenge.game.strategy.actions.CastShieldOnTarget;
import com.priitlaht.challenge.game.strategy.actions.PatrolNearEnemyBase;
import com.priitlaht.challenge.game.strategy.actions.PushTargetToEnemyBase;
import com.priitlaht.challenge.game.strategy.actions.RedirectTargetToEnemyBase;
import com.priitlaht.challenge.game.strategy.conditions.HasEnoughMana;
import com.priitlaht.challenge.game.strategy.conditions.IsHeroInRole;
import com.priitlaht.challenge.game.strategy.conditions.IsRoundAtLeast;
import com.priitlaht.challenge.game.strategy.conditions.IsTargetWithinRangeOfHero;
import com.priitlaht.challenge.game.strategy.engine.Fallback;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import com.priitlaht.challenge.game.strategy.engine.Sequence;
import com.priitlaht.challenge.game.strategy.helpers.TargetMonsterClosestToEnemyBaseWithinRange;
import com.priitlaht.challenge.game.strategy.helpers.TargetMonsterClosestToHeroThatCanBeSentToEnemyBase;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AttackEnemyBase extends Sequence {
    public static Routine of() {
        AttackEnemyBase attackBase = new AttackEnemyBase();
        attackBase
                .addRoutine(IsHeroInRole.of(Hero.Role.JUNGLER))
                .addRoutine(IsRoundAtLeast.of(95))
                .addRoutine(Fallback.of(
                        Sequence.of(
                                HasEnoughMana.of(),
                                TargetMonsterClosestToEnemyBaseWithinRange.of(AiContext.NEAR_BASE_THRESHOLD),
                                Fallback.of(
                                        Sequence.of(
                                                IsTargetWithinRangeOfHero.of(GameConstants.SPELL_WIND_RADIUS),
                                                PushTargetToEnemyBase.of()),
                                        Sequence.of(
                                                IsTargetWithinRangeOfHero.of(GameConstants.SPELL_SHIELD_RADIUS),
                                                CastShieldOnTarget.of())
                                )
                        ),
                        Sequence.of(
                                HasEnoughMana.of(150),
                                TargetMonsterClosestToHeroThatCanBeSentToEnemyBase.of(),
                                RedirectTargetToEnemyBase.of(GameConstants.MONSTER_DISTANCE_PER_TURN)),
                        Sequence.of(
                                HasEnoughMana.of(150),
                                PatrolNearEnemyBase.of()
                        )
                ));
        return attackBase;
    }
}
