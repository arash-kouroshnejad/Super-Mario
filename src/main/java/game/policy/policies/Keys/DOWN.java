package game.policy.policies.Keys;

import core.render.ViewPort;
import game.policy.KeyPolicy;
import persistence.Config;

import static game.util.Handlers.KeyToggled.generateSword;

public class DOWN extends KeyPolicy {

    protected void press() {
        if (policyReference.registeredKeys.contains(UP))
            generateSword(); // todo
        else {
            var mario = ViewPort.getInstance().getLockedElement();
            if (!mario.getType().equals("MiniMario") && !policyReference.crouching &&
                    !(policyReference.registeredKeys.contains(RIGHT) || policyReference.registeredKeys.contains(LEFT))) {
                policyReference.crouching = true;
                String type = mario.getType() + (mario.getManager().isMirrored() ? "Mirrored" : "") + "Crouching";
                mario.getManager().pause();
                mario.setHeight((int) (mario.getHeight() * 0.75));
                mario.setY(mario.getY() + mario.getHeight() / 3);
                mario.swapImage(Config.getInstance().getProperty(type, Integer.class)); // TODO : MOVE INTO ELEMENT FACTORY
                policyReference.registeredKeys.add(DOWN);
            }
        }
    }

    protected void release() {
        var mario = ViewPort.getInstance().getLockedElement();
        if (!mario.getType().equals("MiniMario") && policyReference.crouching) {
            policyReference.crouching = false;
            mario.setY(mario.getY() - mario.getHeight() / 3);
            mario.setHeight((mario.getHeight() * 4) / 3);
        }
        else if (policyReference.registeredKeys.contains(UP)) {
            // todo
        }
    }

    @Override
    public boolean isEnforceable(int keyCode) {
        return keyCode == DOWN;
    }
}
