package Game.Util;

import Core.Editor.MapCreator;
import Persistence.Config;

public class Creator extends MapCreator {
    public Creator() {
        super(Config.getInstance().getProperty("DefaultMapsDir", String.class));
    }

    @Override
    public boolean isDynamic(String type) {
        return false;
    }

    @Override
    public boolean isLocked(String type) {
        return false;
    }
}
