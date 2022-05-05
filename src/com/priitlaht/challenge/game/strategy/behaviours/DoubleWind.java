package com.priitlaht.challenge.game.strategy.behaviours;

import com.priitlaht.challenge.game.strategy.engine.Routine;
import com.priitlaht.challenge.game.strategy.engine.Sequence;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DoubleWind extends Sequence {
    public static Routine of() {
        DoubleWind doubleWind = new DoubleWind();
        return doubleWind;
    }
}
