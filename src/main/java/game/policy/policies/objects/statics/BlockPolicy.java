package game.policy.policies.objects.statics;

import core.editor.LevelEditor;
import core.objects.DynamicElement;
import core.objects.StaticElement;
import game.model.TimedBlock;
import game.policy.D2SPolicy;
import game.policy.policies.objects.dynamics.Bomb;


public class BlockPolicy extends D2SPolicy {
    @Override
    public boolean enforce(DynamicElement element1, StaticElement element2) {
        if (element1.isLockedCharacter()) {
            if (element1.collidesHorizontally(element2) && !element2.isHidden()) {
                checkHorizontal(element1, element2);
            }
            else {
                // vertical collision
                if (element1.beneath(element2)) {
                    headCollision(element1);
                    customBlockBehaviour(element1, element2);
                } // s > d
                else {
                    if (TimedBlock.isTimed(element2))
                        TimedBlock.getBlock(element2).check(10);
                    bottomCollision(element1);
                } // s < d
            }
        }
        else {
            var editor = LevelEditor.getInstance();
            if (element1.getType().equals("Bullet") || element1.getType().equals("FireBall") ||
                    element1.getType().equals("PipeSword"))
                editor.removeElement(element1);
            else if (element1.getType().equals("Bomb")) {
                Bomb.simulateExplosion(element1, policyReference.dynamics);
                policyReference.soundSystem.play("Explosion", false, false);
            }
            else if (element1.getType().equals("Bowser") && policyReference.hoveringBlocks.contains(element2.getType()))
                editor.removeElement(element2);
            else {
                if (element1.collidesHorizontally(element2)) {
                    element1.setSpeedX(-element1.getSpeedX());
                    if (element1.getManager() != null)
                        element1.getManager().setMirrored(!element1.getManager().isMirrored());
                } else
                    element1.setSpeedY(Math.min(0, -(int) ((0.6) * element1.getSpeedY())));
            }
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
            element1.setX(element1.getX() - 10);
        } // d -> s
        else {
            element1.setSpeedX((Math.max(0, element1.getSpeedX())));
            element1.setX(element1.getX() + 10);
        } // s <- d
    }

    protected void headCollision (DynamicElement element1) {
        element1.setSpeedY(Math.max(0, element1.getSpeedY()));
        element1.setY(element1.getY() + 10);
    }

    protected void bottomCollision(DynamicElement element1) {
        if (element1.getType().equals("SlimeBlock")) {
            if (!policyReference.onSlime) {
                policyReference.marioLogic.activateSuperJump();
                if (element1.getSpeedY() > 0)
                    policyReference.marioLogic.jump();
            }
        } else if (policyReference.onSlime)
            policyReference.marioLogic.deactivateSuperJump();
        policyReference.onGround = true;
        element1.setSpeedY(Math.min(0, element1.getSpeedY()));
        if (element1.getType().equals("Floor")) {
            TimedBlock.reset();
            policyReference.bowserLogic.accumulateTimer(10);
        }
    }

    protected void customBlockBehaviour(DynamicElement element1, StaticElement element2) {}
}
