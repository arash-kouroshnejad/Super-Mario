package Game;

import Control.GameManager;
import Core.Objects.*;
import Core.Render.*;
import Core.Util.Loader;
import Core.Util.Logic;
import Game.Plugins.CoinThread;
import Game.Plugins.Gravity;
import Game.Plugins.PlantThread;

import java.awt.*;
import java.util.ArrayList;

public class MarioLogic extends Logic {

    Gravity gThread = new Gravity();
    private ArrayList<DynamicElement> dynamics;
    private ArrayList<StaticElement> statics;

    private boolean[] inTouch;
    private boolean jumping;
    private int minY;

    int mid;

    private long timeElapsed; // in milliseconds

    private GameStat currentGame;

    private final GameManager manager = GameManager.getInstance();

    private final ArrayList<ElementManager> animations = new ArrayList<>();

    public void init(Loader loader) {
        Layer layer = Layers.getInstance().getALL_LAYERS().get(1);
        dynamics = layer.getDynamicElements();
        DynamicElement character = dynamics.get(0);
        currentGame = manager.getCurrentGame();
        character = new DynamicElement(character.getX(), character.getY(), character.getWidth(), character.getHeight(),
                character.getSpeedX(), character.getSpeedY(), currentGame.getCharacter());
        dynamics.remove(0);
        dynamics.add(0, character);
        character.setLockedCharacter();
        lockedElement = character;
        ViewPort.getInstance().setLockedElement(character);
        GameEngine.getInstance().getMap().setLockedCharacter(character);
        character.setImages(loader.getSprite(currentGame.getCharacter()));
        layer = Layers.getInstance().getALL_LAYERS().get(2);
        statics = layer.getStaticElements();
        gThread.setElements(dynamics);
        gThread.apply();
        inTouch = new boolean[dynamics.size()];
        for (DynamicElement de : dynamics) {
            switch (de.getType()) {
                case "Plant" -> {
                    PlantThread pt = new PlantThread(de);
                    de.setManager(pt);
                    pt.start();
                    animations.add(pt);
                }
                case "Coin" -> {
                    CoinThread ct = new CoinThread(de);
                    de.setManager(ct);
                    ct.start();
                    animations.add(ct);
                }
            }
        }
        timeElapsed = currentGame.getTimeElapsed();
        GameEngine.getInstance().enableCustomPainting();
        mid = ViewPort.getInstance().getWidth() / 2;
    }

    @Override
    public void stop() {}

    @Override
    public void reset() {
        for (ElementManager manager : animations) {
            manager.pause();
        }
        animations.clear();
        gThread.remove();
        gThread = new Gravity();
        GameManager.getInstance().resetGame();
        setLockedElement(ViewPort.getInstance().getLockedElement());
        timeElapsed = 0;
    }

    private void killMario() {
        if (currentGame.getLives() > 0) {
            currentGame.setLevel(0);
            currentGame.setScore(0);
            currentGame.setLives(currentGame.getLives() - 1);
            reset();
        }
        else {
            GameEngine.getInstance().closeGame();
            calculateScore();
            currentGame.terminate();
            manager.saveProgress();
            manager.showMenu();
        }
    }

    private void calculateScore() {
        int points = 10 * currentGame.getCoinsEarned();
        points += 20 * currentGame.getLives();
        points += 15 * currentGame.getKillCount();
        updateTime();
        points += (80000 - currentGame.getTimeElapsed()) / 1000;
        currentGame.setScore(currentGame.getScore() + points);
    }

    private void updateTime() {
        timeElapsed += 10; // 100 fps --> 10 milliseconds per frame
        currentGame.setTimeElapsed(timeElapsed); // minus the respawn delay
    }

    @Override
    public void check() {
        updateTime();
        if (timeElapsed > 140000) {
            killMario();
            return;
        }
        // jump limit
        if (!(inTouch[0] && jumping) && Math.abs(lockedElement.getY() - minY) <= 5) {
            lockedElement.setSpeedY(Math.max(0, lockedElement.getSpeedY()));
        }
        inTouch[0] = false;
        int size = dynamics.size();
        for (int i = 0; i < size; i++) {
            DynamicElement element = dynamics.get(i);
            if (!element.getType().equals("Plant")) {
                // gravity
                if (element.getBounds().BOTTOM > ViewPort.getInstance().getHeight() - 20) {
                    element.setHidden(true);
                    ElementManager manager = element.getManager();
                    if (manager != null) {
                        manager.pause();
                    }
                    if (element.isLockedCharacter()) {
                        killMario();
                        return;
                    }
                }
                // collision with statics
                int size2 = statics.size();
                inTouch[i] = false;
                for (int j = 0; j < size2; j++) {
                    StaticElement element1 = statics.get(j);
                    if (element.collidesWith(element1)) {
                        switch (element1.getType()) {
                            case "FlagPole" -> {
                                calculateScore();
                                manager.saveProgress();
                                currentGame.setLevel(currentGame.getLevel() + 1);
                                reset();
                                return;
                            }

                            case "Castle" -> {
                                calculateScore();
                                manager.saveProgress();
                                GameEngine.getInstance().closeGame();
                                manager.showMenu();
                            }

                            case "Floor", "Pipe", "Stair", "Brick", "PowerUpBlock", "PipeExtension" -> {
                                if (element.collidesHorizontally(element1)) {
                                    // horizontal collision
                                    if (element.isLockedCharacter()) {
                                        if (element1.getX() >= element.getX()) {
                                            element.setSpeedX(Math.min(0, element.getSpeedX()));
                                            element.setX(element.getX() - 10);
                                        }
                                        else {
                                            element.setSpeedX((Math.max(0, element.getSpeedX())));
                                            element.setX(element.getX() + 10);
                                        }
                                    }
                                    else {
                                        element.setSpeedX(-element.getSpeedX());
                                    }
                                } else {
                                    // vertical collision
                                    if (element.isLockedCharacter()) {
                                        if (Math.abs(element.getBounds().TOP - element1.getBounds().BOTTOM) < 10) {
                                            element.setSpeedY(Math.max(0, element.getSpeedY()));
                                            if (element1.getType().equals("PowerUpBlock")) {
                                                // TODO : earn POWER UP
                                                element1.swapImage(1);
                                            }
                                        } else {
                                            inTouch[0] = true;
                                            element.setSpeedY(Math.min(0, element.getSpeedY()));
                                        }
                                    }
                                    else {
                                        if (!inTouch[i]) {
                                            inTouch[i] = true;
                                            element.setSpeedY(-element.getSpeedY());
                                        }
                                    }
                                }
                                /*if (element.getType().equals("Star")) {
                                    element.setSpeedY(-30); // ???
                                }*/
                            }
                        }
                    }
                }
                // collision with dynamics
                for (int j = i + 1 ; j < size; j++) {
                    DynamicElement element1 = dynamics.get(j);
                    if (element.collidesWith(element1)) {
                        // mario
                        if (element.isLockedCharacter()) {
                            element1.getManager().pause();
                            if (!element1.isHidden()) {
                                switch(element1.getType()) {
                                    case "Coin" :
                                        currentGame.earnCoin();
                                        element1.setHidden(true);
                                    case "Star":
                                        // TODO : earn POWER UP
                                        element1.setHidden(true);
                                        break;
                                    case "Plant":
                                        if (!element1.isHidden()) {
                                            killMario();
                                            return;
                                        }
                                    case "Goomba":
                                        if (!element1.isHidden()) {
                                            if (element.collidesHorizontally(element1)) {
                                                // TODO : TEST PROPER DEATH FUNCTIONALITY WITH CRAPPY COLLISION DETECTION
                                                killMario();
                                                return;
                                            }
                                            else {
                                                element1.setHidden(true);
                                                currentGame.killEnemy();
                                            }
                                        }
                                        break;
                                    case "Mushroom":
                                        // TODO : earn POWER UP
                                        element1.setHidden(true);
                                        break;
                                }
                            }
                        }
                        else {
                            element.setSpeedX(-element.getSpeedX());
                            element1.setSpeedX(-element1.getSpeedX());
                        }
                    }
                }
            }
            // TODO : custom Plant code
        }
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        mid = ViewPort.getInstance().getWidth() / 2;
        g.setFont(new Font("default", Font.BOLD, 16));
        g.drawString("TIME : " + timeElapsed,  mid - 300, 100);
        g.drawString("COINS : " + currentGame.getCoinsEarned(), mid + 100, 100);
        g.drawString("LIVES : " + currentGame.getLives(), mid + 300, 100);
    }

    @Override
    public void handleKeyPress(int keyCode) {
        switch (keyCode) {
            case 38 -> {
                jumping = true;
                if (inTouch[0]) {
                    minY = lockedElement.getY() - 160;
                    lockedElement.setSpeedY(6 * UP);
                }
            }
            case 40 -> lockedElement.setSpeedY(3 * DOWN);
            case 39 -> lockedElement.setSpeedX(RIGHT); // 3 * RIGHT
            case 37 -> lockedElement.setSpeedX(LEFT); // 3 * LEFT
            case 27 ->  {
                manager.saveProgress();
                GameEngine.getInstance().closeGame();
                manager.showMenu();
            }
        }
    }

    @Override
    public void handleKeyRelease(int keyCode) {
        switch (keyCode) {
            case 38, 40 -> {
                jumping = false;
                lockedElement.setSpeedY(0);
            }
            case 37, 39 -> lockedElement.setSpeedX(0);
        }
    }

    public GameStat getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(GameStat currentGame) {
        this.currentGame = currentGame;
    }
}
