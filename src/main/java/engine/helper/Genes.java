package main.java.engine.helper;

/**
 * Enum representing all the possible actions that Mario can perform for the algorithm.
 * MarioActions are represented as an array of 5 boolean values that represent the buttons that we
 * are pressing at a given moment. They are in the order of LEFT, RIGHT, DOWN, SPEED, JUMP. Here we
 * implement the 11 composite actions that we can perform in the algorithm for simplicity. Except
 * for the first action, which is simply RUN, we omit the "RUN" part on all the other actions as
 * we are always pressing it.
 */
public enum Genes {
    RUN(new boolean[] { false, false, false, true, false }, "Run", 0.01),
    JUMP(new boolean[] { false, false, false, true, true }, "Jump", 0.07),
    DOWN(new boolean[] { false, false, true, true, false }, "Down", 0.05),
    RIGHT(new boolean[] { false, true, false, true, false }, "Right", 0.325),
    RIGHT_JUMP(new boolean[] { false, true, false, true, true }, "RightJump", 0.325),
    RIGHT_DOWN(new boolean[] { false, true, true, true, false }, "RightDown", 0.05),
    RIGHT_DOWN_JUMP(new boolean[] { false, true, true, true, true }, "RightDownJump", 0.05),
    LEFT(new boolean[] { true, false, false, true, false }, "Left", 0.05),
    LEFT_JUMP(new boolean[] { true, false, false, true, true }, "LeftJump", 0.05),
    LEFT_DOWN(new boolean[] { true, false, true, true, false }, "LeftDown", 0.01),
    LEFT_DOWN_JUMP(new boolean[] { true, false, true, true, true }, "LeftDownJump", 0.01);

    private boolean[] actionArray;
    private String name;
    private double probability;

    Genes(boolean[] newActionArray, String newName, double newProbability) {
        actionArray = newActionArray;
        name = newName;
        probability = newProbability;
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
        double randomValue = Math.random();
        double cumulativeProbability = 0.0;

        for (Genes gene : values()) {
            cumulativeProbability += gene.probability;
            if (randomValue <= cumulativeProbability) {
                return gene;
            }
        }

        // This should never happen, but just in case
        return values()[0];
    }

    public static Genes getGeneByIndex(int index) {
        return values()[index];
    }
}
