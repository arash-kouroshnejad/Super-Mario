package game.animations;

import core.objects.DynamicElement;
import game.policy.PolicyReference;
import game.policy.PolicyStack;

public abstract class AbstractAnimation implements Runnable{
    protected PolicyReference policyReference = PolicyReference.getInstance();

    protected final DynamicElement element;

    public AbstractAnimation(DynamicElement element) {
        this.element = element;
    }

    public void reset() {};
}
