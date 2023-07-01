package game.policy;

import core.objects.DynamicElement;
import core.objects.StaticElement;

public abstract class StaticPolicy extends AbstractPolicy {
    public abstract boolean isEnforceable(StaticElement element, DynamicElement element1);
    public abstract boolean enforce(StaticElement element);
}
