package game.policy.policies.general;

import core.objects.DynamicElement;
import game.policy.DynamicPolicy;

public class SpeedPolicy extends DynamicPolicy {
    @Override
    public boolean enforce(DynamicElement element, DynamicElement element1) {
        element.setSpeedY(Math.max(-policyReference.verticalSpeedLimit, element.getSpeedY()));
        element.setSpeedY(Math.min(policyReference.horizontalSpeedLimit, element.getSpeedY()));
        return false;
    }

    @Override
    public boolean isEnforceable(DynamicElement element, DynamicElement element1) {
        return element.isLockedCharacter();
    }
}
