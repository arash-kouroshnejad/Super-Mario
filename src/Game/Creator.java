package Game;

import Core.Editor.MapCreator;

public class Creator extends MapCreator {
    public Creator() {
        super("./src/Resources/Maps/");
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
