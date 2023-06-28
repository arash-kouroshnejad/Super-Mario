package game.policy.policies.General;


import core.objects.DynamicElement;
import core.render.ViewPort;
import game.policy.DynamicPolicy;

public class HeightPolicy extends DynamicPolicy {
    @Override
    public boolean enforce(DynamicElement element) {
        if (element.getBounds().BOTTOM > ViewPort.getInstance().getHeight() - 20) { // TODO : read the bottom bound from config
            element.setHidden(true);
            policyReference.removalList.add(element);
            if (element.isLockedCharacter()) {
                policyReference.currentGame.setScore(Math.max(policyReference.currentGame.getScore() - 30, 0));
                gameLogic.killMario();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isEnforceable(DynamicElement element) {
        return true;
    }
}
