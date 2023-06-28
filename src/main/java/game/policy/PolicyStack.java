package game.policy;

import java.util.ArrayList;
import java.util.List;

public class PolicyStack {
    private static final PolicyStack instance = new PolicyStack();

    private PolicyStack() {}

    public static PolicyStack getInstance() {return instance;}

    private final List<KeyPolicy> keyPolicies = new ArrayList<>();

    public List<KeyPolicy> getKeyPolicies() {
        return keyPolicies;
    }
}
