package Game.Policy.Policies;

import Core.Objects.DynamicElement;
import Core.Objects.StaticElement;
import Game.Util.Events.Event;
import Game.Util.Events.EventQueue;
import Game.Util.Events.EventType;

public class PowerUpBlock extends BlockPolicy{

    @Override
    public boolean isEnforceable(DynamicElement element1, StaticElement element2) {
        return element1.isLockedCharacter() && element2.getType().equals("PowerUpBlock");
    }

    @Override
    protected void customBlockBehaviour(DynamicElement element1) {
        if (!policyReference.activatedBlocks.contains(element1)) {
            policyReference.activatedBlocks.add(element1);
            EventQueue.getInstance().publish(new Event(EventType.GenerateElement,
                    element1.getX() + "x" + (element1.getY() - 18) + ",Item"));
            element1.swapImage(1);
        }
    }
}
