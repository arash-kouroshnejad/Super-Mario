package Game.Animations.BowserAnimations;

import Core.Objects.DynamicElement;
import Game.Animations.AbstractAnimation;
import Game.Util.Events.Event;
import Game.Util.Events.EventQueue;
import Game.Util.Events.EventType;
import Persistence.Config;

public class FireBallAttack extends AbstractAnimation {
    private final int FireBallAttack;
    private final int FireBallAttackMirrored;
    private final int FireBallReleased;
    private final int FireBallReleasedMirrored;
    private final int defaultState;

    public FireBallAttack(DynamicElement element) {
        super(element);
        Config c = Config.getInstance();
        FireBallAttack = c.getProperty("FireBallAttack", Integer.class);
        FireBallAttackMirrored = c.getProperty("FireBallAttackMirrored", Integer.class);
        FireBallReleased = c.getProperty("FireBallReleased", Integer.class);
        FireBallReleasedMirrored = c.getProperty("FireBallReleasedMirrored", Integer.class);
        defaultState = c.getProperty("BowserDefault", Integer.class);
    }

    @Override
    public void run() {
        try {
            element.setSpeedX(0);
            boolean mirrored = element.getManager().isMirrored();
            element.swapImage((mirrored) ? FireBallAttackMirrored : FireBallAttack);
            Thread.sleep(800);
            EventQueue.getInstance().publish(new Event(EventType.GenerateElement, element.getX() + "x" +
                    element.getY() + ",FireBall"));
            element.swapImage((mirrored) ? FireBallReleasedMirrored : FireBallReleased);
            Thread.sleep(800); // TODO : 800 ms sleep is dangerous maybe mario preforms some action :(
            element.swapImage(defaultState);
            Thread.sleep(2000);
        } catch (Exception ignored) {}
    }

    @Override
    public void reset() {}
}
