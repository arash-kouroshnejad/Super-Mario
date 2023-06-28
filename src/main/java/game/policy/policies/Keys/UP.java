package game.policy.policies.Keys;

import core.render.ViewPort;
import game.policy.KeyPolicy;
import persistence.Config;

import static game.util.Handlers.KeyToggled.generateSword;

public class UP extends KeyPolicy {
    @Override
    protected void press() {
        if (policyReference.registeredKeys.contains(DOWN))
            generateSword(); // todo
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
        if (policyReference.registeredKeys.contains(DOWN)) {
            // todo
        }
        else
            mario.setSpeedY(0);
    }

    @Override
    public boolean isEnforceable(int keyCode) {
        return keyCode == UP;
    }
}
