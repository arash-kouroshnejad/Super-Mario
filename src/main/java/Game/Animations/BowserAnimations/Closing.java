package Game.Animations.BowserAnimations;

import Core.Objects.DynamicElement;
import Core.Render.ViewPort;
import Game.Animations.AbstractAnimation;
import Game.Animations.Sprint;

public class Closing extends AbstractAnimation {
    private final Sprint sprint;
    private boolean moving;

    public Closing(DynamicElement element) {
        super(element);
        sprint = Sprint.getSprint(element, 100);
    }
    // TODO : implement proper mirroring functionality
    @Override
    public void run() {
        if (!moving) {
            boolean toRight = element.getX() > ViewPort.getInstance().getLockedElement().getX();
            element.getManager().setMirrored(!toRight);
            Sprint.setMirrored(element, !toRight);
            element.setSpeedX(toRight ? 2 : -2);
            moving = true;
            sprint.run();
        }
        else {
            sprint.run();
        }
    }

    @Override
    public void reset() {
        moving = false;
    }
}
