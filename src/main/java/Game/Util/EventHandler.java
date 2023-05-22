package Game.Util;

import Core.Util.Semaphore;


import java.util.Queue;

public abstract class EventHandler extends Thread{
    protected Semaphore semaphore = new Semaphore(0);
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
    }
}
