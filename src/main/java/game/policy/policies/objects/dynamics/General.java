package game.policy.policies.objects.dynamics;

import core.objects.DynamicElement;
import game.policy.DynamicPolicy;

public class General extends DynamicPolicy {
    @Override
    public boolean enforce(DynamicElement element, DynamicElement element1) {
        // invert elements
        if (element.getManager() != null)
            element.getManager().setMirrored(!element.getManager().isMirrored());
        if (element1.getManager() != null)
            element1.getManager().setMirrored(!element1.getManager().isMirrored());
        element.setSpeedX(-element.getSpeedX());
        element1.setSpeedX(-element1.getSpeedX());
        return false;
    }

    @Override
    public boolean isEnforceable(DynamicElement element, DynamicElement element1) {
        return true;
    }
}
