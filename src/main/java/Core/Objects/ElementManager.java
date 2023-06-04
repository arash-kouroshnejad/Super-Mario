package Core.Objects;

import Core.Util.Semaphore;

public class ElementManager extends Thread{

    protected final DynamicElement element;

    protected Runnable animation;

    private final Semaphore semaphore = new Semaphore(0);

    private boolean paused;

    protected boolean killed;

    protected boolean mirrored;

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

    public void resetState() {}

    public void setMirrored(boolean mirrored) {
        this.mirrored = mirrored;
    }

    public boolean isMirrored() {
        return mirrored;
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

    public void setAnimation(Runnable animation) {
        this.animation = animation;
    }

    public boolean isPaused() {
        return paused;
    }
}
