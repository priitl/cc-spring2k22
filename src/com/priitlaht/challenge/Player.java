package com.priitlaht.challenge;

import com.priitlaht.challenge.game.Game;

import java.util.Scanner;
import java.util.stream.IntStream;

public class Player {
    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Game game = Game.of(in.nextInt(), in.nextInt());
        in.nextInt(); // ignore heroes count, as it is always 3
        while (true) {
            Game.RoundInfo.RoundInfoBuilder roundInfoBuilder = Game.RoundInfo.builder()
                    .myBaseHealth(in.nextInt())
                    .myBaseMana(in.nextInt())
                    .opponentBaseHealth(in.nextInt())
                    .opponentBaseMana(in.nextInt());
            IntStream.range(0, in.nextInt()).mapToObj(i -> Game.RoundInfo.EntityInfo.builder()
                    .id(in.nextInt())
                    .type(in.nextInt())
                    .x(in.nextInt())
                    .y(in.nextInt())
                    .shieldLife(in.nextInt())
                    .isControlled(in.nextInt() == 1)
                    .health(in.nextInt())
                    .vx(in.nextInt())
                    .vy(in.nextInt())
                    .nearBase(in.nextInt())
                    .threatFor(in.nextInt())
                    .build()).forEach(roundInfoBuilder::addEntity);
            game.playRound(roundInfoBuilder.build());
        }
    }
}