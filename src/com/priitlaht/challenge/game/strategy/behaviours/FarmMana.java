package com.priitlaht.challenge.game.strategy.behaviours;

import com.priitlaht.challenge.game.GameConstants;
import com.priitlaht.challenge.game.model.Hero;
import com.priitlaht.challenge.game.strategy.actions.AttackArea;
import com.priitlaht.challenge.game.strategy.actions.InterceptTarget;
import com.priitlaht.challenge.game.strategy.conditions.IsHeroInRole;
import com.priitlaht.challenge.game.strategy.conditions.IsHeroWithinRangeOfGuardPosition;
import com.priitlaht.challenge.game.strategy.conditions.IsHeroWithinRangeOfMyBase;
import com.priitlaht.challenge.game.strategy.engine.Fallback;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import com.priitlaht.challenge.game.strategy.engine.Sequence;
import com.priitlaht.challenge.game.strategy.helpers.TargetMonsterClosestToHero;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FarmMana extends Sequence {
    public static Routine of() {
        FarmMana farmMana = new FarmMana();
        farmMana
                .addRoutine(Fallback.of(
                        IsHeroInRole.of(Hero.Role.JUNGLER),
                        Sequence.of(
                                IsHeroWithinRangeOfMyBase.of(10000),
                                IsHeroWithinRangeOfGuardPosition.of(GameConstants.HERO_VISION_RADIUS)
                        )))
                .addRoutine(Fallback.of(
                        AttackArea.of(),
                        Sequence.of(TargetMonsterClosestToHero.of(), InterceptTarget.of())));
        return farmMana;
    }
}