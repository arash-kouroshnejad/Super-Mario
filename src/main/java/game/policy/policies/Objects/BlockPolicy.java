package game.policy.policies.Objects;

import core.objects.DynamicElement;
import core.objects.StaticElement;
import game.policy.D2SPolicy;


public class BlockPolicy extends D2SPolicy {
    @Override
    public boolean enforce(DynamicElement element1, StaticElement element2) {
        if (element1.collidesHorizontally(element2)) {
            checkHorizontal(element1, element2);
        }
        else {
            // vertical collision
            if (Math.abs(element1.getBounds().TOP - element1.getBounds().BOTTOM) < 10) {
                headCollision(element1);
                customBlockBehaviour(element1);
            } // s > d
            else {
                bottomCollision(element1);
            } // s < d
        }
        return false;
    }

    @Override
    public boolean isEnforceable(DynamicElement element1, StaticElement element2) {
        return policyReference.allBlocks.contains(element2.getType());
    }

    protected void checkHorizontal(DynamicElement element1, StaticElement element2) {
        // horizontal collision
        if (element2.getX() >= element1.getX()) {
            element1.setSpeedX(Math.min(0, element1.getSpeedX()));
            element1.setX(element1.getX() - 8);
        } // d -> s
        else {
            element1.setSpeedX((Math.max(0, element1.getSpeedX())));
            element1.setX(element1.getX() + 8);
        } // s <- d
    }

    protected void headCollision (DynamicElement element1) {
        element1.setSpeedY(Math.max(0, element1.getSpeedY())); // gonna run this twice ,but it won't be an issue
    }

    protected void bottomCollision(DynamicElement element1) {
        if (element1.getType().equals("SlimeBlock")) {
            if (!policyReference.onSlime) {
                gameLogic.activateSuperJump();
                if (element1.getSpeedY() > 0)
                    gameLogic.jump();
            }
        } else if (policyReference.onSlime)
            gameLogic.deactivateSuperJump();
        policyReference.onGround = true;
        element1.setSpeedY(Math.min(0, element1.getSpeedY()));
    }

    protected void customBlockBehaviour(DynamicElement element1) {}
}
