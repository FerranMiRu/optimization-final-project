package main.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import main.java.agents.ours.Agent;
import main.java.engine.core.MarioGame;
import main.java.engine.core.MarioResult;
import main.java.engine.helper.GameStatus;
import main.java.engine.helper.Genes;

public class TrainAgent {

    public static String getLevel(String filepath) {
        String content = "";

        try {
            content = new String(Files.readAllBytes(Paths.get(filepath)));
        } catch (IOException e) {}

        return content;
    }

    public static void main(String[] args) {
        // Parameters
        String levelPath = "./levels/original/lvl-1.txt";

        int totalTicks = 3100; // the maximum time on a level is 200 seconds and inside each second there are 15 ticks, we add a buffer time just in case
        int granularity = 2; // how many ticks we keep doing the same action

        int populationSize = 200;
        // int num_generations = 10000 / populationSize; // we can only evaluate a level 10000 times per championship rules
        int num_generations = 100000;
        double tournamentSizePercentage = 0.15;
        int tournamentSize = Math.max((int) (populationSize * tournamentSizePercentage), 3);
        double crossoverRate = 0.3;
        double mutationRate = 0.01;

        // Initializations
        String level = getLevel(levelPath);
        Random rnd = new Random();
        MarioGame game = new MarioGame();

        Agent[] population = new Agent[populationSize];
        for (int i = 0; i < population.length; i++) {
            population[i] = new Agent(totalTicks, granularity);
            population[i].setRandomChromosome();
        }

        // Genetic Algorithm
        for (int generation = 0; generation < num_generations; generation++) {
            System.out.println(
                "Generation " +
                    generation +
                    " / " +
                    num_generations +
                    " (" +
                    population.length +
                    ")"
            );

            // Evaluation
            for (int i = 0; i < populationSize; i++) {
                MarioResult result = game.runGame(population[i], level, 200, 0, false);
                population[i].setScore(result.getScore());
            }

            // Selection
            Agent[] matingPool = new Agent[populationSize];

            for (int i = 0; i < populationSize; i++) {
                boolean firstScore = true;
                int winnerIndex = -1;
                float maxScore = Float.MIN_VALUE;

                for (int j = 0; j < tournamentSize; j++) {
                    Agent participant = population[rnd.nextInt(populationSize)];

                    if (firstScore || participant.getScore() > maxScore) {
                        maxScore = participant.getScore();
                        winnerIndex = j;
                    }
                }

                matingPool[i] = population[winnerIndex];
            }

            // Crossover
            for (int i = 0; i < populationSize; i += 2) {
                Agent parent1 = matingPool[i];
                Agent parent2 = matingPool[i + 1];

                Agent child1 = new Agent(totalTicks, granularity);
                Agent child2 = new Agent(totalTicks, granularity);

                int crossoverPoint = rnd.nextInt(parent1.getChromosome().length);

                if (rnd.nextDouble() < crossoverRate) {
                    for (int j = 0; j < crossoverPoint; j++) {
                        child1.setChromosome(parent1.getChromosome()[j], j);
                        child2.setChromosome(parent2.getChromosome()[j], j);
                    }
                    for (int j = crossoverPoint; j < parent1.getChromosome().length; j++) {
                        child1.setChromosome(parent2.getChromosome()[j], j);
                        child2.setChromosome(parent1.getChromosome()[j], j);
                    }
                } else {
                    child1.setChromosome(parent1.getChromosome());
                    child2.setChromosome(parent2.getChromosome());
                }

                population[i] = child1;
                population[i + 1] = child2;
            }

            // Mutation
            for (int i = 0; i < populationSize; i++) {
                Agent agent = population[i];

                for (int j = 0; j < agent.getChromosome().length; j++) {
                    if (rnd.nextDouble() < mutationRate) {
                        agent.setChromosome(Genes.getRandomGene(), j);
                    }
                }
            }
        }

        // Last Evaluation
        int averageScore = 0,
            averageDistance = 0,
            amountCompletedRuns = 0;
        float bestDistance = 0;

        Agent bestAgent = null;

        for (int i = 0; i < populationSize; i++) {
            MarioResult result = game.runGame(population[i], level, 200, 0, false);

            population[i].setScore(result.getScore());

            averageScore += result.getScore();
            averageDistance += result.getTotalDistance();
            amountCompletedRuns += result.getGameStatus() == GameStatus.WIN ? 1 : 0;

            if (bestAgent == null || result.getScore() > bestAgent.getScore()) {
                bestAgent = population[i];
                bestDistance = result.getTotalDistance();
            }
        }

        // Print Statistics
        System.out.println("Average Score: " + averageScore / populationSize);
        System.out.println("Average Distance: " + averageDistance / populationSize);
        System.out.println("Amount Completed Runs: " + amountCompletedRuns);

        System.out.println("Best Score: " + bestAgent.getScore());
        System.out.println("Best Distance: " + bestDistance);

        // Save Best Agent
        bestAgent.save(levelPath.split("/")[levelPath.split("/").length - 1]);
    }
}
