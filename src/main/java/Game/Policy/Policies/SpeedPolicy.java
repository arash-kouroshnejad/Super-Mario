package Game.Policy.Policies;

import Core.Objects.DynamicElement;
import Game.Policy.DynamicPolicy;

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
