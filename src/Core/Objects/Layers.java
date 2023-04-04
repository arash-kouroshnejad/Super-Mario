package Core.Objects;

import java.util.ArrayList;

public class Layers {
    private final static Layers instance = new Layers();
    private Layers(){}
    private ArrayList<Layer> ALL_LAYERS = new ArrayList<>();

    public ArrayList<Layer> getALL_LAYERS() {
        return ALL_LAYERS;
    }

    public void setALL_LAYERS(ArrayList<Layer> ALL_LAYERS) {
        this.ALL_LAYERS = ALL_LAYERS;
    }

    public static Layers getInstance() {
        return instance;
    }
    public void addLayer(Layer layer) {
        ALL_LAYERS.add(layer);
    }
}
