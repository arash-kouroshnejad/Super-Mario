package Game.Policy;

import Core.Objects.StaticElement;

public abstract class StaticPolicy extends AbstractPolicy {
    public abstract boolean isEnforceable(StaticElement element);
    public abstract boolean enforce(StaticElement element);
}
