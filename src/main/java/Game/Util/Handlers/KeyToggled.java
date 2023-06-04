package Game.Util.Handlers;


import Control.GameManager;
import Core.Objects.ElementManager;
import Core.Render.ViewPort;
import Game.Util.Events.Event;
import Game.Util.Events.EventHandler;
import Game.Util.Events.EventQueue;
import Game.Util.Events.EventType;
import Persistence.Config;


import java.util.HashSet;
import java.util.Set;

public class KeyToggled extends EventHandler {
    private final static int UP = 38;
    private final static int DOWN = 40;
    private final static int RIGHT = 39;
    private final static int LEFT = 37;
    private final static int ESCAPE = 27;
    private final static int X = 88;
    private final static int SPACE = 32;
    private static boolean crouching;
    private static final Set<Integer> registeredKeys = new HashSet<>();
    private static Timer timer;

    public KeyToggled(Event event) {
        super(event);
    }

    public void run() {
        var mario = ViewPort.getInstance().getLockedElement();
        int keyCode = Integer.parseInt(event.attribute().split(",")[0]);
        boolean pressed = event.attribute().split(",")[1].equals("Press");
        Config c = Config.getInstance();
        if (pressed) {
            registeredKeys.add(keyCode);
            switch (keyCode) {
                case UP -> {
                    if (registeredKeys.contains(DOWN))
                        generateSword();
                    else {
                        if (mario.getManager().isMirrored()) {
                            mario.swapImage(c.getProperty(mario.getType() + "MirroredJumping", Integer.class));
                        } else {
                            mario.swapImage(c.getProperty(mario.getType() + "Jumping", Integer.class));
                        }
                    }
                }
                case DOWN -> {
                    if (registeredKeys.contains(UP))
                        generateSword();
                    else {
                        if (!mario.getType().equals("MiniMario") && !crouching && !(registeredKeys.contains(RIGHT) || registeredKeys.contains(LEFT))) {
                            crouching = true;
                            String type = mario.getType() + (mario.getManager().isMirrored() ? "Mirrored" : "") + "Crouching";
                            mario.getManager().pause();
                            mario.setHeight((int) (mario.getHeight() * 0.75));
                            mario.setY(mario.getY() + mario.getHeight() / 3);
                            mario.swapImage(Config.getInstance().getProperty(type, Integer.class)); // TODO : MOVE INTO ELEMENT FACTORY
                            registeredKeys.add(keyCode);
                        }
                    }
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
                case SPACE ->
                    EventQueue.getInstance().publish(new Event(EventType.GenerateElement, "0x0,Bullet"));
            }
        } else {
            switch (keyCode) {
                case RIGHT, LEFT, UP, DOWN -> {
                    if (keyCode == DOWN && !mario.getType().equals("MiniMario") && crouching) {
                        crouching = false;
                        mario.setY(mario.getY() - mario.getHeight() / 3);
                        mario.setHeight((mario.getHeight() * 4) / 3);
                    }
                    else if ((keyCode == DOWN && registeredKeys.contains(UP)) || (keyCode == UP && registeredKeys.contains(DOWN))) {}
                        // timer.stop();
                    else {
                        mario.getManager().pause();
                        mario.getManager().resetState();
                    }
                }
            }
            registeredKeys.remove(keyCode);
        }
    }

    private static void generateSword () {
        /*if (GameManager.getInstance().getCoins() >= 3) {
            GameManager.getInstance().getCurrentGame().setCoinsEarned(GameManager.getInstance().getCoins() - 3);
            timer = new Timer(2, () -> {EventQueue.getInstance().publish(new Event(EventType.GenerateElement,
                    "0x0,Sword"));});
        }*/
    }

    private static class Timer implements Runnable {
        double secondsElapsed;
        final int limit;
        private final ElementManager thread;
        boolean completed;
        private final Runnable result;
        public Timer(int limit, Runnable result) {
            thread = new ElementManager(null, this);
            this.limit = limit;
            this.result = result;
        }

        public void run() {
            try {
                if (secondsElapsed < limit) {
                    secondsElapsed += 0.1;
                    Thread.sleep(100);
                } else {
                    completed = true;
                    result.run();
                    stop();
                }
            } catch (Exception ignored) {}
        }

        public void stop() {
            thread.kill();
        }
    }
}
