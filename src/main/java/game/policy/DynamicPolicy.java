package game.policy;

import core.objects.DynamicElement;

public abstract class  DynamicPolicy extends AbstractPolicy {
    public abstract boolean enforce(DynamicElement element, DynamicElement element1);
    public abstract boolean isEnforceable(DynamicElement element, DynamicElement element1);
}
