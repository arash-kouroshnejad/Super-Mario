package game.policy.policies.Keys;

import game.policy.KeyPolicy;
import game.util.events.Event;
import game.util.events.EventQueue;
import game.util.events.EventType;

public class SPACE extends KeyPolicy {
    @Override
    protected void press() {
        EventQueue.getInstance().publish(new Event(EventType.GenerateElement, "0x0,Bullet"));
    }

    @Override
    protected void release() {

    }

    @Override
    public boolean isEnforceable(int keyCode) {
        return keyCode == SPACE;
    }
}
