package Game.Animations;

import Core.Objects.DynamicElement;


import java.util.HashMap;
import java.util.Map;

public class Sprint extends AbstractSpriteRotator {
    private static final Map<DynamicElement, Sprint> managers = new HashMap<>();

    private Sprint (DynamicElement element, int sleep) {
        super(element, sleep, "SprintIndexes");
        managers.put(element, this);
    }

    public static void setMirrored(DynamicElement element, boolean mirrored) {
        managers.get(element).mirrored = mirrored;
    }

    public static void remove(DynamicElement element) {
        managers.remove(element);
    }

    public static Sprint getSprint(DynamicElement element, int sleep) {
        return managers.getOrDefault(element, new Sprint(element, sleep));
    }
}
