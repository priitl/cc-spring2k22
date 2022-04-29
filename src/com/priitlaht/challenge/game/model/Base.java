package com.priitlaht.challenge.game.model;

import com.priitlaht.challenge.game.GameConstants;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Base {
    public static final int VISION_RADIUS = 6000;
    private static final Base MY_BASE_INSTANCE = new Base(true);
    private static final Base OPPONENT_BASE_INSTANCE = new Base(false);

    final boolean isMine;
    Point location;
    int health = 3;
    int mana = 0;
    List<Monster> endangeringMonsters;

    public static Base myBaseInstance() {
        return MY_BASE_INSTANCE;
    }

    public static Base opponentBaseInstance() {
        return OPPONENT_BASE_INSTANCE;
    }

    public boolean isInDanger() {
        return !endangeringMonsters.isEmpty();
    }

    public boolean hasEnoughManaForSpells() {
        return mana >= GameConstants.SPELL_MANA_COST;
    }

    public void location(int x, int y) {
        this.location = Point.of(x, y);
    }

    public void update(int health, int mana) {
        this.health = health;
        this.mana = mana;
    }

    public void useMana(int mana) {
        this.mana -= mana;
    }

    public void updateEndangeringMonsters(List<Monster> visibleMonsters) {
        endangeringMonsters = visibleMonsters.stream()
                .filter(monster -> monster.isTargetingBase(this) && monster.isThreateningBase(this))
                .sorted(Comparator.comparing(monster -> monster.distance(location)))
                .collect(Collectors.toList());
    }
}
