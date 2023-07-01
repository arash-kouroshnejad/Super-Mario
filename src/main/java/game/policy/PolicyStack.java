package game.policy;

import java.util.ArrayList;
import java.util.List;

public class PolicyStack {
    private static final PolicyStack instance = new PolicyStack();
    private PolicyStack() {}

    public static PolicyStack getInstance() {
        return instance;
    }

    public List<DynamicPolicy> dynamicPolicies = new ArrayList<>();
    public List<D2SPolicy> d2SPolicies = new ArrayList<>();
}
