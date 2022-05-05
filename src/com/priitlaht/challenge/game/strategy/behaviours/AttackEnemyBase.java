package com.priitlaht.challenge.game.strategy.behaviours;

import com.priitlaht.challenge.game.GameConstants;
import com.priitlaht.challenge.game.model.Hero;
import com.priitlaht.challenge.game.strategy.AiContext;
import com.priitlaht.challenge.game.strategy.actions.*;
import com.priitlaht.challenge.game.strategy.conditions.*;
import com.priitlaht.challenge.game.strategy.engine.Fallback;
import com.priitlaht.challenge.game.strategy.engine.Inverter;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import com.priitlaht.challenge.game.strategy.engine.Sequence;
import com.priitlaht.challenge.game.strategy.helpers.TargetClosestEnemyHeroWithinRangeOfHero;
import com.priitlaht.challenge.game.strategy.helpers.TargetMonsterClosestToEnemyBaseWithinRange;
import com.priitlaht.challenge.game.strategy.helpers.TargetMonsterClosestToHeroThatCanBeSentToEnemyBase;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AttackEnemyBase extends Sequence {
    public static Routine of() {
        AttackEnemyBase attackBase = new AttackEnemyBase();
        attackBase
                .addRoutine(IsHeroInRole.of(Hero.Role.HARASSER))
                .addRoutine(IsRoundAtLeast.of(95))
                .addRoutine(HasEnoughMana.of())
                .addRoutine(Fallback.of(
                        Sequence.of(
                                TargetMonsterClosestToEnemyBaseWithinRange.of(AiContext.NEAR_BASE_THRESHOLD),
                                Fallback.of(
                                        Sequence.of(
                                                IsTargetWithinRangeOfHero.of(GameConstants.SPELL_WIND_RADIUS),
                                                PushTargetToEnemyBase.of()),
                                        Sequence.of(
                                                IsTargetWithinRangeOfEnemyBase.of(GameConstants.MONSTER_BASE_TARGET_RADIUS),
                                                IsTargetWithinRangeOfHero.of(GameConstants.SPELL_SHIELD_RADIUS),
                                                CastShieldOnTarget.of()))),
                        Sequence.of(
                                TargetClosestEnemyHeroWithinRangeOfHero.of(GameConstants.SPELL_CONTROL_RADIUS),
                                Inverter.of(IsTargetShielded.of()),
                                Inverter.of(IsTargetControlled.of()),
                                IsTargetWithinRangeOfEnemyBase.of(GameConstants.MONSTER_BASE_TARGET_RADIUS),
                                RedirectTargetAwayFromClosestMonster.of(GameConstants.HERO_DISTANCE_PER_TURN)),
                        Sequence.of(
                                HasEnoughMana.of(150),
                                TargetMonsterClosestToHeroThatCanBeSentToEnemyBase.of(),
                                RedirectTargetToEnemyBase.of(GameConstants.MONSTER_DISTANCE_PER_TURN)),
                        Sequence.of(
                                HasEnoughMana.of(150),
                                PatrolNearEnemyBase.of())
                ));
        return attackBase;
    }
}
