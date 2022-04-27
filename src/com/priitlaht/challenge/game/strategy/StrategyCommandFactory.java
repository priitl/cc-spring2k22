package com.priitlaht.challenge.game.strategy;

import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.command.*;
import com.priitlaht.challenge.game.model.Base;
import com.priitlaht.challenge.game.model.Hero;
import com.priitlaht.challenge.game.model.Monster;
import com.priitlaht.challenge.game.model.Point;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StrategyCommandFactory {
    private static final StrategyCommandFactory INSTANCE = new StrategyCommandFactory();

    public static StrategyCommandFactory getInstance() {
        return INSTANCE;
    }

    public Optional<Command> defendBaseCommand(Hero hero, GameState state) {
        Base myBase = state.myBase();
        if (myBase.isInDanger()) {
            return closestEndangeringMonster(hero, myBase).map(monster -> {
                monster.assignHero(hero.id());
                if (myBase.hasEnoughManaForSpells() && !monster.isShielded()
                        && monster.distance(myBase.location()) <= Monster.BASE_TARGET_RADIUS
                        && monster.distance(hero) <= WindCommand.RADIUS) {
                    return WindCommand.of(state.opponentBase().location());
                }
                return MoveCommand.of(monster.nextLocation());
            });
        }
        return Optional.empty();
    }

    public Optional<Command> windMonsters(Hero hero, GameState state) {
        List<Monster> windableMonsters = state.visibleMonsters().stream()
                .filter(monster -> !monster.isShielded() && state.myBase().mana() > 50 && monster.distance(hero) <= WindCommand.RADIUS)
                .collect(Collectors.toList());
        if (windableMonsters.size() > 3) {
            windableMonsters.forEach(monster -> monster.assignHero(hero.id()));
            return Optional.of(WindCommand.of(state.opponentBase().location()));
        }
        return Optional.empty();
    }

    public Optional<Command> moveToMonster(Hero hero, GameState state) {
        return state.visibleMonsters().stream()
                .filter(monster -> hero.isBestOffensiveFor(monster) || monster.distance(hero.origin()) <= 4000)
                .min(Comparator.comparing(monster -> monster.distance(hero)))
                .map(monster -> {
                    monster.assignHero(hero.id());
                    Point target = monster.nextLocation();
                    return MoveCommand.of(target);
                });
    }

    public Optional<Command> harassEnemyBase(Hero hero, GameState state) {
        //TODO: control enemy heroes and move closer to base if there are multiple monsters
        if (state.round() > 100 && hero.distance(state.opponentBase().location()) < hero.distance(state.myBase().location())) {
            return state.visibleMonsters().stream()
                    .filter(monster -> monster.isControlled() && !monster.isShielded() &&
                            monster.isThreateningBase(state.opponentBase())
                            && state.myBase().mana() > 50 && monster.distance(hero) <= ShieldCommand.RADIUS)
                    .min(Comparator.comparing(m -> m.distance(state.opponentBase().location())))
                    .map(monster -> {
                        monster.assignHero(hero.id());
                        monster.shield();
                        return ShieldCommand.of(monster.id());
                    });
        }
        return Optional.empty();
    }

    public Optional<Command> controlCommand(Hero hero, GameState state) {
        if (state.round() > 100) {
            return state.visibleMonsters().stream()
                    .filter(monster -> (hero.isBestOffensiveFor(monster) || hero.isBestDefensiveFor(monster)) && !monster.isShielded() && !monster.isControlled()
                            && monster.distance(state.myBase().location()) > Monster.BASE_TARGET_RADIUS
                            && monster.threat() != Monster.Threat.OPPONENT_BASE &&
                            state.myBase().mana() > 50 && monster.distance(hero) <= ControlCommand.RADIUS)
                    .min(Comparator.comparing(m -> m.distance(hero)))
                    .map(monster -> {
                        monster.control();
                        return ControlCommand.of(monster.id(), state.opponentBase().location());
                    });
        }
        return Optional.empty();
    }

    private Optional<Monster> closestEndangeringMonster(Hero hero, Base myBase) {
        return myBase.endangeringMonsters().stream()
                .filter(hero::isBestDefensiveFor)
                .min(Comparator.comparing(monster -> monster.distance(hero.location())));
    }
}
