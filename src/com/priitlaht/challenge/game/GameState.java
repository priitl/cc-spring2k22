package com.priitlaht.challenge.game;

import com.priitlaht.challenge.game.model.*;
import com.priitlaht.challenge.game.strategy.DefaultAi;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GameState {
    private static final GameState INSTANCE = new GameState();

    final Base myBase = Base.myBaseInstance();
    final Base opponentBase = Base.opponentBaseInstance();
    final List<Enemy> visibleEnemies = new ArrayList<>(3);
    final List<Monster> visibleMonsters = new ArrayList<>();
    final Map<Integer, Hero> heroes = new HashMap<>(3);
    Phase phase;
    int round;

    public static GameState instance() {
        return INSTANCE;
    }

    public Hero hero(int heroId) {
        return heroes.get(heroId);
    }

    public Point heroLocation(int heroId) {
        return heroes.get(heroId).location();
    }

    public void update(Game.RoundInfo roundInfo) {
        Map<Integer, Integer> previousRoundMonsterAssignments = visibleMonsters.stream()
                .filter(Monster::hasHeroAssigned)
                .collect(Collectors.toMap(Monster::id, Monster::assignedHeroId));
        round++;
        phase = round < Phase.MID.startingRound ? Phase.START : round < Phase.LATE.startingRound ? Phase.MID : Phase.LATE;
        visibleMonsters.clear();
        visibleEnemies.clear();
        myBase.update(roundInfo.myBaseHealth(), roundInfo.myBaseMana());
        opponentBase.update(roundInfo.opponentBaseHealth(), roundInfo.opponentBaseHealth());
        roundInfo.entityInfos().forEach(entity -> updateEntityState(entity, previousRoundMonsterAssignments.get(entity.id())));
        myBase.updateEndangeringMonsters(visibleMonsters);
        opponentBase.updateEndangeringMonsters(visibleMonsters);
        visibleMonsters.forEach(monster -> monster.updateClosestHeroes(heroes.values()));
    }

    private void updateEntityState(Game.RoundInfo.EntityInfo entity, Integer assignedHeroId) {
        switch (Game.RoundInfo.EntityInfo.Type.getByValue(entity.type())) {
            case MONSTER:
                visibleMonsters.add(toMonster(entity, assignedHeroId));
                break;
            case HERO:
                if (round == 1) {
                    heroes.put(entity.id(), toHero(entity));
                } else {
                    heroes.get(entity.id()).update(Point.of(entity.x(), entity.y()), entity.shieldLife(), entity.isControlled());
                }
                break;
            case ENEMY:
                visibleEnemies.add(toEnemy(entity));
        }
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
        Point myBase = this.myBase.location();
        Hero.HeroBuilder<?, ?> heroBuilder = Hero.builder()
                .id(entity.id())
                .location(Point.of(entity.x(), entity.y()))
                .shieldLife(entity.shieldLife())
                .isControlled(entity.isControlled());
        switch (heroes().size()) {
            case 0:
                heroBuilder
                        .type(Hero.Type.HARASSER)
                        .routine(DefaultAi.of())
                        .origin(myBase.subtractAbs(Point.of(8815, 5300)));
                break;
            case 1:
                heroBuilder
                        .type(Hero.Type.DEFENDER)
                        .routine(DefaultAi.of())
                        .origin(myBase.subtractAbs(Point.of(7200, 2200)));
                break;
            case 2:
                heroBuilder
                        .type(Hero.Type.DEFENDER)
                        .routine(DefaultAi.of())
                        .origin(myBase.subtractAbs(Point.of(4000, 7800)));
                break;
        }
        return heroBuilder.build();
    }

    @Getter
    @RequiredArgsConstructor
    public enum Phase {
        START(0), MID(95), LATE(150);
        final int startingRound;
    }
}
