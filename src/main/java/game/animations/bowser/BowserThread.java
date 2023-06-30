package game.animations.bowser;

import control.GameManager;
import core.objects.DynamicElement;
import core.objects.ElementManager;
import persistence.Config;

public class BowserThread extends ElementManager {
    public BowserThread (DynamicElement element) {
        super(element, GameManager.getInstance().getBowserLogic());
    }

    @Override
    public void resetState() {
        Config c = Config.getInstance();
        String reference = element.getType() + (element.getManager().isMirrored() ? "Mirrored" : "" ) + "Default";
        element.swapImage(c.getProperty(reference, Integer.class));
    }
}
