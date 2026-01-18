package agents.doNothing;

import main.java.engine.core.MarioAgent;
import main.java.engine.core.MarioForwardModel;
import main.java.engine.core.MarioTimer;
import main.java.engine.helper.MarioActions;

public class Agent implements MarioAgent {

    @Override
    public void initialize(MarioForwardModel model, MarioTimer timer) {}

    @Override
    public boolean[] getActions(MarioForwardModel model, MarioTimer timer) {
        return new boolean[MarioActions.numberOfActions()];
    }

    @Override
    public String getAgentName() {
        return "DoNothingAgent";
    }
}
