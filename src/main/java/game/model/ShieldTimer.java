package game.model;

import core.render.ViewPort;
import game.plugins.Bar;
import game.util.Events.Event;
import game.util.Events.EventQueue;
import game.util.Events.EventType;

public class ShieldTimer {
    private static final ShieldTimer instance = new ShieldTimer();
    private ShieldTimer() {}

    public static ShieldTimer getInstance() {
        return instance;
    }
    private long activationTime;
    private long duration;
    private Bar bar;

    public void activate (long duration) {
        var mario = ViewPort.getInstance().getLockedElement();
        EventQueue.getInstance().publish(new Event(EventType.GenerateElement,
                mario.getX() + "x" + mario.getY() + ",GenerateShield"));
        this.activationTime = System.currentTimeMillis();
        this.duration = duration;
        bar = Bar.getBar("Shield");
        bar.setPercentage(100);
        try {
            Thread.sleep(20);
        } catch (Exception ignored) {}
    }

    public boolean isActive() {
        return (System.currentTimeMillis() - activationTime < 1000 * duration);
    }

    public void refreshBar() {
        int left = (int) (100 - (System.currentTimeMillis() - activationTime) / (10 * duration));
        bar.setPercentage(left);
        if (left <= 5) {
            bar.remove();
        }
    }

    public void deactivate() {
        duration = 0;
    }
}
