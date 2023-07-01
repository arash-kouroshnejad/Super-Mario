package game.policy;

import control.GameManager;
import game.BowserLogic;
import game.MarioLogic;
import game.plugins.SoundQueue;

public abstract class AbstractPolicy {
    protected static final GameManager manager = GameManager.getInstance();
    protected static final PolicyReference policyReference = PolicyReference.getInstance();
}
