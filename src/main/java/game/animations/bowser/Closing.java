package game.animations.bowser;

import core.objects.DynamicElement;
import game.animations.Sprint;

public class Closing extends BowserAnimation {
    private final Sprint sprint;

    public Closing(DynamicElement element) {
        super(element);
        sprint = Sprint.getSprint(element, 100);
    }
    // TODO : implement proper mirroring functionality
    @Override
    public void run() {
        faceMario();
        element.setSpeedX(element.getManager().isMirrored() ? -1 : 1);
        sprint.run();
    }
}
