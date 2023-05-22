package Game.Util.Handlers;

import Core.Render.ViewPort;
import Game.Plugins.ModalPanel;
import Game.Util.Event;
import Game.Util.EventHandler;
import Game.Util.EventQueue;
import Game.Util.EventType;

import java.util.Queue;

public class MouseClicked extends EventHandler {
    @Override
    protected void register(Queue<Event> queue) {
        for (var event : queue) {
            if (event.type().equals(EventType.MouseClicked)) {
                var panel = ModalPanel.getInstance();
                int x = Integer.parseInt(event.attribute().split(",")[0]);
                int y = Integer.parseInt(event.attribute().split(",")[1]);
                if (panel.isUp()) {
                    panel.pick(x, y);
                }
                EventQueue.getInstance().consume(event);
            }
        }
        semaphore.forceLock();
    }
}
