package game.policy.policies.objects.dynamics;

import control.GameManager;
import core.editor.LevelEditor;
import core.objects.DynamicElement;
import game.policy.DynamicPolicy;

public class KoopaInShell extends DynamicPolicy {
    @Override
    public boolean enforce(DynamicElement element, DynamicElement element1) {
        if (element1.getType().equals("Koopa")) {
            element1 = element;
        }
        var editor = LevelEditor.getInstance();
        var marioLogic = GameManager.getInstance().getGameLogic();
        var bowserLogic = GameManager.getInstance().getBowserLogic();
        if (element1.isLockedCharacter()) {
            marioLogic.takeDamage();
            return true;
        }
        else if (element1.getType().equals("Bowser"))
            bowserLogic.takeCut();
        else
            editor.removeElement(element1);
        return false;
    }

    @Override
    public boolean isEnforceable(DynamicElement element, DynamicElement element1) {
        var tmp = element;
        if (element1.getType().equals("Koopa")) {
            element = element1;
            element1 = tmp;
        }
        return element.getType().equals("Koopa") && element.getManager().isPaused();
    }
}
