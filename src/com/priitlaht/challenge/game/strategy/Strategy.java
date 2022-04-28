package com.priitlaht.challenge.game.strategy;

import com.priitlaht.challenge.game.GameConstants;
import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.model.Base;
import com.priitlaht.challenge.game.model.Hero;
import com.priitlaht.challenge.game.model.Monster;
import com.priitlaht.challenge.game.model.Point;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class Strategy {

    public abstract void play(Hero hero, GameState state);

    public void defendBaseCommand(Hero hero, GameState state) {
        Base myBase = state.myBase();
        if (myBase.isInDanger()) {
            closestEndangeringMonster(hero, myBase).ifPresent(monster -> {
                int distanceFromHero = monster.distance(hero);
                int distanceFromBase = monster.distance(myBase.location());
                boolean canKillBeforeBase = distanceFromHero <= Hero.DAMAGE_RADIUS && distanceFromBase > (monster.health() - Hero.DAMAGE) * Monster.DISTANCE_PER_TURN;
                if (!canKillBeforeBase && myBase.hasEnoughManaForSpells() && monster.isUnshielded() && monster.isTargetingBase(state.myBase())
                        && distanceFromBase <= Monster.BASE_TARGET_RADIUS - GameConstants.WIND_FORCE
                        && distanceFromHero <= GameConstants.WIND_RADIUS) {
                    hero.wind(state.opponentBase().location());
                    return;
                }
                int shieldDistance = Monster.DISTANCE_PER_TURN * monster.shieldLife();
                if (shieldDistance > distanceFromBase) {
                    return;
                }
                hero.attack(monster);
            });
        } else {
            boolean hasEnemiesNearBase = state.visibleEnemies().stream().anyMatch(enemy -> enemy.distance(myBase.location()) < Base.VISION_RADIUS * 1.1);
            if (!hasEnemiesNearBase) {
                return;
            }
            state.visibleMonsters().stream()
                    .filter(monster -> monster.isThreateningBase(state.myBase()) && hero.isAssignedOrClosestTo(monster))
                    .min(Comparator.comparing(monster -> monster.distance(hero.location())))
                    .ifPresent(monster -> {
                        if (myBase.hasEnoughManaForSpells() && monster.isUnshielded() && monster.distance(hero) <= GameConstants.WIND_RADIUS) {
                            hero.wind(state.opponentBase().location());
                            return;
                        }
                        hero.attack(monster);
                    });
        }
    }

    public void shieldClosestHero(Hero hero, GameState state) {
        if (state.phase() != GameState.Phase.START) {
            state.heroes().stream().filter(other -> other.isUnshielded() && state.myBase().mana() > 20 && other.distance(hero) <= GameConstants.SHIELD_RADIUS
                            && state.visibleEnemies().stream().anyMatch(enemy -> enemy.distance(other) <= GameConstants.CONTROL_RADIUS + Hero.DAMAGE_RADIUS)
                            && closestEndangeringMonster(hero, state.myBase()).filter(monster -> monster.distance(state.myBase().location()) > Hero.DAMAGE_RADIUS).isPresent())
                    .min(Comparator.comparing(other -> other.distance(hero)))
                    .ifPresent(hero::shield);
        }
    }

    public void windMonsters(Hero hero, GameState state) {
        List<Monster> windableMonsters = state.visibleMonsters().stream()
                .filter(monster -> monster.isUnshielded() && state.myBase().mana() > 20 && monster.distance(hero) <= GameConstants.WIND_RADIUS)
                .collect(Collectors.toList());
        if (windableMonsters.size() > 3) {
            hero.wind(state.opponentBase().location());
        }
    }

    public void moveToMonster(Hero hero, GameState state) {
        List<Monster> monsters = state.visibleMonsters().stream()
                .filter(monster -> hero.isAssignedOrClosestTo(monster) && (Hero.Type.DEFENDER != hero.type() || monster.distance(state.myBase().location()) <= 9850))
                .collect(Collectors.toList());
        if (monsters.isEmpty()) {
            return;
        }
        if (monsters.size() == 1) {
            hero.attack(monsters.get(0));
            return;
        }
        Point centerOfMass = Point.centerOfMass(monsters.stream().map(Monster::nextLocation).collect(Collectors.toList()));
        boolean hasMonsterInDamageRadius = monsters.stream().anyMatch(monster -> monster.nextLocation().distance(centerOfMass) <= Hero.DAMAGE_RADIUS);
        if (hasMonsterInDamageRadius) {
            hero.move(centerOfMass);
            return;
        }
        monsters.stream().min(Comparator.comparing(monster -> monster.distance(hero))).ifPresent(hero::attack);
    }

    public void harassEnemyBase(Hero hero, GameState state) {
        if (state.phase() != GameState.Phase.START && hero.distance(state.opponentBase().location()) < Base.VISION_RADIUS * 1.3) {
            List<Monster> windableMonsters = state.visibleMonsters().stream()
                    .filter(monster -> monster.isUnshielded() && monster.isThreateningBase(state.opponentBase())
                            && state.myBase().mana() > 20 && monster.distance(hero) <= GameConstants.WIND_RADIUS
                            && monster.distance(state.opponentBase().location()) <= Monster.DISTANCE_PER_TURN * monster.health() + GameConstants.WIND_RADIUS)
                    .collect(Collectors.toList());
            if (windableMonsters.size() > 2) {
                hero.wind(state.opponentBase().location());
                return;
            }
            state.visibleMonsters().stream()
                    .filter(monster -> monster.isUnshielded() && monster.isThreateningBase(state.opponentBase())
                            && monster.health() >= 12
                            && monster.distance(state.opponentBase().location()) <= Monster.DISTANCE_PER_TURN * monster.health() / 2
                            && state.myBase().mana() > 20 && monster.distance(hero) <= GameConstants.SHIELD_RADIUS)
                    .min(Comparator.comparing(monster -> monster.distance(state.opponentBase().location())))
                    .ifPresent(hero::shield);
            if (!hero.actionPerformed()) {
                pushEnemyAwayFromMonsters(hero, state);
            }
        }
    }

    public void pushEnemyAwayFromMonsters(Hero hero, GameState state) {
        state.visibleEnemies().stream()
                .filter(enemy -> enemy.isUnshielded() && state.myBase().mana() > 20 && enemy.distance(hero) <= GameConstants.CONTROL_RADIUS &&
                        state.visibleMonsters().stream().anyMatch(monster -> monster.distance(enemy) <= Hero.DAMAGE_RADIUS))
                .min(Comparator.comparing(enemy -> enemy.distance(hero)))
                .ifPresent(enemy -> {
                    Point target = Point.of(GameConstants.FIELD_WIDTH / 2, state.myBase().location().y());
                    hero.control(enemy, target);
                });
    }

    public void controlMonsterCommand(Hero hero, GameState state) {
        if (state.phase() != GameState.Phase.START) {
            state.visibleMonsters().stream()
                    .filter(monster -> hero.isAssignedOrClosestTo(monster) && monster.isUnshielded() && !monster.isControlled()
                            && monster.distance(state.myBase().location()) > Monster.BASE_TARGET_RADIUS
                            && monster.threat() != Monster.Threat.OPPONENT_BASE &&
                            state.myBase().mana() > 50 && monster.distance(hero) <= GameConstants.CONTROL_RADIUS)
                    .min(Comparator.comparing(m -> m.distance(hero)))
                    .ifPresent(monster -> hero.control(monster, state.opponentBase().location()));
        }
    }

    public void getRidOfEnemyCommand(Hero hero, GameState state) {
        if (GameState.Phase.MID == state.phase()) {
            state.visibleEnemies().stream()
                    .filter(enemy -> enemy.isUnshielded() && !enemy.isControlled() && enemy.distance(state.myBase().location()) <= Base.VISION_RADIUS * 1.2)
                    .min(Comparator.comparing(enemy -> enemy.distance(hero)))
                    .ifPresent(enemy -> {
                        if (enemy.distance(hero) > GameConstants.CONTROL_RADIUS) {
                            hero.move(enemy.location());
                            return;
                        }
                        if (!state.myBase().hasEnoughManaForSpells()) {
                            return;
                        }
                        Optional<Point> closestMonsterVelocity = state.visibleMonsters().stream()
                                .min(Comparator.comparing(m -> m.distance(enemy)))
                                .map(Monster::velocity);
                        Point target = closestMonsterVelocity.isPresent() ? enemy.location().subtractAbs(closestMonsterVelocity.get()) : Point.of(GameConstants.FIELD_WIDTH / 2, state.myBase().location().y());
                        if (enemy.distance(hero) <= GameConstants.WIND_RADIUS) {
                            hero.wind(target);
                            return;
                        }
                        hero.control(enemy, target);
                    });
        }
    }

    // TODO: resolve mana issues, improve directing monsters to base + avoid contact with monsters
    public void score(Hero hero, GameState state) {
        Point opponentBase = state.opponentBase().location();
        if (hero.distance(opponentBase) < hero.distance(state.myBase().location())) {
            List<Monster> potentialTargets = state.opponentBase().endangeringMonsters().stream()
                    .filter(monster -> monster.health() >= 12 && monster.isUnshielded() && monster.distance(opponentBase) < Base.VISION_RADIUS * 1.15).collect(Collectors.toList());
            if (potentialTargets.isEmpty()) {
                return;
            }
            Optional<Monster> closestMonsterToHero = potentialTargets.stream().min(Comparator.comparing(m -> m.distance(hero)));
            int closestMonsterToHeroDistance = closestMonsterToHero.map(m -> m.distance(hero)).orElse(0);
            if (closestMonsterToHeroDistance > GameConstants.SHIELD_RADIUS) {
                hero.attack(closestMonsterToHero.get());
                return;
            }
            Optional<Monster> closestMonsterToBase = potentialTargets.stream().min(Comparator.comparing(m -> m.distance(opponentBase)));
            int closestMonsterToBaseDistanceToHero = closestMonsterToBase.map(m -> m.distance(hero)).orElse(0);
            if (closestMonsterToBaseDistanceToHero < GameConstants.WIND_RADIUS && state.myBase().hasEnoughManaForSpells() && potentialTargets.size() > 2) {
                hero.wind(opponentBase);
                return;
            }
            if (closestMonsterToBaseDistanceToHero < GameConstants.SHIELD_RADIUS && closestMonsterToBase.get().distance(opponentBase) < 3000 && state.myBase().hasEnoughManaForSpells()) {
                hero.shield(closestMonsterToBase.get());
                return;
            }
            if (closestMonsterToHeroDistance < GameConstants.WIND_RADIUS && state.myBase().hasEnoughManaForSpells() && potentialTargets.size() > 2) {
                hero.wind(opponentBase);
            } else {
                hero.attack(closestMonsterToHero.get());
            }
        }
    }

    public void stayAwayFromMonsters(Hero hero, GameState state) {
        if (state.phase() != GameState.Phase.START) {
            state.visibleMonsters().stream()
                    .filter(monster -> monster.isThreateningBase(state.opponentBase())
                            && hero.distance(monster) < Hero.DAMAGE_RADIUS
                            && hero.distance(state.opponentBase().location()) < Base.VISION_RADIUS * 1.1)
                    .min(Comparator.comparing(m -> m.distance(hero)))
                    .ifPresent(m -> hero.move(hero.location().subtractAbs(m.velocity())));
        }
    }

    public void moveToRandomPositionNearOrigin(Hero hero, GameState state) {
        boolean blueBase = state.myBase().isBlueBase();
        int xOffset = (int) (Math.random() * Hero.VISION_RADIUS);
        Point target = Point.of(hero.origin().x() + (blueBase ? xOffset : -xOffset), hero.origin().y());
        hero.move(target);
    }

    private Optional<Monster> closestEndangeringMonster(Hero hero, Base myBase) {
        return myBase.endangeringMonsters().stream()
                .filter(monster -> hero.isAssignedOrClosestTo(monster) || myBase.endangeringMonsters().size() > 3)
                .min(Comparator.comparing(monster -> monster.distance(myBase.location())));
    }
}
