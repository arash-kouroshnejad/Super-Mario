package game.policy;

import core.objects.StaticElement;

public abstract class StaticPolicy extends AbstractPolicy {
    public abstract boolean isEnforceable(StaticElement element);
    public abstract boolean enforce(StaticElement element);
}
