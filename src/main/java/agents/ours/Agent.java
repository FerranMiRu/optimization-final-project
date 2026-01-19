package main.java.agents.ours;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import main.java.engine.core.MarioAgent;
import main.java.engine.core.MarioForwardModel;
import main.java.engine.core.MarioTimer;
import main.java.engine.helper.Genes;

public class Agent implements MarioAgent {

    private int currentGeneIndex = 0,
        totalTicks,
        granularity,
        realTotalTicks = 6800; // with 200 seconds per level, the game really runs at 30 ms per tick so 6,666.6666667 total ticks per level, and some buffer
    private float score;
    private Genes[] chromosome = null,
        expandedChromosome = null;

    public Agent(int totalTicks, int granularity) {
        this.totalTicks = totalTicks;
        this.granularity = granularity;

        this.chromosome = new Genes[totalTicks / granularity];
    }

    @Override
    public void initialize(MarioForwardModel model, MarioTimer timer) {
        // the paper I'm reproducing assumes 15 ticks per second (66.6666666667 ms per tick) and
        // I'm sticking to those values for everything. However, this repository has the game
        // running at 30 ms per tick, so we need to expand the chromosome first given the
        // granularity (which is set in the paper as 5) and then given the real tick rate
        if (this.expandedChromosome == null) expandedChromosome = new Genes[this.realTotalTicks];

        // The plus one at the end is due to using integer division, which rounds down so we need
        // to add one to the result to ensure we cover all ticks.
        int realGranularity = (this.realTotalTicks / this.totalTicks) * this.granularity + 1;

        for (int i = 0; i < this.realTotalTicks; i++) {
            expandedChromosome[i] = this.chromosome[i / realGranularity];
        }

        this.currentGeneIndex = 0;
    }

    @Override
    public boolean[] getActions(MarioForwardModel model, MarioTimer timer) {
        if (currentGeneIndex >= this.expandedChromosome.length) {
            System.out.println("Warning: currentGeneIndex out of bounds");
            currentGeneIndex = 0;
        }

        boolean[] currentAction = this.expandedChromosome[currentGeneIndex].getActionArray();
        currentGeneIndex++;

        return currentAction;
    }

    @Override
    public String getAgentName() {
        return "OurAgent";
    }

    public Genes[] getChromosome() {
        return chromosome;
    }

    public void setChromosome(Genes[] chromosome) {
        this.chromosome = chromosome;
    }

    public void setChromosome(Genes gene, int index) {
        this.chromosome[index] = gene;
    }

    public void setRandomChromosome() {
        int chromosomeLength = totalTicks / granularity;

        for (int i = 0; i < chromosomeLength; i++) {
            this.chromosome[i] = Genes.getRandomGene();
        }
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public int getCurrentGeneIndex() {
        return currentGeneIndex;
    }

    public void save(String filePath) {
        try {
            FileOutputStream fileOut = new FileOutputStream(("./chromosomes/" + filePath));

            // first write a line with the totalticks and granularity
            fileOut.write((totalTicks + "," + granularity).getBytes());
            fileOut.write("\n".getBytes());

            // then write each gene's index
            for (int i = 0; i < chromosome.length; i++) {
                fileOut.write((chromosome[i].getGeneIndex() + ",").getBytes());
            }
            fileOut.write("\n".getBytes());

            fileOut.close();
            System.out.println("Saved agent to " + filePath);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public static Agent load(String filePath) {
        try {
            FileInputStream fileIn = new FileInputStream(("./chromosomes/" + filePath));
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileIn));

            String line = reader.readLine();
            String[] parts = line.split(",");
            int totalTicks = Integer.parseInt(parts[0]);
            int granularity = Integer.parseInt(parts[1]);

            Agent agent = new Agent(totalTicks, granularity);

            line = reader.readLine();
            String[] genes = line.split(",");
            for (int i = 0; i < genes.length; i++) {
                agent.setChromosome(Genes.getGeneByIndex(Integer.parseInt(genes[i])), i);
            }

            reader.close();
            return agent;
        } catch (IOException i) {
            i.printStackTrace();
        }
        return null;
    }
}
