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
                int shieldDistance = Monster.DISTANCE_PER_TURN * monster.shieldLife();
                if (shieldDistance > distanceFromBase) {
                    return null;
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
                .filter(monster -> !monster.isShielded() && state.myBase().mana() > 20 && monster.distance(hero) <= WindCommand.RADIUS)
                .collect(Collectors.toList());
        if (windableMonsters.size() > 3) {
            windableMonsters.forEach(monster -> monster.assignHero(hero.id()));
            return Optional.of(WindCommand.of(state.opponentBase().location()));
        }
        return Optional.empty();
    }

    public Optional<Command> moveToMonster(Hero hero, GameState state) {
        List<Monster> monsters = state.visibleMonsters().stream()
                .filter(monster -> hero.isAssignedOrClosestTo(monster) && (Hero.Type.DEFENDER != hero.type() || monster.distance(state.myBase().location()) <= 9850))
                .collect(Collectors.toList());
        if (monsters.isEmpty()) {
            return Optional.empty();
        }
        if (monsters.size() == 1) {
            monsters.get(0).assignHero(hero.id());
            return Optional.of(MoveCommand.of(monsters.get(0).nextLocation()));
        }
        Point centerOfMass = Point.centerOfMass(monsters.stream().map(Monster::nextLocation).collect(Collectors.toList()));
        boolean hasMonsterInDamageRadius = monsters.stream().anyMatch(monster -> monster.nextLocation().distance(centerOfMass) <= Hero.DAMAGE_RADIUS);
        if (hasMonsterInDamageRadius) {
            return Optional.ofNullable(MoveCommand.of(centerOfMass));
        }
        return monsters.stream().min(Comparator.comparing(monster -> monster.distance(hero)))
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
                    .filter(monster -> !monster.isShielded() && monster.isTargetingBase(state.opponentBase())
                            && state.myBase().mana() > 20 && monster.distance(hero) <= WindCommand.RADIUS
                            && monster.distance(state.opponentBase().location()) <= Monster.DISTANCE_PER_TURN * monster.health())
                    .collect(Collectors.toList());
            if (windableMonsters.size() > 2) {
                windableMonsters.forEach(monster -> monster.assignHero(hero.id()));
                return Optional.of(WindCommand.of(state.opponentBase().location()));
            }
            Optional<Command> shieldCommand = state.visibleMonsters().stream()
                    .filter(monster -> !monster.isShielded() && monster.isThreateningBase(state.opponentBase())
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
                .filter(enemy -> !enemy.isShielded() && state.myBase().mana() > 20 && enemy.distance(hero) <= ControlCommand.RADIUS &&
                        state.visibleMonsters().stream().anyMatch(monster -> monster.distance(enemy) <= Hero.DAMAGE_RADIUS))
                .min(Comparator.comparing(enemy -> enemy.distance(hero)))
                .map(enemy -> {
                    Point target = Point.of(GameConstants.FIELD_WIDTH / 2, state.myBase().location().y());
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

    public Optional<Command> stayAwayFromMonsters(Hero hero, GameState state) {
        if (state.phase() != GameState.Phase.START) {
            List<Point> monsterNextLocations = state.visibleMonsters().stream()
                    .filter(monster -> monster.isTargetingBase(state.opponentBase()) && hero.distance(monster) < Hero.DAMAGE_RADIUS)
                    .map(Monster::nextLocation)
                    .collect(Collectors.toList());
            if (monsterNextLocations.isEmpty()) {
                return Optional.empty();
            }
            Point centerOfMass = Point.centerOfMass(monsterNextLocations);
            Point target = state.opponentBase().location().subtractAbs(centerOfMass.add(Point.of(Hero.DAMAGE_RADIUS + 1, Hero.DAMAGE_RADIUS + 1)));
            return Optional.of(MoveCommand.of(target));
        }
        return Optional.empty();
    }

    public Command moveToRandomPositionNearOrigin(Hero hero, GameState state) {
        boolean blueBase = state.myBase().isBlueBase();
        int xOffset = (int) (Math.random() * Hero.VISION_RADIUS);
        Point target = Point.of(hero.origin().x() + (blueBase ? xOffset : -xOffset), hero.origin().y());
        return MoveCommand.of(target);
    }

    private Optional<Monster> closestEndangeringMonster(Hero hero, Base myBase) {
        return myBase.endangeringMonsters().stream()
                .filter(monster -> hero.isAssignedOrClosestTo(monster) || myBase.endangeringMonsters().size() > 3)
                .min(Comparator.comparing(monster -> monster.distance(myBase.location())));
    }
}
