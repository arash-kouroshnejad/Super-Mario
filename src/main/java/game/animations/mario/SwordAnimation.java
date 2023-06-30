package game.animations.mario;

import core.objects.DynamicElement;
import game.animations.AbstractAnimation;
import game.policy.PolicyStack;
import game.util.events.Event;
import game.util.events.EventQueue;
import game.util.events.EventType;
import persistence.Config;

public class SwordAnimation extends AbstractAnimation {
    public SwordAnimation(DynamicElement element) {
        super(element);
    }

    @Override
    public void run() {
        var manager = element.getManager();
        PolicyStack.getInstance().disableKeys();
        manager.pause();
        element.setSpeedX(0);
        String reference = element.getType() + "Sword" + (manager.isMirrored() ? "Mirrored" : "");
        element.swapImage(Config.getInstance().getProperty(reference, Integer.class));
        try {
            Thread.sleep(2000);
        } catch (Exception ignored) {}
        manager.resetState();
        PolicyStack.getInstance().resetKeys();
        boolean mirrored = manager.isMirrored();
        int x = element.getX() + ((mirrored) ? -50 : (element.getWidth() + 50));
        int y = element.getY();
        EventQueue.getInstance().publish(new Event(EventType.GenerateElement, x + "x" + y + ",Sword"));
    }
}
