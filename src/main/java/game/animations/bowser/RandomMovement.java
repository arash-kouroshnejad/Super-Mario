package game.animations.bowser;

import core.objects.DynamicElement;
import core.render.ViewPort;
import game.animations.Sprint;

public class RandomMovement extends BowserAnimation {

    private final Sprint sprint;

    public RandomMovement(DynamicElement element) {
        super(element);
        sprint = Sprint.getSprint(element, 100);
    }

    @Override
    public void run() {
        boolean toRight = element.getX() <= 100 || (element.getX() <= 1400 && (element.getSpeedX() == 1 ||
                ViewPort.getInstance().getLockedElement().getX() > element.getX()));
        Sprint.setMirrored(element, !toRight);
        element.getManager().setMirrored(!toRight);
        sprint.run();
        element.setSpeedX(toRight ? 1 : -1);
    }
}
