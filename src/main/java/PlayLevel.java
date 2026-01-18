package main.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import main.java.agents.ours.Agent;
import main.java.engine.core.MarioGame;
import main.java.engine.core.MarioResult;

public class PlayLevel {

    public static void printResults(MarioResult result) {
        System.out.println("****************************************************************");
        System.out.println(
            "Game Status: " +
                result.getGameStatus().toString() +
                " (Remaining Time: " +
                result.getRemainingTime() / 1000 +
                ")"
        );
        System.out.println("Final Score: " + result.getScore());
        System.out.println("Distance: " + result.getTotalDistance());
        System.out.println(
            "Collected Items: " +
                result.getNumCollectedFireflower() +
                " Fireflower, " +
                result.getNumCollectedMushrooms() +
                " Mushrooms, " +
                result.getNumCollectedLifeMushrooms() +
                " Life Mushrooms"
        );
        System.out.println(
            "Kills: " +
                result.getKillsTotal() +
                " (" +
                result.getKillsByFire() +
                " by Fireflower, " +
                result.getKillsByShell() +
                " by Shell, " +
                result.getKillsByStomp() +
                " by Stomp)"
        );
        System.out.println(
            "Miscellaneous: " +
                result.getMarioMode() +
                " final mode, " +
                result.getCurrentCoins() +
                " coins collected"
        );
        System.out.println("****************************************************************");
    }

    public static String getLevel(String filepath) {
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get(filepath)));
        } catch (IOException e) {}
        return content;
    }

    public static void main(String[] args) {
        MarioGame game = new MarioGame();

        Agent agent = Agent.load("lvl-1.txt");
        // Agent agent = new Agent(3100, 5);
        // agent.setRandomChromosome();

        printResults(
            game.runGame(agent, getLevel("./levels/original/lvl-1.txt"), 200, 0, true, 33, 3)
        );

        // agent.save("test.txt");
    }
}
