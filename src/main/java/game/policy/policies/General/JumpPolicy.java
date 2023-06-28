package game.policy.policies.General;

import core.objects.DynamicElement;
import game.policy.DynamicPolicy;

public class JumpPolicy extends DynamicPolicy {
    @Override
    public boolean enforce(DynamicElement element) {
        if (!(policyReference.onGround && policyReference.jumping) && Math.abs(element.getY() - policyReference.minY) <= 5) {
            element.setSpeedY(Math.max(0, element.getSpeedY()));
        }
        return false;
    }

    @Override
    public boolean isEnforceable(DynamicElement element) {
        return element.isLockedCharacter();
    }
}
