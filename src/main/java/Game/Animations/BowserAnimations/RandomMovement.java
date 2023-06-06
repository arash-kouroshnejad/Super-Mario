package Game.Animations.BowserAnimations;

import Core.Objects.DynamicElement;
import Game.Animations.AbstractAnimation;
import Game.Animations.Sprint;

public class RandomMovement extends AbstractAnimation {

    private final Sprint sprint;
    private boolean moving;

    public RandomMovement(DynamicElement element) {
        super(element);
        sprint = Sprint.getSprint(element, 100);
    }

    @Override
    public void run() {
        try {
            if (!moving) {
                boolean toRight = element.getX() <= 100;
                element.setSpeedX(toRight ? 1 : -1);
                moving = true;
                sprint.run();
            }
            else
                sprint.run();
        } catch (Exception ignored) {}
    }
}
