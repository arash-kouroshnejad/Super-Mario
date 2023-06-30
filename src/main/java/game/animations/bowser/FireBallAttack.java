package game.animations.bowser;

import core.objects.DynamicElement;
import game.util.events.Event;
import game.util.events.EventQueue;
import game.util.events.EventType;
import persistence.Config;

public class FireBallAttack extends BowserAnimation {
    private final int FireBallAttack;
    private final int FireBallAttackMirrored;
    private final int FireBallReleased;
    private final int FireBallReleasedMirrored;

    public FireBallAttack(DynamicElement element) {
        super(element);
        Config c = Config.getInstance();
        FireBallAttack = c.getProperty("FireBallAttack", Integer.class);
        FireBallAttackMirrored = c.getProperty("FireBallAttackMirrored", Integer.class);
        FireBallReleased = c.getProperty("FireBallReleased", Integer.class);
        FireBallReleasedMirrored = c.getProperty("FireBallReleasedMirrored", Integer.class);
    }

    @Override
    public void run() {
        try {
            faceMario();
            element.setSpeedX(0);
            boolean mirrored = element.getManager().isMirrored();
            element.swapImage((mirrored) ? FireBallAttackMirrored : FireBallAttack);
            Thread.sleep(800);
            EventQueue.getInstance().publish(new Event(EventType.GenerateElement, element.getX() + "x" +
                    element.getY() + ",FireBall"));
            element.swapImage((mirrored) ? FireBallReleasedMirrored : FireBallReleased);
            Thread.sleep(800); // TODO : 800 ms sleep is dangerous maybe mario preforms some action :(
            element.getManager().resetState();
            Thread.sleep(2000);
        } catch (Exception ignored) {}
    }

    @Override
    public void reset() {}
}
