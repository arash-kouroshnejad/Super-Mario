package game.policy.policies.keys;

import core.render.ViewPort;
import game.policy.KeyPolicy;

public class RIGHT extends KeyPolicy {
    @Override
    protected void press() {
        var mario = ViewPort.getInstance().getLockedElement();
        mario.setSpeedX(2);
        mario.getManager().setMirrored(false);
        mario.getManager().restart();
    }

    @Override
    protected void release() {
        var mario = ViewPort.getInstance().getLockedElement();
        mario.setSpeedX(0);
        mario.getManager().pause();
        mario.getManager().resetState();
    }

    @Override
    public boolean isEnforceable(int keyCode) {
        return keyCode == RIGHT;
    }
}
