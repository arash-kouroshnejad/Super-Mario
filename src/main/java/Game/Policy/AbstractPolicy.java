package Game.Policy;

import Control.GameManager;
import Game.MarioLogic;
import Game.Plugins.SoundQueue;
import Game.Policy.PolicyReference;

public abstract class AbstractPolicy {
    protected final GameManager manager = GameManager.getInstance();
    protected final MarioLogic gameLogic = manager.getGameLogic();
    protected final SoundQueue soundSystem = gameLogic.getSoundSystem();
    protected final PolicyReference policyReference = PolicyReference.getInstance();
}
