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
@RequiredArgsConstructor(staticName = "of")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Base {
    public static final int VISION_RADIUS = 6000;
    final Point location;
    final boolean isMine;
    int health = 3;
    int mana = 0;
    List<Monster> endangeringMonsters;

    public boolean isInDanger() {
        return !endangeringMonsters.isEmpty();
    }

    public boolean hasEnoughManaForSpells() {
        return mana >= GameConstants.SPELL_MANA_COST;
    }

    public void update(int health, int mana) {
        this.health = health;
        this.mana = mana;
    }

    public void castSpell() {
        this.mana = this.mana - GameConstants.SPELL_MANA_COST;
    }

    public void updateEndangeringMonsters(List<Monster> visibleMonsters) {
        endangeringMonsters = visibleMonsters.stream()
                .filter(monster -> monster.isTargetingBase(this) && monster.isThreateningBase(this))
                .sorted(Comparator.comparing(monster -> monster.nextDistance(location)))
                .collect(Collectors.toList());
    }


}
