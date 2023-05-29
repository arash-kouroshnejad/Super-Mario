package Game;

import Control.GameManager;
import Core.Editor.LevelEditor;
import Core.Objects.*;
import Core.Render.*;
import Core.Util.Loader;
import Core.Util.Logic;
import Game.Model.GameStat;
import Game.Plugins.ElementManagers.CoinThread;
import Game.Plugins.ElementManagers.MarioThread;
import Game.Plugins.ElementManagers.PlantThread;
import Game.Util.Events.Event;
import Game.Util.Events.EventQueue;
import Game.Util.Events.EventType;
import Game.Plugins.Gravity;
import Game.Plugins.SoundQueue;
import Persistence.Config;


import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class MarioLogic extends Logic {

    Gravity gThread = new Gravity();
    private ArrayList<DynamicElement> dynamics;
    private ArrayList<StaticElement> statics;
    private boolean[] inTouch; // networked marios jump control ?
    private boolean jumping;
    private int minY;
    int mid;
    private long timeElapsed; // in milliseconds
    private GameStat currentGame;
    private final GameManager manager = GameManager.getInstance();
    private final ArrayList<ElementManager> animations = new ArrayList<>();
    private final SoundQueue soundSystem = SoundQueue.getInstance();
    private boolean saveReady;
    private int checkPointsSaved;
    private final HashMap<String, String[]> modalTypes = new HashMap<>();
    private final HashSet<StaticElement> activatedBlocks = new HashSet<>();

    public void init(Loader loader) {
        soundSystem.init(this);
        soundSystem.play("Background", true, true);
        getModalTypes();
        Layer layer = Layers.getInstance().getALL_LAYERS().get(1);
        dynamics = layer.getDynamicElements();
        DynamicElement character = dynamics.get(0);
        currentGame = manager.getCurrentGame();
        lockedElement = ViewPort.getInstance().getLockedElement();
        /*character = new DynamicElement(character.getX(), character.getY() + loader.getDimension(character.getType()).height
                - loader.getDimension(currentGame.getCharacter()).height, character.getWidth(), character.getHeight(),
                    character.getSpeedX(), character.getSpeedY(), currentGame.getCharacter());
        dynamics.remove(0);
        dynamics.add(0, character);
        character.setLockedCharacter();
        lockedElement = character;
        ViewPort.getInstance().setLockedElement(character);
        GameEngine.getInstance().getMap().setLockedCharacter(character);
        character.setImages(loader.getSprite(currentGame.getCharacter()));
        character.setWidth(loader.getDimension(character.getType()).width);
        character.setHeight(loader.getDimension(character.getType()).height);*/
        layer = Layers.getInstance().getALL_LAYERS().get(2); // TODO : read from config
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
                case "MiniMario", "MegaMario", "FireMario" -> { // TODO : attach handlers for other characters as well getting them from config
                    MarioThread mt = new MarioThread(de);
                    de.setManager(mt);
                    mt.start();
                    mt.pause();
                    animations.add(mt);
                }
            }
        }
        timeElapsed = currentGame.getTimeElapsed();
        GameEngine.getInstance().enableCustomPainting();
        mid = ViewPort.getInstance().getWidth() / 2;
        activatedBlocks.clear();
    } // TODO : clean and segregate this method !
    @Override
    public void stop() {
        pauseManagers();
        soundSystem.pause();
    }
    private void pauseManagers() {
        for (var dynamicElement : dynamics)
            if (dynamicElement.getManager() != null)
                dynamicElement.getManager().pause();
    }
    private void resumeManagers() {
        for (var dynamicElement : dynamics) {
            String type = dynamicElement.getType();
            ElementManager manager = dynamicElement.getManager();
            if (manager != null && !(type.equals("MiniMario") || type.equals("MegaMario") || type.equals("FireMario")))
                manager.restart();
        }
    }
    @Override
    public void resume() {
        soundSystem.play("Background", true, true);
        resumeManagers();
    }
    @Override
    public void reset() {
        for (ElementManager manager : animations) {
            manager.kill();
        }
        animations.clear();
        gThread.remove();
        gThread = new Gravity();
        GameManager.getInstance().resetGame();
        setLockedElement(ViewPort.getInstance().getLockedElement());
        timeElapsed = 0;
    }
    private void killMario() {
        soundSystem.play("MarioDeath", false, false);
        int coinsLost = ((checkPointsSaved + 1) * currentGame.getCoinsEarned() + calculateRisk()) / (checkPointsSaved + 4);
        currentGame.setCoinsEarned(Math.max(currentGame.getCoinsEarned() - coinsLost, 0));
        if (currentGame.getLives() > 0) {
            currentGame.setLevel(0);
            currentGame.setScore(0);
            currentGame.setLives(currentGame.getLives() - 1);
            reset();
        }
        else {
            currentGame.setTimeElapsed(timeElapsed); // minus the respawn delay
            soundSystem.pause();
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
    }
    private int calculateRisk() {
        return (int) (currentGame.getCoinsEarned() * ((double) lockedElement.getX() /
                Config.getInstance().getProperty("MaxDist", Integer.class)));
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
        saveReady = false;
        int size = dynamics.size();
        for (int i = 0; i < size; i++) {
            DynamicElement element = dynamics.get(i);
            // gravity
            if (element.getBounds().BOTTOM > ViewPort.getInstance().getHeight() - 20) { // TODO : read the bottom bound from config
                element.setHidden(true);
                ElementManager manager = element.getManager();
                if (manager != null) {
                    manager.kill();
                }
                if (element.isLockedCharacter()) {
                    killMario();
                    currentGame.setScore(Math.max(currentGame.getScore() - 30, 0));
                    return;
                }
            }
            // collision with statics
            int size2 = statics.size();
            // inTouch[i] = false;
            for (int j = 0; j < size2; j++) {
                StaticElement element1 = statics.get(j);
                if (element.collidesWith(element1)) {
                    switch (element1.getType()) {
                        case "FlagPole" -> {
                            calculateScore();
                            currentGame.setLevel(currentGame.getLevel() + 1);
                            reset();
                            return;
                        }

                        case "Castle" -> {
                            calculateScore();
                            manager.saveProgress();
                            soundSystem.pause();
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
                                            if (!activatedBlocks.contains(element1)) {
                                                // TODO : remove hardcoded "flower"
                                                activatedBlocks.add(element1);
                                                EventQueue.getInstance().publish(new Event(EventType.PowerUpTriggered,
                                                        element1.getX() + "x" + (element1.getY() - 18)));
                                                element1.swapImage(1);
                                            }
                                        }
                                    } else {
                                        inTouch[0] = true;
                                        element.setSpeedY(Math.min(0, element.getSpeedY()));
                                        if (lockedElement.getSpeedX() == 0)
                                            lockedElement.getManager().resetState();
                                    }
                                }
                                else {
                                    // element.setSpeedY(0);
                                    element.setSpeedY(Math.min(0, - (int) ((0.9) * element.getSpeedY())));
                                }
                            }
                        }

                        case "CheckPoint" -> {
                            if (element.isLockedCharacter()) {
                                saveReady = true;
                            }
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
                        if (element1.getManager() != null)
                            element1.getManager().kill(); // TODO : kill mario or the enemy ??!!
                        if (!element1.isHidden()) {
                            switch(element1.getType()) {
                                case "Coin" :
                                    soundSystem.play("CoinEarned", false, false);
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
                                case "Flower" :
                                    if (!element1.isHidden()) {
                                        element1.setHidden(true);
                                        if (element.isLockedCharacter()) {
                                            // TODO : earn power up

                                        }
                                    }
                            }
                            soundSystem.play("Background", true, true);
                        }
                    }
                    else {
                        element.setSpeedX(-element.getSpeedX());
                        element1.setSpeedX(-element1.getSpeedX());
                    }
                }
            }
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
        g.drawString("SCORE : " + currentGame.getScore(), mid + 500, 100);
        g.drawString("WORLD : " + currentGame.getLevel(), mid - 500, 100);
        if (saveReady)
            g.drawString("HIT X TO ENTER THE SAVE MENU", mid - 100, 200);
    }
    @Override
    public void handleKeyPress(int keyCode) {
        if (keyCode == 88 && saveReady) { // specific handling of the checkpoint trigger
            EventQueue.getInstance().publish(new Event(EventType.KeyToggled, keyCode + ",Press"));
        } else {
            switch (keyCode) {
                case 38 -> {
                    jumping = true;
                    if (inTouch[0]) {
                        minY = lockedElement.getY() - 160;
                        lockedElement.setSpeedY(6 * UP);
                    }
                }
                case 39 -> lockedElement.setSpeedX(RIGHT); // 3 * RIGHT
                case 37 -> lockedElement.setSpeedX(LEFT); // 3 * LEFT
            } // TODO : move gravity into engine and pause it from the event handler
            EventQueue.getInstance().publish(new Event(EventType.KeyToggled, keyCode + ",Press"));
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
        EventQueue.getInstance().publish(new Event(EventType.KeyToggled, keyCode + ",Release"));
    }
    @Override
    public void handleMouseClick(int x, int y) {
        EventQueue.getInstance().publish(new Event(EventType.MouseClicked, x + "," + y));
    }
    public void getModalTypes() {
        Config c = Config.getInstance();
        String[] modalTypes = c.getProperty("ModalTypes").split(",");
        for (String type : modalTypes) {
            this.modalTypes.put(type, c.getProperty(type).split(","));
        }
    }
    public Point getModalPosition() {
        return new Point(ViewPort.getInstance().getX() + mid, ViewPort.getInstance().getY() +
                (ViewPort.getInstance().getHeight() / 2) - Config.getInstance().getProperty("ModalHeight",
                    Integer.class) / 2);
    }
    public String[] getModalOptions(String modalType) {
        return modalTypes.get(modalType);
    }
    @Override
    public void pauseElementManagers() {
        for (var manager : animations)
            manager.pause();
    }
    @Override
    public void resumeElementManagers() {
        for (var manager : animations)
            manager.restart();
    }
    @Override
    public void saveGame() {
        currentGame.setTimeElapsed(timeElapsed); // minus the respawn delay
        checkPointsSaved++;
        currentGame.setCoinsEarned(Math.max(currentGame.getCoinsEarned() - calculateRisk(), 0));
    }
    public void withdrawCheckpoint() { // TODO : single checkpoint only implementation
        LevelEditor.getInstance().removeElement("CheckPoint", 2);
        currentGame.setCoinsEarned((int) (currentGame.getCoinsEarned() + 0.25 * calculateRisk()));
    }
    public GameStat getCurrentGame() {
        return currentGame;
    }
}
