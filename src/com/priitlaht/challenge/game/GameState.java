package com.priitlaht.challenge.game;

import com.priitlaht.challenge.game.model.*;
import com.priitlaht.challenge.game.strategy.DefenderStrategy;
import com.priitlaht.challenge.game.strategy.HarasserStrategy;
import com.priitlaht.challenge.game.strategy.JunglerStrategy;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GameState {
    final Base myBase;
    final Base opponentBase;
    final List<Hero> heroes = new ArrayList<>(3);
    final List<Enemy> visibleEnemies = new ArrayList<>(3);
    final List<Monster> visibleMonsters = new ArrayList<>();
    final Map<Integer, Integer> heroAssignments = new HashMap<>();
    int round;

    public void update(Game.RoundInfo roundInfo) {
        Map<Integer, Integer> previousRoundMonsterAssignments = visibleMonsters.stream()
                .filter(Monster::hasHeroAssigned)
                .collect(Collectors.toMap(Monster::id, Monster::assignedHeroId));
        round++;
        visibleMonsters.clear();
        visibleEnemies.clear();
        myBase.update(roundInfo.myBaseHealth(), roundInfo.myBaseMana());
        opponentBase.update(roundInfo.opponentBaseHealth(), roundInfo.opponentBaseHealth());
        roundInfo.entityInfos().forEach(entity -> updateEntityState(entity, previousRoundMonsterAssignments.get(entity.id())));
        myBase.updateEndangeringMonsters(visibleMonsters);
        opponentBase.updateEndangeringMonsters(visibleMonsters);
        visibleMonsters.forEach(monster -> monster.updateClosestHeroes(heroes));
    }

    private void updateEntityState(Game.RoundInfo.EntityInfo entity, Integer assignedHeroId) {
        switch (Game.RoundInfo.EntityInfo.Type.getByValue(entity.type())) {
            case MONSTER:
                visibleMonsters.add(toMonster(entity, assignedHeroId));
                break;
            case HERO:
                if (round == 1) {
                    heroes.add(toHero(entity));
                } else {
                    heroes.stream().filter(hero -> hero.id() == entity.id()).findFirst().ifPresent(hero ->
                            hero.update(Point.of(entity.x(), entity.y()), entity.shieldLife(), entity.isControlled()));
                }
                break;
            case ENEMY:
                visibleEnemies.add(toEnemy(entity));
        }
    }

    public Hero heroByType(Hero.Type type) {
        return heroes.stream().filter(hero -> hero.type() == type).findFirst().orElse(heroes.get(0));
    }

    private Monster toMonster(Game.RoundInfo.EntityInfo entity, Integer assignedHeroId) {
        return Monster.builder()
                .id(entity.id())
                .location(Point.of(entity.x(), entity.y()))
                .shieldLife(entity.shieldLife())
                .isControlled(entity.isControlled())
                .health(entity.health())
                .velocity(Point.of(entity.vx(), entity.vy()))
                .target(Monster.Target.getByValue(entity.nearBase()))
                .threat(Monster.Threat.getByValue(entity.threatFor()))
                .assignedHeroId(assignedHeroId)
                .build();
    }

    private Enemy toEnemy(Game.RoundInfo.EntityInfo entity) {
        return Enemy.builder()
                .id(entity.id())
                .location(Point.of(entity.x(), entity.y()))
                .shieldLife(entity.shieldLife())
                .isControlled(entity.isControlled())
                .build();
    }

    private Hero toHero(Game.RoundInfo.EntityInfo entity) {
        Hero.HeroBuilder<?, ?> heroBuilder = Hero.builder()
                .id(entity.id())
                .location(Point.of(entity.x(), entity.y()))
                .shieldLife(entity.shieldLife())
                .isControlled(entity.isControlled());
        switch (heroes().size()) {
            case 0:
                heroBuilder.type(Hero.Type.HARASSER).strategy(HarasserStrategy.of());
                break;
            case 1:
                heroBuilder.type(Hero.Type.DEFENDER).strategy(DefenderStrategy.of());
                break;
            case 2:
                heroBuilder.type(Hero.Type.JUNGLER).strategy(JunglerStrategy.of());
                break;
        }
        return heroBuilder.build();
    }
}
