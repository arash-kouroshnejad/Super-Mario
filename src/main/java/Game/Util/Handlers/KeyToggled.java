package Game.Util.Handlers;


import Core.Objects.DynamicElement;
import Core.Render.ViewPort;
import Game.Util.Events.Event;
import Game.Util.Events.EventHandler;
import Game.Util.Events.EventQueue;
import Game.Util.Events.EventType;
import Persistence.Config;


import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class KeyToggled extends EventHandler {
    private final static int UP = 38;
    private final static int DOWN = 40;
    private final static int RIGHT = 39;
    private final static int LEFT = 37;
    private final static int ESCAPE = 27;
    private final static int X = 88;

    private static final Set<Integer> registeredKeys = new HashSet<Integer>();

    public KeyToggled(Event event) {
        super(event);
    }

    public void run() {
        var mario = ViewPort.getInstance().getLockedElement();
        int keyCode = Integer.parseInt(event.attribute().split(",")[0]);
        boolean pressed = event.attribute().split(",")[1].equals("Press");
        Config c = Config.getInstance();
        if (pressed) {
            switch (keyCode) {
                case UP -> {
                    if (registeredKeys.contains(DOWN)) {
                        // TODO : throw the sword
                    } else {
                        if (mario.getManager().isMirrored()) {
                            mario.swapImage(c.getProperty(mario.getType() + "MirroredJumping", Integer.class));
                        } else {
                            mario.swapImage(c.getProperty(mario.getType() + "Jumping", Integer.class));
                        }
                    }
                }
                case DOWN -> {
                    registeredKeys.add(keyCode);
                }
                case RIGHT -> {
                    mario.getManager().setMirrored(false);
                    mario.getManager().restart();
                }
                case LEFT -> {
                    mario.getManager().setMirrored(true);
                    mario.getManager().restart();
                }
                case ESCAPE ->
                        EventQueue.getInstance().publish(new Event(EventType.ModalTriggered, "PauseOptions"));
                case X ->
                        EventQueue.getInstance().publish(new Event(EventType.ModalTriggered, "SaveOptions"));
            }
        } else {
            registeredKeys.remove(keyCode);
            switch (keyCode) {
                case RIGHT, LEFT -> {
                    mario.getManager().pause();
                    mario.getManager().resetState();
                }
            }
        }
    }


    /*public KeyToggled() {
        setName("Key Handler");
    }
    @Override
    protected void register(Queue<Event> queue) {
        for (var event : queue) {
            if (event.type().equals(EventType.KeyToggled)) {
                var mario = ViewPort.getInstance().getLockedElement();
                int keyCode = Integer.parseInt(event.attribute().split(",")[0]);
                boolean pressed = event.attribute().split(",")[1].equals("Press");
                Config c = Config.getInstance();
                if (pressed) {
                    switch (keyCode) {
                        case UP -> {
                            if (registeredKeys.contains(DOWN)) {
                                // TODO : throw the sword
                            } else {
                                if (mario.getManager().isMirrored()) {
                                    mario.swapImage(c.getProperty(mario.getType() + "MirroredJumping", Integer.class));
                                } else {
                                    mario.swapImage(c.getProperty(mario.getType() + "Jumping", Integer.class));
                                }
                            }
                        }
                        case DOWN -> {
                            registeredKeys.add(keyCode);
                        }
                        case RIGHT -> {
                            mario.getManager().setMirrored(false);
                            mario.getManager().restart();
                        }
                        case LEFT -> {
                            mario.getManager().setMirrored(true);
                            mario.getManager().restart();
                        }
                        case ESCAPE ->
                                EventQueue.getInstance().publish(new Event(EventType.ModalTriggered, "PauseOptions"));
                        case X ->
                                EventQueue.getInstance().publish(new Event(EventType.ModalTriggered, "SaveOptions"));
                    }
                } else {
                    registeredKeys.remove(keyCode);
                    switch (keyCode) {
                        case RIGHT, LEFT -> {
                            mario.getManager().pause();
                            mario.getManager().resetState();
                        }
                    }
                }
                EventQueue.getInstance().consume(event);
            }
        }
        semaphore.forceLock();
    }*/
}
