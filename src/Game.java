import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
class Game {
    final Base myBase;
    final Base opponentBase;
    final Map<Integer, Hero> heroes = new HashMap<>(3);
    final List<Enemy> visibleEnemies = new ArrayList<>(3);
    final List<Monster> visibleMonsters = new ArrayList<>();
    int round;

    static Game of(int myBaseX, int myBaseY) {
        Point myBaseLocation = Point.of(myBaseX, myBaseY);
        int enemyBaseX = Math.abs(Constants.FIELD_WIDTH - myBaseLocation.x());
        int enemyBaseY = Math.abs(Constants.FIELD_HEIGHT - myBaseLocation.y());
        Point enemyBaseLocation = Point.of(enemyBaseX, enemyBaseY);
        return new Game(Base.of(myBaseLocation), Base.of(enemyBaseLocation));
    }

    void playRound(RoundInfo roundInfo) {
        updateState(roundInfo);
        List<String> commands = new ArrayList<>();
        visibleMonsters.forEach(monster -> {
            int distanceFromBase = monster.nextDistance(myBase.location());
            Map<Integer, Integer> distancesToHeroes = heroes.values().stream().collect(Collectors.toMap(Entity::id, monster::distance));
        });

        heroes.forEach((id, hero) -> {
            Monster monster = visibleMonsters.stream().min(Comparator.comparing(m -> m.nextDistance(hero))).orElse(null);
            if (monster == null) {
                commands.add(String.format("MOVE %d %d", hero.origin().x(), hero.origin().y()));
            } else {
                Point target = monster.nextLocation();
                if (!(monster.target() == Monster.Target.BASE && monster.threat() == Monster.Threat.MY_BASE)) {
                    visibleMonsters.remove(monster);
                }
                commands.add(String.format("MOVE %d %d", target.x(), target.y()));
            }
        });
        commands.forEach(System.out::println);
    }

    void updateState(RoundInfo roundInfo) {
        round++;
        visibleMonsters.clear();
        visibleEnemies.clear();

        myBase.update(roundInfo.myBaseHealth(), roundInfo.myBaseMana());
        opponentBase.update(roundInfo.opponentBaseHealth(), roundInfo.opponentBaseHealth());

        roundInfo.entityInfos().forEach(entity -> {
            switch (RoundInfo.EntityInfo.Type.getByValue(entity.type())) {
                case MONSTER:
                    visibleMonsters.add(Monster.builder()
                            .id(entity.id())
                            .location(Point.of(entity.x(), entity.y()))
                            .shieldLife(entity.shieldLife())
                            .isControlled(entity.isControlled() == 1)
                            .health(entity.health())
                            .velocity(Point.of(entity.vx(), entity.vy()))
                            .target(Monster.Target.getByValue(entity.nearBase()))
                            .threat(Monster.Threat.getByValue(entity.threatFor()))
                            .build());
                    break;
                case HERO:
                    Hero hero = heroes.get(entity.id());
                    if (hero == null) {
                        heroes.put(entity.id(), Hero.builder()
                                .id(entity.id())
                                .origin(myBase.location().subtractAbs(Hero.ORIGINS[heroes.size()]))
                                .location(Point.of(entity.x(), entity.y()))
                                .shieldLife(entity.shieldLife())
                                .isControlled(entity.isControlled() == 1)
                                .build());
                    } else {
                        hero.update(Point.of(entity.x(), entity.y()), entity.shieldLife(), entity.isControlled() == 1);
                    }
                    break;
                case ENEMY:
                    visibleEnemies.add(Enemy.builder()
                            .id(entity.id())
                            .location(Point.of(entity.x(), entity.y()))
                            .shieldLife(entity.shieldLife())
                            .isControlled(entity.isControlled() == 1)
                            .build());
            }
        });
    }
}
