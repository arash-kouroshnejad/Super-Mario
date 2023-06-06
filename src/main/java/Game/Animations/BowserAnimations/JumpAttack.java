package Game.Animations.BowserAnimations;

import Core.Objects.DynamicElement;
import Game.Animations.AbstractAnimation;
import Persistence.Config;

public class JumpAttack extends AbstractAnimation {
    private final int[] jumpIndexes = new int[2];
    private final int[] landingIndexes = new int[2];
    private final int bowserDefault;

    public JumpAttack(DynamicElement element) {
        super(element);
        Config c = Config.getInstance();
        jumpIndexes[0] = c.getProperty("JumpAttack", Integer.class);
        jumpIndexes[1] = c.getProperty("JumpAttackMirrored", Integer.class);
        landingIndexes[0] = c.getProperty("BowserLanding", Integer.class);
        landingIndexes[1] = c.getProperty("BowserLandingMirrored", Integer.class);
        bowserDefault = c.getProperty("BowserDefault", Integer.class);
    }

    @Override
    public void run() {
        try {
            element.setSpeedX(0);
            int index = 0;
            if (element.getManager().isMirrored())
                index = 1;
            element.swapImage(jumpIndexes[index]);
            element.setSpeedY(-15);
            element.setY(element.getY() - 2);
            Thread.sleep(1500);
            element.setSpeedY(0);
            element.swapImage(landingIndexes[index]);
            Thread.sleep(1500);
            element.setSpeedY(0);
            element.swapImage(bowserDefault); // TODO : phase 1 and phase 2 images should be different
            Thread.sleep(3000);
        } catch (Exception ignored) {}
    }
}
