package game.policy.policies.objects.dynamics;

import control.GameManager;
import core.editor.LevelEditor;
import core.objects.DynamicElement;
import core.objects.Layers;
import game.policy.DynamicPolicy;

import java.awt.*;
import java.util.List;

public class Bomb extends DynamicPolicy {
    @Override
    public boolean enforce(DynamicElement element, DynamicElement element1) {
        if (element1.getType().equals("Bomb")) {
            element = element1;
        }
        simulateExplosion(element, policyReference.dynamics);
        return false;
    }

    @Override
    public boolean isEnforceable(DynamicElement element, DynamicElement element1) {
        return element.getType().equals("Bomb") || element1.getType().equals("Bomb");
    }

    public static void simulateExplosion(DynamicElement bomb, List<DynamicElement> dynamics) {
        var editor = LevelEditor.getInstance();
        editor.removeElement(bomb);
        var marioLogic = GameManager.getInstance().getGameLogic();
        var bowserLogic = GameManager.getInstance().getBowserLogic();
        // TODO : find nearby enemies
        for (var damaged : dynamics) {
            if (inRange(new Point(bomb.getX(), bomb.getY()), damaged)) {
                if (damaged.isLockedCharacter())
                    marioLogic.takeDamage();
                else if (damaged.getType().equals("Bowser"))
                    bowserLogic.takeBullet();
                else
                    editor.removeElement(damaged);
            }
        }
    }

    private static boolean inRange(Point point, DynamicElement element) {
        if (Math.min(Math.abs(point.x - element.getX()), Math.abs(point.y - element.getY())) < 100) {
            return Math.sqrt(Math.pow(point.x - element.getX(), 2) + Math.pow(point.y - element.getY(), 2)) < 100;
        }
        return false;
    }
}
