package Game.Plugins.ElementManagers;

import Core.Editor.LevelEditor;
import Core.Objects.DynamicElement;
import Core.Objects.ElementManager;

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
