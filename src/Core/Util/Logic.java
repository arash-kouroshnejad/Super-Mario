package Core.Util;

import Core.Objects.DynamicElement;
import Core.Objects.Layers;

public abstract class Logic {
    protected final static int RIGHT = 1;
    protected final static int LEFT = -1;
    protected final static int DOWN = 2;
    protected final static int UP = -2;

    protected DynamicElement lockedElement;

    public void setLockedElement(DynamicElement lockedElement) {
        this.lockedElement = lockedElement;
    }

    protected final Layers layers = Layers.getInstance();

    public abstract void handleKeyPress(int keyCode);
    public abstract void handleKeyRelease(int keyCode);
    public abstract void check();
    public abstract void init(Loader loader);

    public abstract void stop();

    public abstract  void reset();
}
