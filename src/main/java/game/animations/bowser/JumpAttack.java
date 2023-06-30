package game.animations.bowser;

import core.objects.DynamicElement;
import persistence.Config;

public class JumpAttack extends BowserAnimation {
    private final int[] jumpIndexes = new int[2];
    private final int[] landingIndexes = new int[2];

    public JumpAttack(DynamicElement element) {
        super(element);
        Config c = Config.getInstance();
        jumpIndexes[0] = c.getProperty("JumpAttack", Integer.class);
        jumpIndexes[1] = c.getProperty("JumpAttackMirrored", Integer.class);
        landingIndexes[0] = c.getProperty("BowserLanding", Integer.class);
        landingIndexes[1] = c.getProperty("BowserLandingMirrored", Integer.class);
    }

    @Override
    public void run() {
        try {
            element.setSpeedX(0);
            int index = 0;
            if (element.getManager().isMirrored())
                index = 1;
            element.swapImage(jumpIndexes[index]);
            element.setSpeedY(-8);
            element.setY(element.getY() - 2);
            Thread.sleep(800);
            element.setSpeedY(0);
            element.swapImage(landingIndexes[index]);
            Thread.sleep(800);
            element.setSpeedY(0);
            element.getManager().resetState();
            Thread.sleep(3000);
        } catch (Exception ignored) {}
    }
}
