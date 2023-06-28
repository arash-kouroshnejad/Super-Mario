package game.policy.policies.Objects;

import core.objects.DynamicElement;
import core.objects.StaticElement;
import core.render.GameEngine;
import game.policy.D2SPolicy;

public class CastlePolicy extends D2SPolicy {
    @Override
    public boolean enforce(DynamicElement element1, StaticElement element2) {
        gameLogic.calculateScore();
        manager.saveProgress();
        soundSystem.pause();
        GameEngine.getInstance().closeGame();
        manager.showMenu();
        return true;
    }

    @Override
    public boolean isEnforceable(DynamicElement element1, StaticElement element2) {
        return element1.isLockedCharacter() && element2.getType().equals("Castle");
    }
}
