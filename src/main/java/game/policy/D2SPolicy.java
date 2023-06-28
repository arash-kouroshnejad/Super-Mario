package game.policy;

import core.objects.DynamicElement;
import core.objects.StaticElement;

public abstract class  D2SPolicy extends AbstractPolicy{
    public abstract boolean enforce(DynamicElement element1, StaticElement element2);
    public abstract boolean isEnforceable(DynamicElement element1, StaticElement element2);
}
