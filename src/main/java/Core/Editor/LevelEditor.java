package Core.Editor;

import Core.Objects.*;
import Core.Render.*;
import Core.Util.Loader;
import Core.Util.Logic;
import Core.Util.Semaphore;
import Persistence.Config;

import java.awt.*;
import java.util.ArrayList;
import java.util.Optional;

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

    private DynamicElement lastAdded;

    private Semaphore mutex = Semaphore.getMutex();

    public void init(MapLoader loader, Logic gameLogic, MapCreator creator) {
        this.loader = loader;
        this.creator = creator;
        this.gameLogic = gameLogic;
        GameEngine.getInstance().enableEditorMode();
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

    protected void insertAt(String type, int x, int y, int state, int layerIndex) {
        insertAt(type, x, y, state, spritesFrame.getSpeedX(), spritesFrame.getSpeedY(), layerIndex);
    }
    // static element insertion

    public void staticInsert(String type, int x, int y, int state, int layerIndex) {
        insertAt(type, x, y, state, 0, 0, layerIndex);
    }

    public void insertAt(String type, int x, int y, int state, int speedX, int speedY, int layerIndex) {
        mutex.acquire();
        // ugly code, layers is redundant really !!
        java.util.List<Layer> layers = this.layers.getALL_LAYERS();
        if (layers.size() <= layerIndex) {
            layers.add(new Layer(new ArrayList<StaticElement>(), new ArrayList<DynamicElement>(), layerIndex));
        }
        Layer layer = layers.get(layerIndex);
        Dimension d = loader.getDimension(type);
        if (loader.isDynamic(type)) {
            DynamicElement element = new DynamicElement(x, y, d.width, d.height, speedX, speedY,
                    type);
            if (loader.isLocked(type)) {
                element.setLockedCharacter();
                ViewPort.getInstance().setLockedElement(element);
                GameEngine.getInstance().getGameLogic().setLockedElement(element);
                layer.addDynamicElement(element, 0); // TODO : funky logic depends on the order fix it and restore level editor sanity!
            }
            else
                layer.addDynamicElement(element);
            element.setImages(loader.getSprite(type));
            element.swapImage(state);
            lastAdded = element;
        }
        else {
            StaticElement element = new StaticElement(x, y, d.width, d.height, type);
            layer.addStaticElement(element);
            element.setImages(loader.getSprite(type));
            element.swapImage(state);
        }
        mutex.release();
    }
    // dynamic element insertion

    public void attachManager(Class<? extends ElementManager> c) {
        mutex.acquire();
        try {
            var manager = c.getConstructor(DynamicElement.class).newInstance(lastAdded);
            lastAdded.setManager(manager);
            manager.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mutex.release();
    }

    public void removeElement(String type, int layerIndex) {
        mutex.acquire();
        java.util.List<Layer> layers = Layers.getInstance().getALL_LAYERS();
        if (layers.size() > layerIndex && layerIndex >= 0) {
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
        mutex.release();
    }

    public void removeElement(StaticElement element) {
        mutex.acquire();
        for (var layer : layers.getALL_LAYERS()) {
            if (element instanceof DynamicElement) {
                layer.getDynamicElements().remove((DynamicElement) element);
                if (((DynamicElement) element).getManager() != null)
                    ((DynamicElement) element).getManager().kill();
            }
            else
                layer.getStaticElements().remove(element);
        }
        mutex.release();
    }

    public Optional<DynamicElement> getDynamicElement(String type, int layerIndex, int index) { // TODO : integrate all these methods
        if (layers.getALL_LAYERS().size() > layerIndex) {
            var elements = layers.getALL_LAYERS().get(layerIndex).getDynamicElements();
            if (elements.size() > index && index != -1) {
                int current = 0;
                for (var element : elements) {
                    if (element.getType().equals(type))
                        current++;
                    if (current == index)
                        return Optional.of(element);
                }
                return Optional.empty();
            }
            else if (elements.size() != 0) {
                for (var element : elements)
                    if (element.getType().equals(type))
                        return Optional.of(element);
            }
        }
        return Optional.empty();
    }

    public Optional<StaticElement> getStaticElement(String type, int layerIndex, int index) {
        if (layers.getALL_LAYERS().size() > layerIndex) {
            var elements = layers.getALL_LAYERS().get(layerIndex).getStaticElements();
            if (elements.size() > index && index != -1) {
                int current = 0;
                for (var element : elements) {
                    if (element.getType().equals(type))
                        current++;
                    if (current == index)
                        return Optional.of(element);
                }
                return Optional.empty();
            }
            else if (elements.size() > 0)
                for (var element : elements)
                    if (element.getType().equals(type))
                        return Optional.of(element);
        }
        return Optional.empty();
    }

    public void setLoader(Loader loader) {
        this.loader = loader;
    }
}
