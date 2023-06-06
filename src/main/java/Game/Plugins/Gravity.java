package Game.Plugins;

import Core.Objects.DynamicElement;
import Core.Util.Semaphore;

import java.util.ArrayList;
import java.util.List;

public class Gravity extends Thread{ // TODO : make it abstract and move it to engine
    private List<DynamicElement> elements;

    public void setElements(List<DynamicElement> elements) {
        this.elements = elements;
    }

    private boolean killed;

    private final Semaphore semaphore = new Semaphore(0);

    private boolean paused;

    public void run() {
        while (!killed) {
            if (paused)
                semaphore.forceLock();
            try {
                for (DynamicElement element : elements) {
                    if (!element.isHidden() && !(element.getType().equals("Plant") || element.getType().equals("Bullet")
                            || element.getType().equals("Bird") || element.getType().equals("FireBall")))
                        element.setSpeedY(element.getSpeedY() + 1);
                }
                Thread.sleep(100);
            } catch (Exception ignored) {}
        }
    }

    public void apply() {
        killed = false;
        super.start();
    }

    public void pause() {
        paused = true;
    }

    public void restart() {
        paused = false;
        semaphore.forceRelease();
    }

    public void remove() {killed = true;}
}
