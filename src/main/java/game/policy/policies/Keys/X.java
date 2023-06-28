package game.policy.policies.Keys;

import game.policy.KeyPolicy;
import game.util.Events.Event;
import game.util.Events.EventQueue;
import game.util.Events.EventType;

public class X extends KeyPolicy {
    @Override
    protected void press() {
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
