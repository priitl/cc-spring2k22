package com.priitlaht.challenge.game;

import com.priitlaht.challenge.game.command.Command;
import com.priitlaht.challenge.game.command.MoveCommand;
import com.priitlaht.challenge.game.model.Entity;
import com.priitlaht.challenge.game.model.Monster;
import com.priitlaht.challenge.game.model.Point;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StrategyExecutor {
    public List<Command> execute(GameState state) {
        List<Command> commands = new ArrayList<>();
        state.visibleMonsters().forEach(monster -> {
            int distanceFromBase = monster.nextDistance(state.myBase().location());
            Map<Integer, Integer> distancesToHeroes = state.heroes().values().stream().collect(Collectors.toMap(Entity::id, monster::distance));
        });

        state.heroes().forEach((id, hero) -> {
            Monster monster = state.visibleMonsters().stream().min(Comparator.comparing(m -> m.nextDistance(hero))).orElse(null);
            if (monster == null) {
                commands.add(MoveCommand.of(hero.origin()));
            } else {
                Point target = monster.nextLocation();
                if (!(monster.target() == Monster.Target.BASE && monster.threat() == Monster.Threat.MY_BASE)) {
                    state.visibleMonsters().remove(monster);
                }
                commands.add(MoveCommand.of(target));
            }
        });
        return commands;
    }
}
