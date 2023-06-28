package game.animations.bowser;

import control.GameManager;
import core.objects.DynamicElement;
import core.objects.ElementManager;

public class BowserThread extends ElementManager {
    public BowserThread (DynamicElement element) {
        super(element, GameManager.getInstance().getBowserLogic());
    }
}
