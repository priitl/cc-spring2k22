package com.priitlaht.challenge.game;

import com.priitlaht.challenge.game.model.*;
import com.priitlaht.challenge.game.strategy.DefaultAi;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

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
    final Map<Integer, Hero> heroes = new HashMap<>(3);
    final Map<Integer, Enemy> enemies = new HashMap<>(3);
    final Map<Integer, Monster> monsters = new HashMap<>();
    int round;

    public static GameState instance() {
        return INSTANCE;
    }

    public Hero hero(int heroId) {
        return heroes.get(heroId);
    }

    public void update(Game.RoundInfo roundInfo) {
        round++;
        enemies.clear(); // TODO: remove
        myBase.update(roundInfo.myBaseHealth(), roundInfo.myBaseMana());
        opponentBase.update(roundInfo.opponentBaseHealth(), roundInfo.opponentBaseHealth());
        updateLastRoundMonsters();
        roundInfo.entityInfos().forEach(this::updateEntityState);
        monsters.values().forEach(monster -> monster.updateClosestHeroes(heroes.values()));
    }

    private void updateLastRoundMonsters() {
        List<Monster> monstersToKeepInPlay = monsters.values().stream()
                .filter(monster -> isWithinBoundsAndNotSeen(monster, monster.nextLocation()))
                .map(Monster::moveToNextLocation)
                .collect(Collectors.toList());
        monsters.clear();
        monsters.putAll(monstersToKeepInPlay.stream().collect(Collectors.toMap(Monster::id, monster -> monster)));
    }

    private void updateEntityState(Game.RoundInfo.EntityInfo entity) {
        switch (Game.RoundInfo.EntityInfo.Type.getByValue(entity.type())) {
            case MONSTER:
                addOrUpdateMonsters(entity);
                break;
            case HERO:
                addOrUpdateHero(entity);
                break;
            case ENEMY:
                addOrUpdateEnemy(entity);
                break;
        }
    }

    private void addOrUpdateMonsters(Game.RoundInfo.EntityInfo entity) {
        Monster.MonsterBuilder<?, ?> builder = monsters.containsKey(entity.id()) ? monsters.get(entity.id()).toBuilder() : Monster.builder();
        Monster monster = builder
                .id(entity.id())
                .location(Vector.of(entity.x(), entity.y()))
                .shieldDuration(entity.shieldLife())
                .isControlled(entity.isControlled())
                .health(entity.health())
                .speed(Vector.of(entity.vx(), entity.vy()))
                .target(Monster.Target.getByValue(entity.nearBase()))
                .threat(Monster.Threat.getByValue(entity.threatFor()))
                .build();
        Monster mirroredMonster = monster.mirror();
        monsters.put(entity.id(), monster);
        if (!monsters.containsKey(mirroredMonster.id()) && isWithinBoundsAndNotSeen(mirroredMonster, mirroredMonster.location())) {
            monsters.put(mirroredMonster.id(), mirroredMonster);
        }
    }

    private void addOrUpdateEnemy(Game.RoundInfo.EntityInfo entity) {
        Enemy.EnemyBuilder<?, ?> builder = enemies.containsKey(entity.id()) ? enemies.get(entity.id()).toBuilder() : Enemy.builder();
        enemies.put(entity.id(), builder
                .id(entity.id())
                .location(Vector.of(entity.x(), entity.y()))
                .shieldDuration(entity.shieldLife())
                .isControlled(entity.isControlled())
                .build());
    }

    private void addOrUpdateHero(Game.RoundInfo.EntityInfo entity) {
        Hero.HeroBuilder<?, ?> builder = heroes.containsKey(entity.id())
                ? heroes.get(entity.id()).toBuilder()
                : Hero.builder().routine(DefaultAi.of()).role(heroes.size() == 0 ? Hero.Role.JUNGLER : Hero.Role.DEFENDER);
        heroes.put(entity.id(), builder
                .id(entity.id())
                .location(Vector.of(entity.x(), entity.y()))
                .shieldDuration(entity.shieldLife())
                .isControlled(entity.isControlled())
                .build());
    }

    private boolean isWithinBoundsAndNotSeen(Monster monster, Vector location) {
        return location.withinBounds(0, 0, GameConstants.FIELD_WIDTH, GameConstants.FIELD_HEIGHT)
                && monster.distance(myBase.location()) >= GameConstants.BASE_VISION_RADIUS
                && heroes.values().stream().noneMatch(hero -> hero.distance(location) <= GameConstants.HERO_VISION_RADIUS);
    }
}
