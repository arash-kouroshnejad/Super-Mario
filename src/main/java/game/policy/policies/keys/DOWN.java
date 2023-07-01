package game.policy.policies.keys;

import core.render.ViewPort;
import game.model.Mario;
import game.policy.KeyPolicy;
import persistence.Config;



public class DOWN extends KeyPolicy {

    protected void press() {
        if (policyReference.registeredKeys.contains(UP)) {
            var timers = policyReference.timers;
            if (!timers.containsKey("SwordTimer"))
                timers.put("SwordTimer", Mario.getInstance().getSword().generateTimer());
        }
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
        var timers = policyReference.timers;
        if (timers.containsKey("SwordTimer")) {
            Mario.getInstance().getSword().cancel();
            policyReference.timers.remove("SwordTimer");
        }
    }

    @Override
    public boolean isEnforceable(int keyCode) {
        return keyCode == DOWN;
    }
}
