package game.policy.policies.objects.dynamics;

import control.GameManager;
import core.editor.LevelEditor;
import core.objects.DynamicElement;
import game.policy.DynamicPolicy;

public class PipeSword extends DynamicPolicy {
    @Override
    public boolean enforce(DynamicElement element, DynamicElement element1) {
        var tmp = element;
        if (element1.getType().equals("PipeSword")) {
            element = element1;
            element1 = tmp;
        }
        var editor = LevelEditor.getInstance();
        var bowserLogic = GameManager.getInstance().getBowserLogic();
        if (element1.getType().equals("Goomba") || element1.getType().equals("Plant"))
            editor.removeElement(element1);
        if (element1.getType().equals("Bowser"))
            bowserLogic.takeCut();
        editor.removeElement(element);
        return false;
    }

    @Override
    public boolean isEnforceable(DynamicElement element, DynamicElement element1) {
        return element.getType().equals("PipeSword") || element1.getType().equals("PipeSword");
    }
}
