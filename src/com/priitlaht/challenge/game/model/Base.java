package com.priitlaht.challenge.game.model;

import com.priitlaht.challenge.game.GameConstants;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Base {
    private static final Base MY_BASE_INSTANCE = new Base(true);
    private static final Base OPPONENT_BASE_INSTANCE = new Base(false);

    final boolean isMine;
    Vector location;
    int health = GameConstants.BASE_INITIAL_HEALTH;
    int mana = GameConstants.BASE_INITIAL_MANA;

    public static Base myBaseInstance() {
        return MY_BASE_INSTANCE;
    }

    public static Base opponentBaseInstance() {
        return OPPONENT_BASE_INSTANCE;
    }

    public boolean hasEnoughManaForSpells() {
        return mana >= GameConstants.SPELL_MANA_COST;
    }

    public void location(int x, int y) {
        this.location = Vector.of(x, y);
    }

    public void update(int health, int mana) {
        this.health = health;
        this.mana = mana;
    }

    public void useMana(int mana) {
        this.mana -= mana;
    }
}
