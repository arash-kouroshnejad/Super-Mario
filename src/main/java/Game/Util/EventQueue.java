package Game.Util;


import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class EventQueue {
    private static final EventQueue instance = new EventQueue();
    private EventQueue(){}

    private final ConcurrentLinkedQueue<Event> queue = new ConcurrentLinkedQueue<>();
    private final ArrayList<EventHandler> handlers = new ArrayList<>();

    public synchronized void publish(Event event) {
        queue.add(event);
        for (var handler : handlers)
            handler.restart();
    }
    public Queue<Event> getPublishedEvents() {
        return queue;
    }

    public synchronized void consume (Event event) {
        queue.remove(event);
    }
    public synchronized void startHandlers() {
        for (var handler : handlers)
            handler.start();
    }

    public synchronized void killHandlers() {
        for (var handler : handlers)
            handler.kill();
    }
    public void addHandler(EventHandler handler) {
        handlers.add(handler);
    }

    public static EventQueue getInstance() {
        return instance;
    }
}
