package game.policy.policies.objects.statics;


import core.objects.DynamicElement;
import core.objects.StaticElement;
import game.policy.D2SPolicy;

public class Flag extends D2SPolicy {
    @Override
    public boolean enforce(DynamicElement element1, StaticElement element2) {
        policyReference.marioLogic.calculateScore();
        manager.saveProgress(); // TODO : test save/load extensively
        policyReference.currentGame.setLevel(policyReference.currentGame.getLevel() + 1);
        policyReference.marioLogic.reset();
        return true;
    }

    @Override
    public boolean isEnforceable(DynamicElement element1, StaticElement element2) {
        return element1.isLockedCharacter() && element2.getType().equals("FlagPole");
    }
}
