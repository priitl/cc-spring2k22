package com.priitlaht.challenge.game.strategy.behaviours;

import com.priitlaht.challenge.game.GameConstants;
import com.priitlaht.challenge.game.strategy.actions.CastShieldOnHero;
import com.priitlaht.challenge.game.strategy.conditions.HasEnoughMana;
import com.priitlaht.challenge.game.strategy.conditions.IsEnemyHeroWithinRangeOfHero;
import com.priitlaht.challenge.game.strategy.conditions.IsHeroShielded;
import com.priitlaht.challenge.game.strategy.conditions.IsRoundAtLeast;
import com.priitlaht.challenge.game.strategy.engine.Inverter;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import com.priitlaht.challenge.game.strategy.engine.Sequence;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ShieldHero extends Sequence {
    public static Routine of() {
        ShieldHero shieldMyself = new ShieldHero();
        shieldMyself
                .addRoutine(IsRoundAtLeast.of(95))
                .addRoutine(IsEnemyHeroWithinRangeOfHero.of(GameConstants.HERO_VISION_RADIUS))
                .addRoutine(Inverter.of(IsHeroShielded.of()))
                .addRoutine(HasEnoughMana.of(50))
                .addRoutine(CastShieldOnHero.of());
        return shieldMyself;
    }
}