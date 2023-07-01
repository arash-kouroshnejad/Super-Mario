package game.util.handlers;


import game.policy.KeyStack;
import game.util.events.Event;
import game.util.events.EventHandler;


public class KeyToggled extends EventHandler {

    public KeyToggled(Event event) {
        super(event);
    }

    public void run() {
        int keyCode = Integer.parseInt(event.attribute().split(",")[0]);
        boolean pressed = event.attribute().split(",")[1].equals("Press");
        var stack = KeyStack.getInstance().getKeyPolicies();
        for (var policy : stack)
            if (policy.isEnforceable(keyCode)) {
                policy.enforce(keyCode, pressed);
                return;
            }
    }

    public static void generateSword () {
        /*if (GameManager.getInstance().getCoins() >= 3) {
            GameManager.getInstance().getCurrentGame().setCoinsEarned(GameManager.getInstance().getCoins() - 3);
            timer = new Timer(2, () -> {EventQueue.getInstance().publish(new Event(EventType.GenerateElement,
                    "0x0,Sword"));});
        }*/
    }
}
