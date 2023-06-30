package game.animations.items;

import core.objects.DynamicElement;
import core.objects.ElementManager;
import core.render.ViewPort;
import game.model.ShieldTimer;
import game.util.events.Event;
import game.util.events.EventQueue;
import game.util.events.EventType;

public class ShieldThread extends ElementManager {
    public ShieldThread(DynamicElement element) {
        super(element, () -> {
            while (ShieldTimer.getInstance().isActive()) {
                var mario = ViewPort.getInstance().getLockedElement();
                element.setX(mario.getX() + mario.getWidth() / 2 - element.getWidth() / 2);
                // element.setX(ViewPort.getInstance().getLockedElement().getX());
                // element.setY(ViewPort.getInstance().getLockedElement().getY() - element.getHeight() / 2);
                element.setY(ViewPort.getInstance().getLockedElement().getY() - 40);
                ShieldTimer.getInstance().refreshBar();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            EventQueue.getInstance().publish(new Event(EventType.GenerateElement, "0x0,RemoveShield"));
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
