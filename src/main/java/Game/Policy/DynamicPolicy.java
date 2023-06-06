package Game.Policy;

import Core.Objects.DynamicElement;

public abstract class  DynamicPolicy extends AbstractPolicy {
    public abstract boolean enforce(DynamicElement element);
    public abstract boolean isEnforceable(DynamicElement element);
}
