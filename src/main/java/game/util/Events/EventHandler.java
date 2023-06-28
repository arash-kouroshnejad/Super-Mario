package game.util.Events;

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

    protected Event event;

    public EventHandler(Event event) {
        this.event = event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
