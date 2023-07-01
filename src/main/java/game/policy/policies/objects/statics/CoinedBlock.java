package game.policy.policies.objects.statics;

import core.editor.LevelEditor;
import core.objects.DynamicElement;
import core.objects.StaticElement;
import game.util.events.Event;
import game.util.events.EventQueue;
import game.util.events.EventType;

public class CoinedBlock extends BlockPolicy {
    @Override
    public void customBlockBehaviour(DynamicElement element1, StaticElement element2) {
        EventQueue.getInstance().publish(new Event(EventType.GenerateElement,
                element2.getX() + "x" + element2.getY() + ",Coin"));
        EventQueue.getInstance().publish(new Event(EventType.GenerateElement,
                element2.getX() + "x" + element2.getY() + ",Brick"));
        LevelEditor.getInstance().removeElement(element2);
    }

    @Override
    public boolean isEnforceable(DynamicElement element1, StaticElement element2) {
        return element2.getType().equals("CoinedBlock");
    }
}
