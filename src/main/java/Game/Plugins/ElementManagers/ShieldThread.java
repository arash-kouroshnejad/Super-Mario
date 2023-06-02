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
            if (Shield.getInstance().isActive()) {
                element.setX(ViewPort.getInstance().getLockedElement().getX() - element.getWidth() / 2);
                element.setX(ViewPort.getInstance().getLockedElement().getY() - element.getHeight() / 2);
                try {
                    Thread.sleep(8);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else {
                EventQueue.getInstance().publish(new Event(EventType.PowerUpTriggered, "0x0,RemoveShield"));
            }
        });
    }
}
