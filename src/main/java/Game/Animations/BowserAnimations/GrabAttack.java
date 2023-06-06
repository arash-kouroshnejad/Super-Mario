package Game.Animations.BowserAnimations;

import Core.Objects.DynamicElement;
import Core.Render.ViewPort;
import Game.Animations.AbstractAnimation;
import Game.Animations.AbstractSpriteRotator;
import Persistence.Config;

public class GrabAttack extends AbstractAnimation {

    private final Rotator rotator;
    private boolean hasGrabbed;
    private final int[] defaultIndexes = new int[2];

    public GrabAttack(DynamicElement element) {
        super(element);
        this.rotator = new Rotator(element, 100);
        Config c = Config.getInstance();
        defaultIndexes[0] = c.getProperty("BowserGrabAttack", Integer.class);
        defaultIndexes[1] = c.getProperty("BowserMirroredGrabAttack", Integer.class);
    }

    @Override
    public void run() {
        // TODO : run and grab mario
        // TODO : then ...
        if (!hasGrabbed) {
            if (element.getManager().isMirrored())
                element.swapImage(defaultIndexes[1]);
            else
                element.swapImage(defaultIndexes[0]);
            if (element.collidesHorizontally(ViewPort.getInstance().getLockedElement())) {// TODO : maybe there is a better condition to check
                hasGrabbed = true;
                ViewPort.getInstance().getLockedElement().setHidden(true);
            }
        }
        else
            rotator.run();
    }

    private static class Rotator extends AbstractSpriteRotator {
        public Rotator(DynamicElement element, int sleep) {
            super(element, sleep, "GrabAttackIndexes");
        }

        public void setMirrored(boolean mirrored) {
            this.mirrored = mirrored;
        }
    }

    @Override
    public void reset() {
        ViewPort.getInstance().getLockedElement().setHidden(false);
        element.swapImage(defaultIndexes[element.getManager().isMirrored() ? 1 : 0]);
    }
}
