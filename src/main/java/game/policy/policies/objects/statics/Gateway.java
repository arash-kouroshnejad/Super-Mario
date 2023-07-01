package game.policy.policies.objects.statics;

import core.objects.DynamicElement;
import core.objects.StaticElement;
import game.policy.D2SPolicy;

public class Gateway extends D2SPolicy {
    @Override
    public boolean enforce(DynamicElement element1, StaticElement element2) {
        if (!element1.collidesHorizontally(element2)) {
            if (policyReference.crouching && element1.isLockedCharacter()) {
                if (policyReference.currentGame.getLevel() == 0)
                    policyReference.currentGame.setLevel(2);
                else
                    policyReference.currentGame.setLevel(0);
                // manager.saveProgress(); // TODO : save to a temp var :(
                policyReference.marioLogic.reset();
                return true;
            }
            else {
                if (element1.isLockedCharacter())
                    policyReference.onGround = true;
                element1.setSpeedY(Math.min(0, element1.getSpeedY()));
            }
        }
        else {
            // horizontal collision
            if (element2.getX() >= element1.getX()) {
                element1.setSpeedX(Math.min(0, element1.getSpeedX()));
                element1.setX(element1.getX() - 10);
            } // d -> s
            else {
                element1.setSpeedX((Math.max(0, element1.getSpeedX())));
                element1.setX(element1.getX() + 10);
            } // s <- d
        }
        return false;
    }

    @Override
    public boolean isEnforceable(DynamicElement element1, StaticElement element2) {
        return element2.getType().equals("Gateway");
    }
}
