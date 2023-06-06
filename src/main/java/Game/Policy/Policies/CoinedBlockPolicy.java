package Game.Policy.Policies;

import Core.Objects.DynamicElement;
import Core.Objects.StaticElement;
import Game.Policy.D2SPolicy;

public class CoinedBlockPolicy extends D2SPolicy {
    @Override
    public boolean enforce(DynamicElement element1, StaticElement element2) {
        return false;
    }

    @Override
    public boolean isEnforceable(DynamicElement element1, StaticElement element2) {
        return false;
    }
}
