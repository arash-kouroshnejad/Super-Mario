package Core.Objects;

import Core.Render.*;

import java.util.ArrayList;

public class Map {
    private static int currentId;
    private final ArrayList<Layer> LAYERS;
    private final int ID;
    private DynamicElement lockedCharacter;
    public void setLockedCharacter(DynamicElement lockedCharacter) {
        this.lockedCharacter = lockedCharacter;
    }

    public Map(ArrayList<Layer> LAYERS, int ID) {
        this.LAYERS = LAYERS;
        this.ID = ID;
    }

    public ArrayList<Layer> getLAYERS() {
        return LAYERS;
    }
    public int getID() {
        return ID;
    }
    public void init() {
        Layers.getInstance().setALL_LAYERS(LAYERS);
        ViewPort.getInstance().setLockedElement(lockedCharacter);
        GameEngine.getInstance().setMap(this);
        currentId = ID;
    }

    public static int getCurrentId() {
        return currentId;
    }
}
