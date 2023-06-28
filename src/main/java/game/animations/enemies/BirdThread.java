package game.animations.enemies;

import core.objects.DynamicElement;
import core.objects.ElementManager;
import core.render.ViewPort;
import game.util.Events.Event;
import game.util.Events.EventQueue;
import game.util.Events.EventType;

public class BirdThread extends ElementManager {
    public BirdThread(DynamicElement element) {
        super(element, () -> {
            try {
                if (ViewPort.getInstance().inView(element)) {
                    element.swapImage(1);
                    EventQueue.getInstance().publish(new Event(EventType.GenerateElement, element.getX() + "x" +
                            element.getY() + ",Bomb"));
                    Thread.sleep(2500);
                    element.swapImage(0);
                    Thread.sleep(2500);
                } else
                    Thread.sleep(5000);
            } catch (Exception ignored) {}
        });
    }
}
