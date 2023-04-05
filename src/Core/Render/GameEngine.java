package Core.Render;

import Core.Objects.*;
import Core.Util.Logic;

import java.awt.*;
import java.util.ArrayList;

public class GameEngine {
    private final static GameEngine instance = new GameEngine();
    GameFrame gameFrame;
    protected GameEngine() {}

    private boolean started;

    private final ViewPort viewPort = ViewPort.getInstance();

    public static GameEngine getInstance() {
        return instance;
    }

    // private Graphics graphics;

    protected Layers layers = Layers.getInstance();

    private Map map;

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    private Logic gameLogic;

    public Logic getGameLogic() {
        return gameLogic;
    }

    private Animation animationAgent;

    private boolean customPainting;

    /*public void setGraphics(Graphics graphics) {
        this.graphics = graphics;
    }*/

    public void init(Logic gameLogic) {
        this.gameLogic = gameLogic;
        gameFrame = new GameFrame();
        viewPort.setFrame(gameFrame);
        animationAgent = new Animation(100);
        animationAgent.start();
    }

    public void startGame() {
        started = true;
    }

    public void closeGame() {
        started = false;
        gameFrame.setVisible(false);
        animationAgent.pause();
        customPainting = false;
    }

    public void pauseAnimation() {
        animationAgent.pause();
    }

    public void resumeAnimation() {
        animationAgent.start();
    }
    public void paint(Graphics g) {
        ArrayList<Layer> allLayers = layers.getALL_LAYERS();
        if (allLayers != null) {
            int layerCount = allLayers.size();
            for (int  j= 0;j<layerCount;j++){
                Layer layer = allLayers.get(j);
                ArrayList<StaticElement> staticElements = layer.getStaticElements();
                int size = staticElements.size();
                for( int i=0;i<size;i++) {
                    StaticElement element = staticElements.get(i);
                    if (viewPort.inView(element) && !element.isHidden()) {
                        g.drawImage(element.getImage(), element.getX() - viewPort.getX(), element.getY() - viewPort.getY(), element.getWidth(), element.getHeight(), gameFrame);
                    }
                }
                ArrayList<DynamicElement> dynamicElements = layer.getDynamicElements();
                size = dynamicElements.size();
                for (int i=0;i<size;i++) {
                    DynamicElement element1 = dynamicElements.get(i);
                    if (viewPort.inView(element1) && !element1.isHidden()) {
                        g.drawImage(element1.getImage(), element1.getX() - viewPort.getX(), element1.getY() - viewPort.getY(), element1.getWidth(), element1.getHeight(), gameFrame);
                        // Logic code goes here
                        if (started) {
                            gameLogic.check();
                        }
                        element1.move();
                    }
                }
            }
            if (started && customPainting) {
                gameLogic.paint(g);
            }
        }
    }
    protected void resize(Dimension dim) {
        viewPort.setWidth(dim.width);
        viewPort.setHeight(dim.height);
    }
    public void enableCustomPainting() {
        customPainting = true;
    }
}
