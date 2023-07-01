package game.policy.policies.objects.statics;

import core.editor.LevelEditor;
import core.objects.DynamicElement;
import core.objects.StaticElement;
import game.model.CoinFilledBlocks;
import game.util.events.Event;
import game.util.events.EventQueue;
import game.util.events.EventType;

public class CoinFilledBlock extends BlockPolicy{
    @Override
    protected void customBlockBehaviour(DynamicElement element1, StaticElement element2) {
        if (CoinFilledBlocks.getInstance().use(element2)) {
            policyReference.currentGame.setCoinsEarned(policyReference.currentGame.getCoinsEarned() + 1);
            policyReference.soundSystem.play("CoinEarned", false, false);
        } else {
            EventQueue.getInstance().publish(new Event(EventType.GenerateElement,
                    element2.getX() + "x" + element2.getY() + ",FilledBlock"));
            LevelEditor.getInstance().removeElement(element2);
        }
    }

    @Override
    public boolean isEnforceable(DynamicElement element1, StaticElement element2) {
        return element2.getType().equals("CoinFilledBlock");
    }
}
