package Core.Editor;

import Core.Objects.*;
import Core.Render.*;
import Core.Util.Loader;
import Core.Util.Logic;
import Persistence.Config;

import java.awt.*;
import java.util.ArrayList;

public class LevelEditor extends GameEngine {
    private final static LevelEditor instance = new LevelEditor();
    protected LevelEditor(){}

    public static LevelEditor getInstance() {
        return instance;
    }

    private Loader loader;

    private SpritePicker spritesFrame;

    private Logic gameLogic;

    private MapCreator creator;

    public void init(MapLoader loader, Logic gameLogic, MapCreator creator) {
        this.loader = loader;
        this.creator = creator;
        this.gameLogic = gameLogic;
        loader.loadMap(Config.getInstance().getProperty("EditorInputMap", Integer.class));
        gameLogic.setLockedElement(ViewPort.getInstance().getLockedElement());
        GameEngine.getInstance().init(gameLogic);
        spritesFrame = new SpritePicker();
    }

    public void createMap() {
        int id = Config.getInstance().getProperty("EditorOutputMap", Integer.class);
        Map map = new Map(Layers.getInstance().getALL_LAYERS(), id);
        creator.saveMap(map, id);
    }

    public Loader getLoader() {
        return loader;
    }

    public void insertAt(String type, int x, int y, int state, int layerIndex) {
        // ugly code, layers is redundant really !!
        ArrayList<Layer> layers = this.layers.getALL_LAYERS();
        if (layers.size() <= layerIndex) {
            layers.add(new Layer(new ArrayList<StaticElement>(), new ArrayList<DynamicElement>(), layerIndex));
        }
        Layer layer = layers.get(layerIndex);
        Dimension d = loader.getDimension(type);
        if (loader.isDynamic(type)) {
            DynamicElement element = new DynamicElement(x, y, d.width, d.height, spritesFrame.getSpeedX(), spritesFrame.getSpeedY(),
                    type);
            layer.addDynamicElement(element);
            if (loader.isLocked(type)) {
                element.setLockedCharacter();
                ViewPort.getInstance().setLockedElement(element);
                GameEngine.getInstance().getGameLogic().setLockedElement(element);
                gameLogic.setLockedElement(element);
            }
            element.setImages(loader.getSprite(type));
            element.swapImage(state);
        }
        else {
            StaticElement element = new StaticElement(x, y, d.width, d.height, type);
            layer.addStaticElement(element);
            element.setImages(loader.getSprite(type));
            element.swapImage(state);
        }
    }

    public void removeElement(String type, int layerIndex) {
        ArrayList<Layer> layers = Layers.getInstance().getALL_LAYERS();
        if (layers.size() >= layerIndex && layerIndex >= 0) {
            Layer l = layers.get(layerIndex);
            for (StaticElement e : l.getStaticElements()) {
                if (e.getType().equals(type)) {
                    l.getStaticElements().remove(e);
                    return;
                }
            }
            for (DynamicElement e : l.getDynamicElements()) {
                if (e.getType().equals(type)) {
                    l.getDynamicElements().remove(e);
                    return;
                }
            }
        }
    }

    public void setLoader(Loader loader) {
        this.loader = loader;
    }
}
