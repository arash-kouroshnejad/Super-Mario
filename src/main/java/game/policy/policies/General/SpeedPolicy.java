package game.policy.policies.General;

import core.objects.DynamicElement;
import game.policy.DynamicPolicy;

public class SpeedPolicy extends DynamicPolicy {
    @Override
    public boolean enforce(DynamicElement element) {
        element.setSpeedY(Math.max(-policyReference.verticalSpeedLimit, element.getSpeedY()));
        element.setSpeedY(Math.min(policyReference.horizontalSpeedLimit, element.getSpeedY()));
        return false;
    }

    @Override
    public boolean isEnforceable(DynamicElement element) {
        return element.isLockedCharacter();
    }
}
