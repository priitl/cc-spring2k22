package com.priitlaht.challenge.game.strategy.behaviours;

import com.priitlaht.challenge.game.model.Hero;
import com.priitlaht.challenge.game.strategy.actions.AttackArea;
import com.priitlaht.challenge.game.strategy.actions.AttackTarget;
import com.priitlaht.challenge.game.strategy.engine.Fallback;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import com.priitlaht.challenge.game.strategy.engine.Sequence;
import com.priitlaht.challenge.game.strategy.helpers.TargetMonsterClosestToHero;
import com.priitlaht.challenge.game.strategy.helpers.UpdateHeroRole;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FarmMana extends Sequence {
    public static Routine of() {
        FarmMana farmMana = new FarmMana();
        farmMana
                .addRoutine(Fallback.of(
                        AttackArea.of(),
                        Sequence.of(TargetMonsterClosestToHero.of(), AttackTarget.of())))
                .addRoutine(UpdateHeroRole.of(Hero.Role.JUNGLER));
        return farmMana;
    }
}