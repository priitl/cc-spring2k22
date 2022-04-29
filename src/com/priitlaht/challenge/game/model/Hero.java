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
    public static final int VISION_RADIUS = 2200;
    public static final int DAMAGE = 2;
    Type type;
    Routine routine;
    Point origin;
    boolean actionPerformed;

    public void playRound() {
        this.actionPerformed = false;
        if (GameState.instance().round() == GameState.Phase.MID.startingRound() && type == Type.HARASSER) {
            this.origin = GameState.instance().myBase().location().subtractAbs(Point.of(13630, 4500));
        }
        if (routine.isIdle()) {
            routine.start();
        }
        routine.play(this.id);
    }

    public boolean isAtOrigin() {
        return Objects.equals(this.location, this.origin);
    }


    public boolean isAssignedOrClosestTo(Monster monster) {
        return Objects.equals(monster.assignedHeroId(), this.id) || (!monster.hasHeroAssigned() && Objects.equals(monster.closestHeroId(), this.id));
    }

    public void control(Entity entity, Point target) {
        this.actionPerformed = true;
        entity.isControlled = true;
        entity.assignedHeroId = this.id; // TODO: should this be here?
        GameState.instance().myBase().useMana(GameConstants.SPELL_MANA_COST);
        System.out.printf("SPELL CONTROL %d %d %d%n", entity.id(), target.x(), target.y());
    }

    public void wind(Point target) {
        this.actionPerformed = true;
        GameState.instance().myBase().useMana(GameConstants.SPELL_MANA_COST);
        System.out.printf("SPELL WIND %d %d%n", target.x(), target.y());
    }

    public void shield(Entity entity) {
        this.actionPerformed = true;
        entity.shieldLife = GameConstants.MAX_SHIELD_LIFE;
        GameState.instance().myBase().useMana(GameConstants.SPELL_MANA_COST);
        System.out.printf("SPELL SHIELD %d%n", entity.id());
    }

    public void attack(Monster monster) {
        this.actionPerformed = true;
        monster.assignedHeroId = this.id;
        System.out.printf("MOVE %d %d%n", monster.nextLocation().x(), monster.nextLocation().y());
    }

    public void moveToOrigin() {
        moveTo(origin);
    }

    public void moveTo(Point target) {
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
