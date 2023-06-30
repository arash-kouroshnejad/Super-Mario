package game.policy.policies.Keys;

import core.render.ViewPort;
import game.model.Mario;
import game.policy.KeyPolicy;
import persistence.Config;


public class UP extends KeyPolicy {
    @Override
    protected void press() {
        if (policyReference.registeredKeys.contains(UP)) {
            var timers = policyReference.timers;
            if (!timers.containsKey("SwordTimer"))
                timers.put("SwordTimer", Mario.getInstance().getSword().generateTimer());
        }
        else {
            var mario = ViewPort.getInstance().getLockedElement();
            Config c = Config.getInstance();
            if (mario.getManager().isMirrored()) {
                mario.swapImage(c.getProperty(mario.getType() + "MirroredJumping", Integer.class));
            } else {
                mario.swapImage(c.getProperty(mario.getType() + "Jumping", Integer.class));
            }
        }
    }

    @Override
    protected void release() {
        var mario = ViewPort.getInstance().getLockedElement();
        mario.setSpeedY(0);
        var timers = policyReference.timers;
        if (timers.containsKey("SwordTimer")) {
            Mario.getInstance().getSword().cancel();
            policyReference.timers.remove("SwordTimer");
        }
    }

    @Override
    public boolean isEnforceable(int keyCode) {
        return keyCode == UP;
    }
}
