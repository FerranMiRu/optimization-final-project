package main.java.engine.helper;

import java.util.Random;

/**
 * Enum representing all the possible actions that Mario can perform for the algorithm.
 * MarioActions are represented as an array of 5 boolean values that represent the buttons that we
 * are pressing at a given moment. They are in the order of LEFT, RIGHT, DOWN, SPEED, JUMP. Here we
 * implement the 11 composite actions that we can perform in the algorithm for simplicity. Except
 * for the first action, which is simply RUN, we omit the "RUN" part on all the other actions as
 * we are always pressing it.
 */
public enum Genes {
    RUN(new boolean[] { false, false, false, true, false }, "Run"),
    JUMP(new boolean[] { false, false, false, true, true }, "Jump"),
    DOWN(new boolean[] { false, false, true, true, false }, "Down"),
    RIGHT(new boolean[] { false, true, false, true, false }, "Right"),
    RIGHT_JUMP(new boolean[] { false, true, false, true, true }, "RightJump"),
    RIGHT_DOWN(new boolean[] { false, true, true, true, false }, "RightDown"),
    RIGHT_DOWN_JUMP(new boolean[] { false, true, true, true, true }, "RightDownJump"),
    LEFT(new boolean[] { true, false, false, true, false }, "Left"),
    LEFT_JUMP(new boolean[] { true, false, false, true, true }, "LeftJump"),
    LEFT_DOWN(new boolean[] { true, false, true, true, false }, "LeftDown"),
    LEFT_DOWN_JUMP(new boolean[] { true, false, true, true, true }, "LeftDownJump");

    private boolean[] actionArray;
    private String name;

    Genes(boolean[] newActionArray, String newName) {
        actionArray = newActionArray;
        name = newName;
    }

    public boolean[] getActionArray() {
        return actionArray;
    }

    public String getName() {
        return name;
    }

    public int getGeneIndex() {
        return ordinal();
    }

    public static int numberOfActions() {
        return MarioActions.values().length;
    }

    public static Genes getRandomGene() {
        return values()[new Random().nextInt(values().length)];
    }

    public static Genes getGeneByIndex(int index) {
        return values()[index];
    }
}
