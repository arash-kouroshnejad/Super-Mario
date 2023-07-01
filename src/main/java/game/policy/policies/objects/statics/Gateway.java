package game.policy.policies.objects.statics;

import core.objects.DynamicElement;
import core.objects.StaticElement;
import game.policy.D2SPolicy;

public class Gateway extends D2SPolicy {
    @Override
    public boolean enforce(DynamicElement element1, StaticElement element2) {
        if (!element1.collidesHorizontally(element2) && policyReference.crouching) {
            if (policyReference.currentGame.getLevel() == 0)
                policyReference.currentGame.setLevel(2);
            else
                policyReference.currentGame.setLevel(0);
            // manager.saveProgress(); // TODO : save to a temp var :(
            policyReference.marioLogic.reset();
            return true;
        }
        return false;
    }

    @Override
    public boolean isEnforceable(DynamicElement element1, StaticElement element2) {
        return element1.isLockedCharacter() && element1.getType().equals("Gateway");
    }
}
