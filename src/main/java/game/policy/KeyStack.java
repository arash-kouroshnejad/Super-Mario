package game.policy;

import java.util.ArrayList;
import java.util.List;

public class KeyStack {
    private static final KeyStack instance = new KeyStack();

    private KeyStack() {}

    public static KeyStack getInstance() {return instance;}

    private final List<KeyPolicy> keyPolicies = new ArrayList<>();

    private final List<KeyPolicy> defaultStack = new ArrayList<>();

    public List<KeyPolicy> getKeyPolicies() {
        return keyPolicies;
    }

    public synchronized void disableKeys() {
        defaultStack.clear();
        defaultStack.addAll(keyPolicies);
        keyPolicies.clear();
    }

    public synchronized void resetKeys() {
        keyPolicies.clear();
        keyPolicies.addAll(defaultStack);
    }
}
