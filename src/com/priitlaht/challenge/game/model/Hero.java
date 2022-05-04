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
import java.util.Optional;

@Getter
@SuperBuilder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Hero extends Entity {
    Role role;
    Routine routine;
    String action;
    String message;
    Entity target;
    Vector guardPosition;

    public void resolveAction() {
        this.action = null;
        this.message = null;
        if (routine.status() == null) {
            routine.start();
        }
        routine.play(this.id);
    }

    public void playAction() {
        System.out.printf("%s %s%n", action, Optional.ofNullable(message).orElse(role.name()));
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

    public void updateGuardPosition(Vector guardPosition) {
        this.guardPosition = guardPosition;
    }

    public void updateMessage(String message) {
        this.message = message;
    }

    public boolean isAssignedOrClosestTo(Entity entity) {
        return Objects.equals(entity.assignedHeroId(), this.id) || (!entity.hasHeroAssigned() && Objects.equals(entity.closestHeroId(), this.id));
    }

    public void control(Entity entity, Vector target) {
        entity.isControlled = true;
        entity.assignedHeroId = this.id;
        GameState.instance().myBase().useMana(GameConstants.SPELL_MANA_COST);
        this.action = String.format("SPELL CONTROL %d %d %d", entity.id(), Math.round(target.x()), Math.round(target.y()));
    }

    public void wind(Entity entity, Vector target) {
        entity.assignedHeroId = this.id;
        this.wind(target);
    }

    public void wind(Vector target) {
        GameState.instance().myBase().useMana(GameConstants.SPELL_MANA_COST);
        this.action = String.format("SPELL WIND %d %d", Math.round(target.x()), Math.round(target.y()));
    }

    public void shield(Entity entity) {
        entity.shieldDuration = GameConstants.SPELL_SHIELD_DURATION;
        GameState.instance().myBase().useMana(GameConstants.SPELL_MANA_COST);
        this.action = String.format("SPELL SHIELD %d", entity.id());
    }

    public void moveTo(Vector target) {
        this.action = String.format("MOVE %d %d", Math.round(target.x()), Math.round(target.y()));
    }

    public void stayInPlace() {
        this.action = "WAIT";
    }

    public void intercept(Entity entity, Vector interceptLocation) {
        entity.assignedHeroId = this.id;
        this.action = String.format("MOVE %d %d", Math.round(interceptLocation.x()), Math.round(interceptLocation.y()));
    }

    @RequiredArgsConstructor
    public enum Role {
        HARASSER, DEFENDER, JUNGLER;
    }
}
