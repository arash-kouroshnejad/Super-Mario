package game.policy.policies.keys;

import game.policy.KeyPolicy;
import game.util.events.Event;
import game.util.events.EventQueue;
import game.util.events.EventType;

public class X extends KeyPolicy {
    @Override
    protected void press() {
        if (policyReference.saveReady)
            EventQueue.getInstance().publish(new Event(EventType.ModalTriggered, "SaveOptions"));
    }

    @Override
    protected void release() {

    }

    @Override
    public boolean isEnforceable(int keyCode) {
        return keyCode == X;
    }
}
