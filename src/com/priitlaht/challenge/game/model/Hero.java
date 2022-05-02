package com.priitlaht.challenge.game.model;

import com.priitlaht.challenge.game.GameConstants;
import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.strategy.engine.Routine;
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
    public static final int DISTANCE_PER_TURN = 800;
    public static final int VISION_RADIUS = 2200;
    public static final int DAMAGE = 2;
    Role role;
    Routine routine;
    Point origin;
    String action;
    Entity target;

    public void playRound() {
        this.action = null;
        if (GameState.instance().round() == GameState.Phase.MID.startingRound() && role == Role.HARASSER) {
            this.origin = GameState.instance().myBase().location().subtractAbs(Point.of(13630, 4500));
        }
        if (routine.status() == null) {
            routine.start();
        }
        routine.play(this.id);
    }

    public double distanceToTarget() {
        return this.distance(target);
    }

    public void updateTarget(Entity target) {
        this.target = target;
    }

    public void updateRole(Role role) {
        this.role = role;
    }

    public boolean isAtOrigin() {
        return Objects.equals(this.location, this.origin);
    }

    public boolean isAssignedOrClosestTo(Monster monster) {
        return Objects.equals(monster.assignedHeroId(), this.id) || (!monster.hasHeroAssigned() && Objects.equals(monster.closestHeroId(), this.id));
    }

    public void control(Entity entity, Point target) {
        entity.isControlled = true;
        entity.assignedHeroId = this.id;
        GameState.instance().myBase().useMana(GameConstants.SPELL_MANA_COST);
        this.action = String.format("SPELL CONTROL %d %d %d", entity.id(), Math.round(target.x()), Math.round(target.y()));
    }

    public void wind(Entity entity, Point target) {
        entity.assignedHeroId = this.id;
        this.wind(target);
    }

    public void wind(Point target) {
        GameState.instance().myBase().useMana(GameConstants.SPELL_MANA_COST);
        this.action = String.format("SPELL WIND %d %d", Math.round(target.x()), Math.round(target.y()));
    }

    public void shield(Entity entity) {
        entity.shieldLife = GameConstants.MAX_SHIELD_LIFE;
        GameState.instance().myBase().useMana(GameConstants.SPELL_MANA_COST);
        this.action = String.format("SPELL SHIELD %d", entity.id());
    }

    public void attack(Entity entity) {
        entity.assignedHeroId = this.id;
        this.action = String.format("MOVE %d %d", Math.round(entity.nextLocation().x()), Math.round(entity.nextLocation().y()));
    }

    public void moveToOrigin() {
        moveTo(origin);
    }

    public void moveTo(Point target) {
        this.action = String.format("MOVE %d %d", Math.round(target.x()), Math.round(target.y()));
    }

    public void stayInPlace() {
        this.action = "WAIT";
    }

    @RequiredArgsConstructor
    public enum Role {
        HARASSER, DEFENDER, JUNGLER;
    }
}
