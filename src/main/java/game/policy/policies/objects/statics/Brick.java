package game.policy.policies.objects.statics;

import core.editor.LevelEditor;
import core.objects.DynamicElement;
import core.objects.StaticElement;

public class Brick extends BlockPolicy{
    @Override
    protected void customBlockBehaviour(DynamicElement element1, StaticElement element2) {
        policyReference.currentGame.setScore(policyReference.currentGame.getScore() + 1);
        LevelEditor.getInstance().removeElement(element2);
    }

    @Override
    public boolean isEnforceable(DynamicElement element1, StaticElement element2) {
        return element2.getType().equals("Brick");
    }
}
