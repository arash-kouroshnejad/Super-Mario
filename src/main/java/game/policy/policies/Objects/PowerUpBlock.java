package game.policy.policies.Objects;

import core.objects.DynamicElement;
import core.objects.StaticElement;
import game.util.events.Event;
import game.util.events.EventQueue;
import game.util.events.EventType;

public class PowerUpBlock extends BlockPolicy {

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
