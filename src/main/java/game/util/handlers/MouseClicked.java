package game.util.handlers;

import game.plugins.ModalPanel;
import game.util.events.Event;
import game.util.events.EventHandler;
import game.util.events.EventQueue;

public class MouseClicked extends EventHandler {

    public MouseClicked(Event event) {
        super(event);
    }

    public void run() {
        var panel = ModalPanel.getInstance();
        int x = Integer.parseInt(event.attribute().split(",")[0]);
        int y = Integer.parseInt(event.attribute().split(",")[1]);
        if (panel.isUp()) {
            panel.pick(x, y);
        }
        EventQueue.getInstance().consume(event);
    }

    /*public MouseClicked() {
        setName("Mouse Handler");
    }
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
    }*/
}
