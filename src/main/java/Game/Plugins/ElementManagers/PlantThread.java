package Game.Plugins.ElementManagers;

import Core.Objects.DynamicElement;
import Core.Objects.ElementManager;

public class PlantThread extends ElementManager {
    public PlantThread(DynamicElement plant) {
        super(plant, new Runnable() {
            @Override
            public void run() {
                try {
                    plant.setHidden(true);
                    Thread.sleep(1250);
                    plant.setHidden(false);
                    plant.setSpeedY(-1);
                    Thread.sleep(800);
                    plant.setSpeedY(0);
                    plant.swapImage(1);
                    Thread.sleep(800);
                    plant.swapImage(0);
                    plant.setSpeedY(1);
                    Thread.sleep(800);
                    plant.setSpeedY(0);
                } catch (Exception ignored) {}
            }
        });
    }
}
