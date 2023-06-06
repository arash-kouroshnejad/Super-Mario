package Game.Policy.Policies;

import Control.GameManager;
import Core.Objects.DynamicElement;
import Core.Objects.StaticElement;
import Core.Render.GameEngine;
import Game.Policy.D2SPolicy;

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
