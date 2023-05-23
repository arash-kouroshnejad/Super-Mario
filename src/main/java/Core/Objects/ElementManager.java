package Core.Objects;

import Core.Util.Semaphore;

public abstract class ElementManager extends Thread{
    private final DynamicElement element;
    private final Runnable animation;

    private final Semaphore semaphore = new Semaphore(0);

    private boolean paused;

    protected boolean killed;

    public ElementManager( DynamicElement element, Runnable animation) {
        this.element = element;
        this.animation = animation;
    }

    public void run() {
        while (!killed) {
            if (paused)
                semaphore.forceLock();
            animation.run();
        }
    }

    public void pause() {
        paused = true;
    }

    public void restart() {
        paused = false;
        semaphore.forceRelease();
    }

    public void kill() {
        killed = true;
    }

    public boolean isKilled() {
        return killed;
    }
}
