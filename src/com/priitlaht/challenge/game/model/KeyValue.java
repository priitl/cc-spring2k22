package com.priitlaht.challenge.game.model;

import lombok.Value;

@Value(staticConstructor = "of")
public class KeyValue<KEY, VALUE> {
    KEY key;
    VALUE value;
}
