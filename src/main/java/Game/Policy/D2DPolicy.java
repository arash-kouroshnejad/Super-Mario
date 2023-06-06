package Game.Policy;

import Core.Objects.DynamicElement;

public abstract class D2DPolicy extends AbstractPolicy{
    public abstract boolean enforce(DynamicElement element1, DynamicElement element2);
    public abstract boolean isEnforceable(DynamicElement element1, DynamicElement element2);
}
