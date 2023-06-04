package Game.Plugins.ElementManagers;

import Core.Objects.DynamicElement;
import Core.Objects.ElementManager;
import Core.Render.ViewPort;
import Game.Model.Shield;
import Game.Util.Events.Event;
import Game.Util.Events.EventQueue;
import Game.Util.Events.EventType;

public class ShieldThread extends ElementManager {
    public ShieldThread(DynamicElement element) {
        super(element, () -> {
            while (Shield.getInstance().isActive()) {
                var mario = ViewPort.getInstance().getLockedElement();
                element.setX(mario.getX() + mario.getWidth() / 2 - element.getWidth() / 2);
                // element.setX(ViewPort.getInstance().getLockedElement().getX());
                // element.setY(ViewPort.getInstance().getLockedElement().getY() - element.getHeight() / 2);
                element.setY(ViewPort.getInstance().getLockedElement().getY() - 40);
                try {
                    Thread.sleep(15);
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
