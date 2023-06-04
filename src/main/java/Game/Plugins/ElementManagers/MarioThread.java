package Game.Plugins.ElementManagers;

import Core.Objects.DynamicElement;
import Core.Objects.ElementManager;
import Persistence.Config;

import java.util.HashMap;

public class MarioThread extends ElementManager {
    private boolean mirrored;
    private final HashMap<String, Integer> defaultStates = new HashMap<>();
    public MarioThread(DynamicElement mario) {
        super(mario, new Sprint(mario));
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
        this.mirrored = mirrored;
        Sprint.setMirrored(mirrored);
    }

    public boolean isMirrored() {
        return mirrored;
    }

    private static class Sprint implements Runnable {
        Config c = Config.getInstance();
        int[] sprintIndexes;
        int[] mirroredSprintIndexes;
        final DynamicElement element;
        private static boolean mirrored;
        private int index;
        public Sprint (DynamicElement element) {
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
        }
        @Override
        public void run() {
            try {
                updateIndex();
                if (mirrored)
                    element.swapImage(mirroredSprintIndexes[index]);
                else
                    element.swapImage(sprintIndexes[index]);
                Thread.sleep(100);
            } catch (Exception ignored) {}
        }

        public static void setMirrored(boolean mirrored) {
            Sprint.mirrored = mirrored;
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
}
