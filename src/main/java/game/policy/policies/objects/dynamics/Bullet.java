package game.policy.policies.objects.dynamics;

import core.editor.LevelEditor;
import core.objects.DynamicElement;
import game.policy.DynamicPolicy;

public class Bullet extends DynamicPolicy {
    @Override
    public boolean enforce(DynamicElement element, DynamicElement element1) {
        var editor = LevelEditor.getInstance();
        var tmp = element;
        if (element1.getType().equals("Bullet")) {
            element = element1;
            element1 = tmp;
        }
        if (element1.getType().equals("Goomba") || element1.getType().equals("Plant") ||
                element1.getType().equals("Spiny") || element1.getType().equals("Bird")) {
            editor.removeElement(element1);
            policyReference.currentGame.killEnemy();
        }
        else if (element1.getType().equals("Bowser"))
            policyReference.bowserLogic.takeBullet();
        if (!element1.getType().equals("GoldenRing"))
            editor.removeElement(element);
        return false;
    }

    @Override
    public boolean isEnforceable(DynamicElement element, DynamicElement element1) {
        return element.getType().equals("Bullet") || element1.getType().equals("Bullet");
    }
}
