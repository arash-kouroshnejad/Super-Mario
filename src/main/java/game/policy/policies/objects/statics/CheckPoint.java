package game.policy.policies.objects.statics;

import core.objects.DynamicElement;
import core.objects.StaticElement;
import game.policy.D2SPolicy;

public class CheckPoint extends D2SPolicy {
    @Override
    public boolean enforce(DynamicElement element1, StaticElement element2) {
        if (element1.isLockedCharacter())
            policyReference.saveReady = true;
        return false;
    }

    @Override
    public boolean isEnforceable(DynamicElement element1, StaticElement element2) {
        return element2.getType().equals("FlagPole");
    }
}
