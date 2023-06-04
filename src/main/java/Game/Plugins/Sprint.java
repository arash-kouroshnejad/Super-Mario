package Game.Plugins;

import Core.Objects.DynamicElement;
import Persistence.Config;

import java.util.HashMap;
import java.util.Map;

public class Sprint implements Runnable {
    private static final Map<DynamicElement, Sprint> managers = new HashMap<>();
    Config c = Config.getInstance();
    int[] sprintIndexes;
    int[] mirroredSprintIndexes;
    final DynamicElement element;
    private boolean mirrored;
    private int index;
    private final int sleep;
    public Sprint (DynamicElement element, int sleep) {
        managers.put(element, this);
        this.element = element;
        String[] sprintIndexes = new String[0];
        try {
            sprintIndexes = c.getProperty(element.getType() + "SprintIndexes").split(",");
        } catch (Exception e) {
            System.out.println("Error in reading sprint indexes for : " + element.getType() + "SprintIndexes");
        }
        String[] mirroredSprintIndexes = c.getProperty(element.getType() + "MirroredSprintIndexes").split(",");
        this.sprintIndexes = new int[sprintIndexes.length];
        this.mirroredSprintIndexes = new int[mirroredSprintIndexes.length];
        for (int i = 0; i < sprintIndexes.length; i++) {
            this.sprintIndexes[i] = Integer.parseInt(sprintIndexes[i]);
            this.mirroredSprintIndexes[i] = Integer.parseInt(mirroredSprintIndexes[i]);
        }
        this.sleep = sleep;
    }

    @Override
    public void run() {
        try {
            updateIndex();
            if (mirrored)
                element.swapImage(mirroredSprintIndexes[index]);
            else
                element.swapImage(sprintIndexes[index]);
            Thread.sleep(sleep);
        } catch (Exception ignored) {}
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

    private void updateIndex() {
        if (mirrored) {
            if (index == mirroredSprintIndexes.length - 1) {
                index = 0;
            } else {
                index++;
            }
        } else {
            if(index == sprintIndexes.length - 1) {
                index = 0;
            } else {
                index++;
            }
        }
    }
}
