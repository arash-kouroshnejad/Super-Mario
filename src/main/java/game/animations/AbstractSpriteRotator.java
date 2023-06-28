package game.animations;

import core.objects.DynamicElement;
import persistence.Config;

public abstract class AbstractSpriteRotator extends AbstractAnimation {
    Config c = Config.getInstance();
    int[] indexes;
    int[] mirroredIndexes;
    protected boolean mirrored;
    protected int index;
    private final int sleep;

    protected AbstractSpriteRotator (DynamicElement element, int sleep, String reference) {
        super(element);
        String[] sprintIndexes = c.getProperty(element.getType() + reference).split(",");
        String[] mirroredSprintIndexes = c.getProperty(element.getType() + "Mirrored" + reference).split(",");
        this.indexes = new int[sprintIndexes.length];
        this.mirroredIndexes = new int[mirroredSprintIndexes.length];
        for (int i = 0; i < sprintIndexes.length; i++) {
            this.indexes[i] = Integer.parseInt(sprintIndexes[i]);
            this.mirroredIndexes[i] = Integer.parseInt(mirroredSprintIndexes[i]);
        }
        this.sleep = sleep;
    }
    @Override
    public void run() {
        try {
            updateIndex();
            if (mirrored)
                element.swapImage(mirroredIndexes[index]);
            else
                element.swapImage(indexes[index]);
            Thread.sleep(sleep);
        } catch (Exception ignored) {}
    }

    private void updateIndex() {
        if (mirrored) {
            if (index == mirroredIndexes.length - 1) {
                index = 0;
            } else {
                index++;
            }
        } else {
            if(index == indexes.length - 1) {
                index = 0;
            } else {
                index++;
            }
        }
    }

    public void reset() {index = 0;}
}
