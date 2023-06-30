package game.util.loaders;

import core.editor.MapCreator;
import persistence.Config;

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
