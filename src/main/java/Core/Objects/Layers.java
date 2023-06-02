package Core.Objects;


import java.util.concurrent.CopyOnWriteArrayList;

public class Layers {
    private final static Layers instance = new Layers();
    private Layers(){}
    private CopyOnWriteArrayList<Layer> ALL_LAYERS = new CopyOnWriteArrayList<>();

    public CopyOnWriteArrayList<Layer> getALL_LAYERS() {
        return ALL_LAYERS;
    }

    public void setALL_LAYERS(CopyOnWriteArrayList<Layer> ALL_LAYERS) {
        this.ALL_LAYERS = ALL_LAYERS;
    }

    public static Layers getInstance() {
        return instance;
    }

    public void addLayer(Layer layer) {
        ALL_LAYERS.add(layer);
    }
}
