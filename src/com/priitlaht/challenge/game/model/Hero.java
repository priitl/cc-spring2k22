package com.priitlaht.challenge.game.model;

import com.priitlaht.challenge.game.GameConstants;
import com.priitlaht.challenge.game.GameState;
import com.priitlaht.challenge.game.strategy.engine.Routine;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Objects;
import java.util.Optional;

@Getter
@SuperBuilder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class Hero extends Entity {
    Role role;
    Routine routine;
    String action;
    String message;
    Entity target;
    Vector guardPosition;
    Vector patrolDestination;
    boolean patrolReversed;

    public void resolveAction() {
        this.action = null;
        this.target = null;
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

    public void updateMessage(String message) {
        this.message = message;
    }

    public void updateGuardPosition(Vector guardPosition) {
        this.guardPosition = guardPosition;
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

    public void patrolBetween(Vector[] positions) {
        if (this.patrolDestination == null) {
            this.patrolDestination = positions[0];
        }
        if (this.location.equals(patrolDestination)) {
            for (int i = 0; i < positions.length; i++) {
                if (this.location.equals(positions[i])) {
                    if (this.patrolReversed) {
                        this.patrolReversed = !(i == 1);
                        this.patrolDestination = positions[i - 1];
                        System.err.println("Reversed: " + patrolDestination.x());
                    } else {
                        this.patrolReversed = i == positions.length - 2;
                        this.patrolDestination = positions[i + 1];
                        System.err.println("Regular: " + patrolDestination.x());
                    }
                    break;
                }
            }
        }
        moveTo(this.patrolDestination);
    }

    public void moveTo(Vector target) {
        this.location = this.location.stepTo(target, GameConstants.HERO_DISTANCE_PER_TURN);
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
        INTERCEPTOR, DEFENDER, HARASSER
    }
}
