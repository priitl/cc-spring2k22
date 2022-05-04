package com.priitlaht.challenge.game;

import com.priitlaht.challenge.game.model.*;
import com.priitlaht.challenge.game.strategy.DefaultAi;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    final List<Entity> visbleEntities = new ArrayList<>();
    final Map<Integer, Hero> heroes = new HashMap<>(3);
    int round;

    public static GameState instance() {
        return INSTANCE;
    }

    public Hero hero(int heroId) {
        return heroes.get(heroId);
    }

    public void update(Game.RoundInfo roundInfo) {
        Map<Integer, Integer> previousRoundMonsterAssignments = visibleMonsters.stream()
                .filter(Monster::hasHeroAssigned)
                .collect(Collectors.toMap(Monster::id, Monster::assignedHeroId));
        List<Monster> previousRoundMonsters = new ArrayList<>(visibleMonsters);
        round++;
        visibleMonsters.clear();
        visibleEnemies.clear();
        visbleEntities.clear();
        myBase.update(roundInfo.myBaseHealth(), roundInfo.myBaseMana());
        opponentBase.update(roundInfo.opponentBaseHealth(), roundInfo.opponentBaseHealth());
        roundInfo.entityInfos().forEach(entity ->
                updateEntityState(entity, previousRoundMonsterAssignments.get(entity.id()), previousRoundMonsters));
        visibleMonsters.forEach(monster -> monster.updateClosestHeroes(heroes.values()));
        visbleEntities.addAll(visibleMonsters);
        visbleEntities.addAll(visibleEnemies);
    }

    // TODO keep previous monsters that are not dead and visible enemy heroes
    private void updateEntityState(Game.RoundInfo.EntityInfo entity, Integer assignedHeroId, List<Monster> previousRoundMonsters) {
        switch (Game.RoundInfo.EntityInfo.Type.getByValue(entity.type())) {
            case MONSTER:
                Monster monster = toMonster(entity, assignedHeroId);
                visibleMonsters.remove(monster);
                visibleMonsters.add(monster);
                Monster mirroredMonster = monster.mirror();
                if (!previousRoundMonsters.contains(monster) && !visibleMonsters.contains(mirroredMonster)) {
                    visibleMonsters.add(mirroredMonster);
                }
                break;
            case HERO:
                if (round == 1) {
                    heroes.put(entity.id(), toHero(entity));
                } else {
                    heroes.get(entity.id()).update(Vector.of(entity.x(), entity.y()), entity.shieldLife(), entity.isControlled());
                }
                break;
            case ENEMY:
                visibleEnemies.add(toEnemy(entity));
        }
    }

    private Monster toMonster(Game.RoundInfo.EntityInfo entity, Integer assignedHeroId) {
        return Monster.builder()
                .id(entity.id())
                .position(Vector.of(entity.x(), entity.y()))
                .shieldDuration(entity.shieldLife())
                .isControlled(entity.isControlled())
                .health(entity.health())
                .velocity(Vector.of(entity.vx(), entity.vy()))
                .target(Monster.Target.getByValue(entity.nearBase()))
                .threat(Monster.Threat.getByValue(entity.threatFor()))
                .assignedHeroId(assignedHeroId)
                .build();
    }

    private Enemy toEnemy(Game.RoundInfo.EntityInfo entity) {
        return Enemy.builder()
                .id(entity.id())
                .position(Vector.of(entity.x(), entity.y()))
                .shieldDuration(entity.shieldLife())
                .isControlled(entity.isControlled())
                .build();
    }

    private Hero toHero(Game.RoundInfo.EntityInfo entity) {
        return Hero.builder()
                .id(entity.id())
                .position(Vector.of(entity.x(), entity.y()))
                .shieldDuration(entity.shieldLife())
                .role(Hero.Role.JUNGLER)
                .isControlled(entity.isControlled())
                .routine(DefaultAi.of())
                .build();
    }
}
