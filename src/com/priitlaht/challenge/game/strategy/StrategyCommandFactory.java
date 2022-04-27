package com.priitlaht.challenge.game.strategy;

import com.priitlaht.challenge.game.GameConstants;
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
                int distanceFromHero = monster.distance(hero);
                int distanceFromBase = monster.distance(myBase.location());
                boolean canKillBeforeBase = distanceFromHero <= Hero.DAMAGE_RADIUS && distanceFromBase > (monster.health() - Hero.DAMAGE) * Monster.DISTANCE_PER_TURN;
                if (!canKillBeforeBase && myBase.hasEnoughManaForSpells() && !monster.isShielded() && monster.isTargetingBase(state.myBase())
                        && distanceFromBase <= Monster.BASE_TARGET_RADIUS - WindCommand.FORCE
                        && distanceFromHero <= WindCommand.RADIUS) {
                    return WindCommand.of(state.opponentBase().location());
                }
                return MoveCommand.of(monster.nextLocation());
            });
        }
        return Optional.empty();
    }

    public Optional<Command> shieldClosestHero(Hero hero, GameState state) {
        if (state.phase() != GameState.Phase.START) {
            return state.heroes().stream().filter(other -> !other.isShielded() && state.myBase().mana() > 20 && other.distance(hero) <= ShieldCommand.RADIUS
                            && state.visibleEnemies().stream().anyMatch(enemy -> enemy.distance(other) <= ControlCommand.RADIUS + Hero.DAMAGE_RADIUS))
                    .min(Comparator.comparing(other -> other.distance(hero)))
                    .map(other -> {
                        other.shield();
                        return ShieldCommand.of(other.id());
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
                .filter(monster -> hero.isAssignedOrClosestTo(monster) && (Hero.Type.DEFENDER != hero.type() || monster.distance(state.myBase().location()) <= 9850))
                .min(Comparator.comparing(monster -> monster.distance(hero)))
                .map(monster -> {
                    monster.assignHero(hero.id());
                    Point target = monster.nextLocation();
                    return MoveCommand.of(target);
                });
    }

    public Optional<Command> harassEnemyBase(Hero hero, GameState state) {
        //TODO: control enemy heroes and move closer to base if there are multiple monsters
        if (state.phase() != GameState.Phase.START && hero.distance(state.opponentBase().location()) < Base.VISION_RADIUS * 1.3) {
            List<Monster> windableMonsters = state.visibleMonsters().stream()
                    .filter(monster -> monster.isShielded() && monster.isThreateningBase(state.opponentBase())
                            && state.myBase().mana() > 20 && monster.distance(hero) <= WindCommand.RADIUS
                            && monster.distance(state.opponentBase().location()) <= WindCommand.FORCE + Monster.DISTANCE_PER_TURN)
                    .collect(Collectors.toList());
            if (!windableMonsters.isEmpty()) {
                windableMonsters.forEach(monster -> monster.assignHero(hero.id()));
                return Optional.of(WindCommand.of(state.opponentBase().location()));
            }
            Optional<Command> shieldCommand = state.visibleMonsters().stream()
                    .filter(monster -> !monster.isShielded() && monster.isTargetingBase(state.opponentBase())
                            && monster.distance(state.opponentBase().location()) <= Monster.DISTANCE_PER_TURN * monster.health()
                            && state.myBase().mana() > 20 && monster.distance(hero) <= ShieldCommand.RADIUS)
                    .min(Comparator.comparing(monster -> monster.distance(state.opponentBase().location())))
                    .map(monster -> {
                        monster.assignHero(hero.id());
                        monster.shield();
                        return ShieldCommand.of(monster.id());
                    });
            if (shieldCommand.isPresent()) {
                return shieldCommand;
            } else {
                return pushEnemyAwayFromMonsters(hero, state);
            }
        }
        return Optional.empty();
    }

    public Optional<Command> pushEnemyAwayFromMonsters(Hero hero, GameState state) {
        return state.visibleEnemies().stream()
                .filter(enemy -> !enemy.isShielded() && state.myBase().mana() > 50 && enemy.distance(hero) <= ControlCommand.RADIUS &&
                        state.visibleMonsters().stream().anyMatch(monster -> monster.distance(enemy) <= Hero.DAMAGE_RADIUS))
                .min(Comparator.comparing(enemy -> enemy.distance(hero)))
                .map(enemy -> {
                    Point target = Point.of(GameConstants.FIELD_WIDTH / 2, state.myBase().location().y());
                    if (enemy.distance(hero) <= WindCommand.RADIUS) {
                        return WindCommand.of(target);
                    }
                    enemy.control();
                    return ControlCommand.of(enemy.id(), target);
                });
    }

    public Optional<Command> controlMonsterCommand(Hero hero, GameState state) {
        if (state.phase() != GameState.Phase.START) {
            return state.visibleMonsters().stream()
                    .filter(monster -> hero.isAssignedOrClosestTo(monster) && !monster.isShielded() && !monster.isControlled()
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

    public Optional<Command> getRidOfEnemyCommand(Hero hero, GameState state) {
        if (state.phase() != GameState.Phase.START) {
            return state.visibleEnemies().stream()
                    .filter(enemy -> !enemy.isShielded() && state.myBase().mana() > 50 && enemy.distance(hero) <= ControlCommand.RADIUS)
                    .min(Comparator.comparing(enemy -> enemy.distance(hero)))
                    .map(enemy -> {
                        Point target = Point.of(GameConstants.FIELD_WIDTH / 2, state.myBase().location().y());
                        if (enemy.distance(hero) <= WindCommand.RADIUS) {
                            return WindCommand.of(target);
                        }
                        enemy.control();
                        return ControlCommand.of(enemy.id(), target);
                    });
        }
        return Optional.empty();
    }

    public Command moveToRandomPositionNearOrigin(Hero hero) {
        int xOffset = (int) (Math.random() * Hero.VISION_RADIUS);
        int yOffset = (int) (Math.random() * Hero.VISION_RADIUS);
        Point target = Point.of(Math.abs(hero.origin().x() - xOffset), Math.abs(hero.origin().y() - yOffset));
        return MoveCommand.of(target);
    }

    private Optional<Monster> closestEndangeringMonster(Hero hero, Base myBase) {
        return myBase.endangeringMonsters().stream()
                .filter(hero::isAssignedOrClosestTo)
                .min(Comparator.comparing(monster -> monster.distance(myBase.location())));
    }
}
