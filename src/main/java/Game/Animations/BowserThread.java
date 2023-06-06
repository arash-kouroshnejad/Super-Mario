package Game.Animations;

import Core.Objects.DynamicElement;
import Core.Objects.ElementManager;
import Game.Animations.BowserAnimations.GrabAttack;

public class BowserThread extends ElementManager {
    public BowserThread (DynamicElement element) {
        super(element, new GrabAttack(element));
    }
}
