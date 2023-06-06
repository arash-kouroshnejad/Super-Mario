package Game.Policy;

import Core.Objects.DynamicElement;
import Core.Objects.StaticElement;

public abstract class  D2SPolicy extends AbstractPolicy{
    public abstract boolean enforce(DynamicElement element1, StaticElement element2);
    public abstract boolean isEnforceable(DynamicElement element1, StaticElement element2);
}
