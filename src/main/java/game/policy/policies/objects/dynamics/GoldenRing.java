package game.policy.policies.objects.dynamics;

import core.editor.LevelEditor;
import core.objects.DynamicElement;
import game.policy.DynamicPolicy;

public class GoldenRing extends DynamicPolicy {
    @Override
    public boolean enforce(DynamicElement element, DynamicElement element1) {
        var tmp = element;
        var editor = LevelEditor.getInstance();
        if (element1.getType().equals("GoldenRing")) {
            element = element1;
            element1 = tmp;
        }
        if (element1.getType().equals("Goomba") || element1.getType().equals("Koopa") ||
                element.getType().equals("Spiny")) {
            editor.removeElement(element1);
            manager.getGameLogic().getSoundSystem().play("Explosion", false, false);
        }
        return false;
    }

    @Override
    public boolean isEnforceable(DynamicElement element, DynamicElement element1) {
        return element.getType().equals("GoldenRing") || element1.getType().equals("GoldenRing");
    }
}
