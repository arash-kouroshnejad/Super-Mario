package game.model;

import core.objects.StaticElement;


import java.util.HashMap;
import java.util.Map;

public class CoinFilledBlocks {
    private static final CoinFilledBlocks instance = new CoinFilledBlocks();
    private CoinFilledBlocks () {}
    public static CoinFilledBlocks getInstance() {
        return instance;
    }
    private final Map<StaticElement, Integer> capacities = new HashMap<>();

    public void add(StaticElement block) {
        capacities.put(block, 5);
    }
    public boolean isEmptyOrAdd(StaticElement block) {
        if (capacities.containsKey(block)) {
            return capacities.get(block) == 0;
        } else {
            add(block);
            return false;
        }
    }

    public boolean use (StaticElement block) {
        if (!isEmptyOrAdd(block)) {
            capacities.put(block, capacities.get(block) - 1);
            return true;
        } else {
            return false;
        }
    }
}
