package Core.Render;

import Core.Objects.*;
import Core.Util.Logic;
import Core.Util.Semaphore;

import java.awt.*;
import java.util.ArrayList;


public class GameEngine {
    private final static GameEngine instance = new GameEngine();
    GameFrame gameFrame;

    protected GameEngine() {}

    private boolean started;

    private boolean editorMode;

    private final ViewPort viewPort = ViewPort.getInstance();

    public static GameEngine getInstance() {
        return instance;
    }


    protected Layers layers = Layers.getInstance();

    private Map map;

    private Semaphore mutex = Semaphore.getMutex();

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
        animationAgent = new Animation(150);
        animationAgent.start();
    }

    public void startGame() {
        started = true;
    }

    public void closeGame() {
        started = false;
        gameFrame.setVisible(false);
        animationAgent.kill();
        customPainting = false;
        editorMode = false;
    }

    public void enableEditorMode() {
        editorMode = true;
    }

    public void pauseAnimation() {
        animationAgent.pause();
        gameLogic.stop();
    }

    public void resumeAnimation() {
        animationAgent.restart();
        gameLogic.resume();
    }

    public void paint(Graphics g) {
        mutex.acquire();
        java.util.List<Layer> allLayers = layers.getALL_LAYERS();
        if (allLayers != null) {
            if (started) {
                gameLogic.check();
            }
            for (Layer layer : allLayers) {
                for (var element : layer.getStaticElements()) {
                    if (viewPort.inView(element) && !element.isHidden()) {
                        g.drawImage(element.getImage(), element.getX() - viewPort.getX(), element.getY() - viewPort.getY(),
                                element.getWidth(), element.getHeight(), gameFrame);
                    }
                }
                for (var element : layer.getDynamicElements()) {
                    if (viewPort.inView(element) && !element.isHidden()) {
                        g.drawImage(element.getImage(), element.getX() - viewPort.getX(), element.getY() - viewPort.getY(),
                                element.getWidth(), element.getHeight(), gameFrame);
                        // Logic code goes here
                        if (element.isLockedCharacter() || !editorMode)
                            element.move();
                    }
                }
            }
            if (started && customPainting) {
                gameLogic.paint(g);
            }
        }
        mutex.release();
    }
    protected void resize(Dimension dim) {
        viewPort.setWidth(dim.width);
        viewPort.setHeight(dim.height);
    }
    public void enableCustomPainting() {
        customPainting = true;
    }
}
