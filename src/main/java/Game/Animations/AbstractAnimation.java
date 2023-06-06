package Game.Animations;

import Core.Objects.DynamicElement;

public abstract class AbstractAnimation implements Runnable{
    protected final DynamicElement element;

    public AbstractAnimation(DynamicElement element) {
        this.element = element;
    }

    public void reset() {};
}
