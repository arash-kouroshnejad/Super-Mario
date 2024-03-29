package Core.Editor;

import Core.Objects.*;
import Core.Render.*;
import Core.Util.Loader;
import Core.Util.Logic;

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
        loader.loadMap(1);
        gameLogic.setLockedElement(ViewPort.getInstance().getLockedElement());
        GameEngine.getInstance().init(gameLogic);
        spritesFrame = new SpritePicker();
    }

    public void createMap() {
        Map map = new Map(Layers.getInstance().getALL_LAYERS(), 1);
        creator.saveMap(map, 1); // TODO : bring level editor back up again
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
}
