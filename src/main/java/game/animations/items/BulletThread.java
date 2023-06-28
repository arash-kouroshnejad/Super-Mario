package game.animations.items;

import core.editor.LevelEditor;
import core.objects.DynamicElement;
import core.objects.ElementManager;

public class BulletThread extends ElementManager {
    public BulletThread(DynamicElement element) {
        super(element, () -> {
            try {
                Thread.sleep(2000);
                LevelEditor.getInstance().removeElement(element);

            } catch (Exception ignored) {}
        });
    }
}
