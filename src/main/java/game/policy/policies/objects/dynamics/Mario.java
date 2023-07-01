package game.policy.policies.objects.dynamics;

import control.GameManager;
import core.editor.LevelEditor;
import core.objects.DynamicElement;
import game.policy.DynamicPolicy;

public class Mario extends DynamicPolicy {
    @Override
    public boolean enforce(DynamicElement element, DynamicElement element1) {
        var tmp = element;
        if (element1.isLockedCharacter()) {
            element = element1;
            element1 = tmp;
        }
        var editor = LevelEditor.getInstance();
        if (!element1.isHidden()) {
            switch (element1.getType()) {
                case "Coin":
                    policyReference.soundSystem.play("CoinEarned", false, false);
                    policyReference.currentGame.earnCoin();
                    element1.setHidden(true);
                    editor.removeElement(element1);
                    break;
                case "Star":
                    policyReference.marioLogic.earnPowerUp();
                    policyReference.shield.activate(15);
                    policyReference.currentGame.setScore(policyReference.currentGame.getScore() + 40);
                    element1.setHidden(true);
                    editor.removeElement(element1);
                    break;
                case "Plant":
                    if (!element1.isHidden()) {
                        policyReference.marioLogic.takeDamage();
                        return true; // todo terminate logic
                    }
                    break;
                case "Goomba":
                    if (!element1.isHidden()) {
                        if (element.collidesHorizontally(element1) && !policyReference.shield.isActive()) {
                            // TODO : TEST PROPER DEATH FUNCTIONALITY WITH CRAPPY COLLISION DETECTION
                            policyReference.marioLogic.takeDamage();
                            return true;
                        } else {
                            element1.setHidden(true);
                            editor.removeElement(element1);
                            policyReference.currentGame.killEnemy();
                        }
                    }
                    break;
                case "Mushroom":
                    policyReference.marioLogic.earnPowerUp();
                    policyReference.currentGame.setScore(policyReference.currentGame.getScore() + 30);
                    element1.setHidden(true);
                    editor.removeElement(element1);
                    break;
                case "Flower":
                    element1.setHidden(true);
                    editor.removeElement(element1);
                    policyReference.currentGame.setScore(policyReference.currentGame.getScore() + 10); // TODO : turn into a separate method
                    policyReference.marioLogic.earnPowerUp();
                    break;
                case "Koopa":
                    if (!element1.getManager().isPaused()) {
                        if (element.collidesHorizontally(element1)) {
                            policyReference.marioLogic.takeDamage();
                        } else {
                            boolean mirrored = element.getManager().isMirrored();
                            element1.setSpeedX(mirrored ? -2 : 2);
                            element1.setX(element1.getX() + (mirrored ? -5 : 5));
                            element1.getManager().resetState();
                        }
                    } else {
                        policyReference.currentGame.setScore(policyReference.currentGame.getScore() + 3);
                        policyReference.currentGame.killEnemy();
                        editor.removeElement(element1);
                    }
                    break;
                case "Bird", "Spiny", "FireBall":
                    if (!policyReference.shield.isActive())
                        policyReference.marioLogic.takeDamage();
                    else
                        editor.removeElement(element1);
                    break;
                case "Bowser":
                    if (!element.collidesHorizontally(element1))
                        policyReference.bowserLogic.takeHit();
                    break;
            }
        }
        return false;
    }

    @Override
    public boolean isEnforceable(DynamicElement element, DynamicElement element1) {
        return element.isLockedCharacter() || element1.isLockedCharacter();
    }
}
