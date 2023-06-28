package game.policy.policies.Objects;

import core.objects.DynamicElement;
import core.objects.StaticElement;
import game.policy.D2SPolicy;

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
