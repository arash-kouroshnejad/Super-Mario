package Core.Objects;

public abstract class ElementManager extends Thread{
    private final DynamicElement element;
    private final Runnable animation;

    protected boolean killed;

    public ElementManager( DynamicElement element, Runnable animation) {
        this.element = element;
        this.animation = animation;
    }

    public void run() {
        while (!killed) {
            animation.run();
        }
    }

    public void pause() {
        killed = true;
    }

    public boolean isKilled() {
        return killed;
    }
}
