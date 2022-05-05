package com.priitlaht.challenge.game.strategy.helpers;

import com.priitlaht.challenge.game.GameConstants;
import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.model.Base;
import com.priitlaht.challenge.game.model.Hero;
import com.priitlaht.challenge.game.model.Monster;
import com.priitlaht.challenge.game.strategy.AiContext;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

@NoArgsConstructor(staticName = "of")
public class TargetMonsterClosestToHeroThatCanBeSentToEnemyBase extends Routine {
    @Override
    public void play(int heroId) {
        Collection<Monster> visibleMonsters = GameState.instance().monsters().values();
        Hero hero = GameState.instance().hero(heroId);
        Base opponentBase = GameState.instance().opponentBase();
        Optional<Monster> target = visibleMonsters.stream()
                .filter(monster -> !monster.isShielded() && !monster.isControlled() && !monster.isThreateningBase(opponentBase)
                        && monster.distance(hero) <= GameConstants.SPELL_CONTROL_RADIUS
                        && monster.distance(opponentBase.location()) < AiContext.NEAR_BASE_THRESHOLD * 1.3)
                .min(Comparator.comparing(monster -> monster.distance(hero)));
        if (target.isPresent()) {
            hero.updateTarget(target.get());
            succeed();
        } else {
            fail();
        }
    }
}
