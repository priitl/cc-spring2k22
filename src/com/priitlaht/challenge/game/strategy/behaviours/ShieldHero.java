package com.priitlaht.challenge.game.strategy.behaviours;

import com.priitlaht.challenge.game.model.Hero;
import com.priitlaht.challenge.game.strategy.actions.CastShieldOnHero;
import com.priitlaht.challenge.game.strategy.conditions.HasEnoughMana;
import com.priitlaht.challenge.game.strategy.conditions.IsEnemyHeroWithinRangeOfHero;
import com.priitlaht.challenge.game.strategy.conditions.IsHeroShielded;
import com.priitlaht.challenge.game.strategy.conditions.IsRoundAtLeast;
import com.priitlaht.challenge.game.strategy.engine.Inverter;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import com.priitlaht.challenge.game.strategy.engine.Sequence;
import com.priitlaht.challenge.game.strategy.helpers.UpdateHeroRole;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ShieldHero extends Sequence {
    public static Routine of() {
        ShieldHero shieldMyself = new ShieldHero();
        shieldMyself
                .addRoutine(IsRoundAtLeast.of(95))
                .addRoutine(IsEnemyHeroWithinRangeOfHero.of(Hero.VISION_RADIUS))
                .addRoutine(Inverter.of(IsHeroShielded.of()))
                .addRoutine(HasEnoughMana.of(50))
                .addRoutine(UpdateHeroRole.of(Hero.Role.JUNGLER))
                .addRoutine(CastShieldOnHero.of());
        return shieldMyself;
    }
}