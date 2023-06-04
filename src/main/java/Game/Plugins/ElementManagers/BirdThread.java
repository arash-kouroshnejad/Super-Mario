package Game.Plugins.ElementManagers;

import Core.Objects.DynamicElement;
import Core.Objects.ElementManager;
import Core.Render.ViewPort;
import Game.Util.Events.Event;
import Game.Util.Events.EventQueue;
import Game.Util.Events.EventType;

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
