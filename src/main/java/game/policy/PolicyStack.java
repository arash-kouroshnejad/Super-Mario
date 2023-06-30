package game.policy;

import java.util.ArrayList;
import java.util.List;

public class PolicyStack {
    private static final PolicyStack instance = new PolicyStack();

    private PolicyStack() {}

    public static PolicyStack getInstance() {return instance;}

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
