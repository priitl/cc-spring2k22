package com.priitlaht.challenge.game.strategy;

import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.command.*;
import com.priitlaht.challenge.game.model.Base;
import com.priitlaht.challenge.game.model.Hero;
import com.priitlaht.challenge.game.model.Monster;
import com.priitlaht.challenge.game.model.Point;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class DefaultStrategy implements Strategy {

    // Make defenders stay in place
    @Override
    public void play(GameState gameState) {
        Base myBase = gameState.myBase();
        List<Command> commands = gameState.heroes().stream()
                .map(hero -> {
                    Hero otherDefender = gameState.heroes().stream()
                            .filter(other -> other.id() != hero.id() && other.type() != Hero.Type.HARASSER)
                            .findFirst().orElse(hero);
                    if (myBase.isInDanger() && Hero.Type.HARASSER != hero.type()) {
                        Optional<Command> command = myBase.endangeringMonsters().stream()
                                .filter(monster -> monster.hasNoHeroAssignedOrAssignedTo(hero.id()))
                                .min(Comparator.comparing(monster -> monster.distance(hero.location())))
                                .map(monster -> {
                                    if (myBase.hasEnoughManaForSpells() && monster.distance(myBase.location()) <= 5000
                                            && !monster.isShielded()
                                            && monster.distance(hero) <= WindCommand.RADIUS) {
                                        myBase.castSpell();
                                        monster.assignHero(hero.id());
                                        return WindCommand.of(gameState.opponentBase().location());
                                    }
                                    if (otherDefender.distance(monster) > hero.distance(monster)) {
                                        return MoveCommand.of(monster.nextLocation());
                                    }
                                    return null;
                                });
                        if (command.isPresent()) {
                            return command.get();
                        }
                    }

                    //TODO: figure out why it does not work
                    List<Monster> windableMonsters = gameState.visibleMonsters().stream()
                            .filter(monster -> !monster.isShielded()
                                    && myBase.mana() > 50 && monster.nextDistance(hero) <= WindCommand.RADIUS).collect(Collectors.toList());
                    if (windableMonsters.size() > 2) {
                        windableMonsters.forEach(monster -> monster.assignHero(hero.id()));
                        myBase.castSpell();
                        return WindCommand.of(gameState.opponentBase().location());
                    }

                    //TODO: control enemy heroes and move closer to base if there are multiple monsters
                    if (gameState.round() > 100 && Hero.Type.HARASSER == hero.type() && hero.distance(gameState.opponentBase().location()) < hero.distance(gameState.myBase().location())) {
                        Optional<Command> command = gameState.visibleMonsters().stream()
                                .filter(monster -> monster.isControlled() && !monster.isShielded() &&
                                        monster.isThreateningBase(gameState.opponentBase())
                                        && myBase.mana() > 50 && monster.distance(hero) <= ShieldCommand.RADIUS)
                                .min(Comparator.comparing(m -> m.distance(gameState.opponentBase().location())))
                                .map(monster -> {
                                    monster.assignHero(hero.id());
                                    monster.shield();
                                    return ShieldCommand.of(monster.id());
                                });
                        if (command.isPresent()) {
                            myBase.castSpell();
                            return command.get();
                        }
                    }
                    if (gameState.round() > 100) {
                        Optional<Command> command = gameState.visibleMonsters().stream()
                                .filter(monster -> monster.hasNoHeroAssignedOrAssignedTo(hero.id()) && !monster.isShielded() && !monster.isControlled()
                                        && monster.distance(myBase.location()) > Monster.BASE_TARGET_RADIUS
                                        && monster.threat() != Monster.Threat.OPPONENT_BASE &&
                                        myBase.mana() > 50 && monster.distance(hero) <= ControlCommand.RADIUS)
                                .min(Comparator.comparing(m -> m.distance(hero)))
                                .map(monster -> {
                                    myBase.castSpell();
                                    monster.control();
                                    return ControlCommand.of(monster.id(), gameState.opponentBase().location());
                                });
                        if (command.isPresent()) {
                            return command.get();
                        }
                    }
                    Optional<Command> command = gameState.visibleMonsters().stream()
                            .filter(monster -> monster.hasNoHeroAssignedOrAssignedTo(hero.id()) &&
                                    (Hero.Type.DEFENDER != hero.type() || monster.nextDistance(hero.origin()) <= 4000))
                            .min(Comparator.comparing(monster -> monster.distance(hero)))
                            .map(monster -> {
                                monster.assignHero(hero.id());
                                Point target = monster.nextLocation();
                                return MoveCommand.of(target);
                            });
                    return command.orElseGet(() -> MoveCommand.of(hero.origin()));
                })
                .collect(Collectors.toList());
        commands.forEach(Command::execute);
    }
}
