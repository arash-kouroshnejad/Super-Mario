package Game.Util.Events;


import Game.Util.Handlers.*;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventQueue {
    private static final EventQueue instance = new EventQueue();

    private EventQueue(){}


    private final ExecutorService pool = Executors.newFixedThreadPool(10);

    private final Queue<Event> queue = new ConcurrentLinkedQueue<>();

    private final ArrayList<EventHandler> handlers = new ArrayList<>();

    public synchronized void publish(Event event) {
        /*queue.add(event);
        for (var handler : handlers)
            handler.restart();*/
        switch (event.type()) {
            case KeyToggled -> pool.submit(new KeyToggled(event));
            case ModalTriggered -> pool.submit(new ModalTriggered(event));
            case PowerUpTriggered -> pool.submit(new ElementFactory(event));
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

    public void addHandler(EventHandler handler) {
        handlers.add(handler);
    }

    public static EventQueue getInstance() {
        return instance;
    }
}
