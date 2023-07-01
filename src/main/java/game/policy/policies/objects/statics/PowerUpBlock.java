package game.policy.policies.objects.statics;

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
    protected void customBlockBehaviour(DynamicElement element1, StaticElement element2) {
        if (!policyReference.activatedBlocks.contains(element2)) {
            policyReference.activatedBlocks.add(element2);
            EventQueue.getInstance().publish(new Event(EventType.GenerateElement,
                    element2.getX() + "x" + (element2.getY() - 18) + ",Item"));
            element2.swapImage(1);
        }
    }
}
