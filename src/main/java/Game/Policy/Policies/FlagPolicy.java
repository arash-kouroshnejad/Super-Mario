package Game.Policy.Policies;


import Core.Objects.DynamicElement;
import Core.Objects.StaticElement;
import Game.Policy.D2SPolicy;

public class FlagPolicy extends D2SPolicy {
    @Override
    public boolean enforce(DynamicElement element1, StaticElement element2) {
        gameLogic.calculateScore();
        manager.saveProgress(); // TODO : test save/load extensively
        policyReference.currentGame.setLevel(policyReference.currentGame.getLevel() + 1);
        gameLogic.reset();
        return true;
    }

    @Override
    public boolean isEnforceable(DynamicElement element1, StaticElement element2) {
        return element1.isLockedCharacter() && element2.getType().equals("FlagPole");
    }
}
