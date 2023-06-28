package game.animations.bowser;

import control.GameManager;
import core.objects.DynamicElement;
import core.render.ViewPort;
import game.animations.AbstractAnimation;
import game.animations.Sprint;
import game.BowserLogic;

public abstract class BowserAnimation extends AbstractAnimation {
    protected final BowserLogic logic;
    public BowserAnimation(DynamicElement element) {
        super(element);
        logic = GameManager.getInstance().getBowserLogic();
    }

    protected void faceMario() {
        boolean toRight = element.getX() < ViewPort.getInstance().getLockedElement().getX();
        element.getManager().setMirrored(!toRight);
        Sprint.setMirrored(element, !toRight);
    }
}
