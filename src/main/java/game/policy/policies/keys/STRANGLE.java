package game.policy.policies.keys;

import control.GameManager;
import game.policy.KeyPolicy;

public class STRANGLE extends KeyPolicy {
    private int count;
    @Override
    public boolean isEnforceable(int keyCode) {
        return keyCode == RIGHT || keyCode == LEFT;
    }

    @Override
    protected void press() {

    }

    @Override
    protected void release() {
        count++;
        if (count == 10) {
            GameManager.getInstance().getBowserLogic().releaseMario();
        }
    }
}
