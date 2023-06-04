package Game.Model;

import Core.Editor.LevelEditor;
import Core.Objects.StaticElement;
import Core.Render.ViewPort;
import Game.Util.Events.Event;
import Game.Util.Events.EventQueue;
import Game.Util.Events.EventType;


import java.awt.*;

public class Shield {
    private static Shield instance = new Shield();
    private Shield() {}

    public static Shield getInstance() {
        return instance;
    }
    private long activationTime;
    private long duration;

    private StaticElement element;

    public void activate (long duration) {
        var mario = ViewPort.getInstance().getLockedElement();
        EventQueue.getInstance().publish(new Event(EventType.GenerateElement,
                mario.getX() + "x" + mario.getY() + ",GenerateShield"));
        this.activationTime = System.currentTimeMillis();
        this.duration = duration;
        /*try {
            Thread.sleep(20);
        } catch (Exception ignored) {}
        element = LevelEditor.getInstance().getStaticElement("GoldenRing", 0, 0).orElseThrow();*/
    }

    public boolean isActive() {
        return (System.currentTimeMillis() - activationTime < 1000 * duration);
    }

    public int getLeft () {
        if (isActive())
            return (int) (100 - (System.currentTimeMillis() - activationTime) / (10 * duration)); // TODO : use logic time instead
        return 0;
    }

    public void deactivate() {
        duration = 0;
    }

    public void updateElementPosition() {
        var coordinates = ViewPort.getInstance().getLockedElement().getPosition();
        var dimensions = ViewPort.getInstance().getLockedElement().getDimensions();
        element.setX(coordinates.x + dimensions.width / 2 - element.getWidth() / 2);
        element.setY(coordinates.y - 40);
    }
}
