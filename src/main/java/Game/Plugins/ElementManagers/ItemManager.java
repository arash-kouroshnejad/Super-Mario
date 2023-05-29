package Game.Plugins.ElementManagers;

import Core.Objects.DynamicElement;
import Core.Objects.ElementManager;
import Core.Render.ViewPort;

public class ItemManager extends ElementManager {
    private final static int UP = -5;
    private final static int RIGHT = 1;
    public ItemManager(DynamicElement element) {
        super(element, new SpriteRotator(element));
    }

    protected record SpriteRotator(DynamicElement element) implements Runnable {
        @Override
            public void run() {
                try {
                    element.setSpeedY(UP);
                    Thread.sleep(200);
                    element.setSpeedY(0);
                    if (!element.getType().equals("Flower")) {
                        element.setSpeedX(RIGHT);
                        if (element.getType().equals("Star")) {
                            while (ViewPort.getInstance().inView(element)) {
                                if (element.getSpeedY() == 0) {
                                    element.setSpeedY(-UP);
                                    sleep(1000);
                                    element.setSpeedY(0);
                                    sleep(1000);
                                }
                            }
                        }
                    }
                    element.setHidden(true);
                } catch (Exception ignored) {}
            }
        }
}
