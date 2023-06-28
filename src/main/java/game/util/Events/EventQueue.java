package game.util.Events;


import game.util.Handlers.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventQueue {
    private static final EventQueue instance = new EventQueue();

    private EventQueue(){}


    private final ExecutorService pool = Executors.newFixedThreadPool(1); // TODO : fix level editor and revert to multi threaded handling

    private final Queue<Event> queue = new ConcurrentLinkedQueue<>();

    private final Map<EventType, EventHandler> handlers = new HashMap<>();

    public void init() {
        handlers.put(EventType.KeyToggled, new KeyToggled(null));
        handlers.put(EventType.ModalTriggered, new ModalTriggered(null));
        handlers.put(EventType.GenerateElement, new ElementFactory(null));
        handlers.put(EventType.MouseClicked, new MouseClicked(null));
        handlers.put(EventType.ModalOptionClicked, new ModalOptionSelected(null));
    }

    public synchronized void publish(Event event) {
        /*queue.add(event);
        for (var handler : handlers)
            handler.restart();*/
        /*handlers.get(event.type()).setEvent(event);
        pool.submit(handlers.get(event.type()));*/
        switch (event.type()) {
            case KeyToggled -> pool.submit(new KeyToggled(event));
            case ModalTriggered -> pool.submit(new ModalTriggered(event));
            case GenerateElement -> pool.submit(new ElementFactory(event));
            case MouseClicked -> pool.submit(new MouseClicked(event));
            case ModalOptionClicked -> pool.submit(new ModalOptionSelected(event));
        }
    } //  TODO : test publish duplication

    public Queue<Event> getPublishedEvents() {
        return queue;
    }

    public synchronized void consume (Event event) {
        queue.remove(event);
    }

    public synchronized void startHandlers() {
        /*for (var handler : handlers)
            if (!handler.isAlive())
                handler.start();*/
    }

    public static EventQueue getInstance() {
        return instance;
    }
}
