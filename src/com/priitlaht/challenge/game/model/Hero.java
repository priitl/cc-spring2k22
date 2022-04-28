package com.priitlaht.challenge.game.model;

import com.priitlaht.challenge.game.GameConstants;
import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.strategy.HarasserStrategy;
import com.priitlaht.challenge.game.strategy.Strategy;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Getter
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Hero extends Entity {
    public static final int DAMAGE_RADIUS = 800;
    public static final int VISION_RADIUS = 2200;
    public static final int DAMAGE = 2;
    Type type;
    Strategy strategy;
    Point origin;
    Base base;
    boolean actionPerformed;

    public void playRound(GameState state) {
        this.actionPerformed = false;
        if (state.round() == GameState.Phase.MID.startingRound() && type == Type.HARASSER) {
            this.strategy = HarasserStrategy.of();
            this.origin = Point.of(state.myBase().isBlueBase() ? 13630 : 4000, 4500);
        }
        strategy.play(this, state);
    }

    public boolean isAssignedOrClosestTo(Monster monster) {
        return Objects.equals(monster.assignedHeroId(), this.id) || (!monster.hasHeroAssigned() && Objects.equals(monster.closestHeroId(), this.id));
    }

    public void control(Entity entity, Point target) {
        this.actionPerformed = true;
        entity.isControlled = true;
        entity.assignedHeroId = this.id; // TODO: should this be here?
        base.useMana(GameConstants.SPELL_MANA_COST);
        System.out.printf("SPELL CONTROL %d %d %d%n", entity.id(), target.x(), target.y());
    }

    public void wind(Point target) {
        this.actionPerformed = true;
        base.useMana(GameConstants.SPELL_MANA_COST);
        System.out.printf("SPELL WIND %d %d%n", target.x(), target.y());
    }

    public void shield(Entity entity) {
        this.actionPerformed = true;
        entity.shieldLife = GameConstants.MAX_SHIELD_LIFE;
        base.useMana(GameConstants.SPELL_MANA_COST);
        System.out.printf("SPELL SHIELD %d%n", entity.id());
    }

    public void attack(Monster monster) {
        this.actionPerformed = true;
        monster.assignedHeroId = this.id;
        System.out.printf("MOVE %d %d%n", monster.nextLocation().x(), monster.nextLocation().y());
    }

    public void move(Point target) {
        this.actionPerformed = true;
        System.out.printf("MOVE %d %d%n", target.x(), target.y());
    }

    public void stayInPlace() {
        this.actionPerformed = true;
        System.out.println("WAIT");
    }

    @RequiredArgsConstructor
    public enum Type {
        HARASSER, DEFENDER, JUNGLER;
    }
}
