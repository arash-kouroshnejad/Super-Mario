package game.animations.mario;

import core.objects.DynamicElement;
import core.objects.ElementManager;
import game.animations.Sprint;
import persistence.Config;

import java.util.HashMap;

public class MarioThread extends ElementManager {
    private final HashMap<String, Integer> defaultStates = new HashMap<>();
    public MarioThread(DynamicElement mario) {
        super(mario, Sprint.getSprint(mario, 100));
        var config = Config.getInstance();
        String[] modes = config.getProperty("MarioModes").split(",");
        for (String mode : modes) {
            defaultStates.put(mode, config.getProperty(mode + "Default", Integer.class));
            defaultStates.put(mode + "Mirrored", config.getProperty(mode + "MirroredDefault", Integer.class));
        }
    }

    public void resetState() {
        element.swapImage(defaultStates.get(element.getType() + ((mirrored) ? "Mirrored" : "")));
    }

    public void setMirrored(boolean mirrored) {
        super.setMirrored(mirrored);
        Sprint.setMirrored(element, mirrored);
    }

    public boolean isMirrored() {
        return mirrored;
    }

    public void kill() {
        super.kill();
        Sprint.remove(element);
    }
}
