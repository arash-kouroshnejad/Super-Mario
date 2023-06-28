package game.policy;

import control.GameManager;
import game.MarioLogic;
import game.plugins.SoundQueue;

public abstract class AbstractPolicy {
    protected final GameManager manager = GameManager.getInstance();
    protected final MarioLogic gameLogic = manager.getGameLogic();
    protected final SoundQueue soundSystem = gameLogic.getSoundSystem();
    protected final PolicyReference policyReference = PolicyReference.getInstance();
}
