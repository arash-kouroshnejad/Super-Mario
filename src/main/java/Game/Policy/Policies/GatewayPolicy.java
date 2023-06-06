package Game.Policy.Policies;

import Core.Objects.DynamicElement;
import Core.Objects.StaticElement;
import Game.Policy.D2SPolicy;

public class GatewayPolicy extends D2SPolicy {
    @Override
    public boolean enforce(DynamicElement element1, StaticElement element2) {
        if (!element1.collidesHorizontally(element2)) {
            if (policyReference.currentGame.getLevel() == 0)
                policyReference.currentGame.setLevel(2);
            else
                policyReference.currentGame.setLevel(0);
            // manager.saveProgress(); // TODO : save to a temp var :(
            gameLogic.reset();
            return true;
        }
        return false;
    }

    @Override
    public boolean isEnforceable(DynamicElement element1, StaticElement element2) {
        return element1.isLockedCharacter() && element1.getType().equals("Gateway");
    }
}
