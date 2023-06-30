package game.animations.bowser;

import core.objects.DynamicElement;
import core.render.ViewPort;
import game.animations.AbstractSpriteRotator;
import persistence.Config;

public class GrabAttack extends BowserAnimation {

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
        var mario = ViewPort.getInstance().getLockedElement();
        if (!hasGrabbed) {
            if (element.getManager().isMirrored())
                element.swapImage(defaultIndexes[1]);
            else
                element.swapImage(defaultIndexes[0]);
            if (element.collidesWith(mario) && element.isLevelWith(mario)) {// TODO : maybe there is a better condition to check
                hasGrabbed = true;
                logic.captureMario();
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
        hasGrabbed = false;
        ViewPort.getInstance().getLockedElement().setHidden(false);
        element.swapImage(defaultIndexes[element.getManager().isMirrored() ? 1 : 0]);
        try {
            Thread.sleep(4000);
        } catch (Exception ignored) {}
    }
}
