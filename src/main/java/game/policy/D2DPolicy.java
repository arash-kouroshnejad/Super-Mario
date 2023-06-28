package game.policy;

import core.objects.DynamicElement;

public abstract class D2DPolicy extends AbstractPolicy{
    public abstract boolean enforce(DynamicElement element1, DynamicElement element2);
    public abstract boolean isEnforceable(DynamicElement element1, DynamicElement element2);
}
