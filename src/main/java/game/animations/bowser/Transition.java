package game.animations.bowser;

import core.objects.DynamicElement;
import core.render.ViewPort;
import game.animations.AbstractAnimation;
import game.policy.PolicyStack;

public class Transition extends BowserAnimation{
    private final AbstractAnimation jumpAttack;
    public Transition(DynamicElement element) {
        super(element);
        jumpAttack = new JumpAttack(element);
    }

    @Override
    public void run() {
        PolicyStack.getInstance().disableKeys();
        var mario = ViewPort.getInstance().getLockedElement();
        mario.setSpeedX(0);
        mario.getManager().pause();
        mario.getManager().resetState();
        jumpAttack.run();
        logic.enablePhase2();
        PolicyStack.getInstance().resetKeys();
    }
}
