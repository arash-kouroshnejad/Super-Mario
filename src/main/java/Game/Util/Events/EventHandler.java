package Game.Util.Events;

import Core.Util.Semaphore;


import java.util.Queue;
import java.util.Set;

public abstract class EventHandler implements Runnable{

    /*protected Semaphore semaphore = new Semaphore(0);
    private boolean killed;
    private final EventQueue queue = EventQueue.getInstance();
    protected abstract void register(Queue<Event> queue);
    public void restart() {
        semaphore.forceRelease();
    }

    public void run() {
        while (!killed) {
            var events = queue.getPublishedEvents();
            register(events);
        }
    }

    public void kill() {
        killed = true;
    }*/

    protected final Event event;

    public EventHandler(Event event) {
        this.event = event;
    }
}
