package game.policy.policies.general;


import core.editor.LevelEditor;
import core.objects.DynamicElement;
import core.render.ViewPort;
import game.policy.DynamicPolicy;

public class HeightPolicy extends DynamicPolicy {
    @Override
    public boolean enforce(DynamicElement element, DynamicElement element1) {
        if (element.getBounds().BOTTOM > ViewPort.getInstance().getHeight() - 20) { // TODO : read the bottom bound from config
            if (element.isLockedCharacter()) {
                policyReference.currentGame.setScore(Math.max(policyReference.currentGame.getScore() - 30, 0));
                policyReference.marioLogic.killMario();
                return true;
            }
            var editor = LevelEditor.getInstance();
            editor.removeElement(element);
        }
        return false;
    }

    @Override
    public boolean isEnforceable(DynamicElement element, DynamicElement element1) {
        return true;
    }
}
