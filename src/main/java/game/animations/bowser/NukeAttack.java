package game.animations.bowser;

import core.objects.DynamicElement;
import game.util.events.Event;
import game.util.events.EventQueue;
import game.util.events.EventType;

public class NukeAttack extends BowserAnimation{
    public NukeAttack(DynamicElement element) {
        super(element);
    }

    @Override
    public void run() {
        element.setSpeedX(0);
        faceMario();
        element.getManager().resetState();
        int x = (int) (Math.random() * 1300);
        EventQueue.getInstance().publish(new Event(EventType.GenerateElement, x + "x100,Bomb"));
        try {
            Thread.sleep(3000);
        } catch (Exception ignored) {}
    }
}
