package com.priitlaht.challenge.game.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Enemy extends Entity {
    List<Monster> nearbyMonsters;

    public void updateNearbyMonsters(List<Monster> visibleMonsters) {
        nearbyMonsters = visibleMonsters.stream()
                .sorted(Comparator.comparing(monster -> monster.nextDistance(location)))
                .collect(Collectors.toList());
    }
}
