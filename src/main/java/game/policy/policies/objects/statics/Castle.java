package game.policy.policies.objects.statics;

import core.objects.DynamicElement;
import core.objects.StaticElement;
import core.render.GameEngine;
import game.policy.D2SPolicy;

public class Castle extends D2SPolicy {
    @Override
    public boolean enforce(DynamicElement element1, StaticElement element2) {
        policyReference.marioLogic.calculateScore();
        manager.saveProgress();
        policyReference.soundSystem.pause();
        GameEngine.getInstance().closeGame();
        manager.showMenu();
        return true;
    }

    @Override
    public boolean isEnforceable(DynamicElement element1, StaticElement element2) {
        return element1.isLockedCharacter() && element2.getType().equals("Castle");
    }
}
