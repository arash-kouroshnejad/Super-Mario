package Game;

import Control.GameManager;
import Core.Editor.LevelEditor;
import Core.Objects.*;
import Core.Render.GameEngine;
import Core.Render.ViewPort;
import Core.Util.Loader;
import Core.Util.Logic;
import Game.Model.CoinFilledBlocks;
import Game.Model.GameStat;
import Game.Model.Mario;
import Game.Model.Shield;
import Game.Plugins.ElementManagers.*;
import Game.Plugins.Gravity;
import Game.Plugins.SoundQueue;
import Game.Util.Events.Event;
import Game.Util.Events.EventQueue;
import Game.Util.Events.EventType;
import Persistence.Config;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public class MarioLogic extends Logic {

    private final GameManager manager = GameManager.getInstance();
    private final ArrayList<ElementManager> animations = new ArrayList<>();
    private final SoundQueue soundSystem = SoundQueue.getInstance();
    private final Shield shield = Shield.getInstance();
    private final HashMap<String, String[]> modalTypes = new HashMap<>();
    private final HashSet<StaticElement> activatedBlocks = new HashSet<>();
    private final ArrayList<StaticElement> removalQueue = new ArrayList<>();
    Gravity gThread = new Gravity();
    int mid;
    int marioState;
    private List<DynamicElement> dynamics;
    private List<StaticElement> statics;
    private boolean[] inTouch; // networked marios jump control ?
    private boolean jumping;
    private int minY;
    private long timeElapsed; // in milliseconds
    private GameStat currentGame;
    private boolean saveReady;
    private int checkPointsSaved;
    private int jumpLimit = 160;
    private int jumpSpeed = 6 * UP;
    private int verticalSpeedLimit = 5;
    private int horizontalSpeedLimit = 5;
    private boolean onSlime;

    public void init(Loader loader) {
        soundSystem.init(this);
        soundSystem.play("Background", true, true);
        getModalTypes();
        Layer layer = Layers.getInstance().getALL_LAYERS().get(1);
        dynamics = layer.getDynamicElements();
        // DynamicElement character = LevelEditor.getInstance().getDynamicElement("MiniMario", 1,-1).orElseThrow(); // TODO : necessary ?
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
                case "MiniMario", "MegaMario", "FireMario" -> {
                    MarioThread mt = new MarioThread(de);
                    de.setManager(mt);
                    mt.start();
                    mt.pause();
                    animations.add(mt);
                }
                case "Koopa" -> {
                    KoopaThread kt = new KoopaThread(de);
                    de.setManager(kt);
                    kt.start();
                    animations.add(kt);
                }
                case "Spiny" -> {
                    SpinyThread st = new SpinyThread(de);
                    de.setManager(st);
                    st.start();
                    animations.add(st);
                }
                case "Bird" -> {
                    BirdThread bt = new BirdThread(de);
                    de.setManager(bt);
                    bt.start();
                    animations.add(bt);
                }
            }
        }
        dropPowerUp();
        shield.deactivate();
        timeElapsed = currentGame.getTimeElapsed();
        GameEngine.getInstance().enableCustomPainting();
        mid = ViewPort.getInstance().getWidth() / 2;
        activatedBlocks.clear();
    } // TODO : clean and segregate this method !

    @Override
    public void stop() {
        pauseManagers();
        gThread.pause();
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
        gThread.restart();
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
        // setLockedElement(ViewPort.getInstance().getLockedElement());
        timeElapsed = 0;
    }

    private void killMario() {
        soundSystem.play("MarioDeath", false, false);
        int coinsLost = ((checkPointsSaved + 1) * currentGame.getCoinsEarned() + calculateRisk()) / (checkPointsSaved + 4);
        currentGame.setCoinsEarned(Math.max(currentGame.getCoinsEarned() - coinsLost, 0));
        if (currentGame.getLives() > 0) {
            currentGame.setLives(currentGame.getLives() - 1);
            reset();
        } else {
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

    private void earnPowerUp() {
        int lastState = marioState;
        marioState = Math.min(2, ++marioState);
        if (marioState != lastState)
            EventQueue.getInstance().publish(new Event(EventType.GenerateElement,
                    lockedElement.getX() + "x" + (lockedElement.getY() - 20) + "," +
                            Mario.getInstance().getMarioState(marioState).name()));
    }

    private void dropPowerUp() {
        int lastState = marioState;
        marioState = 0;
        EventQueue.getInstance().publish(new Event(EventType.GenerateElement,
                lockedElement.getX() + "x" + (lockedElement.getY() - 20) + ",Mario-" +
                        lastState + "/" + marioState));
    }

    private void takeDamage() {
        if (marioState > 0)
            dropPowerUp();
        else {
            currentGame.setScore(Math.max(0, currentGame.getScore() - 20));
            killMario();
        }
    }

    private void activateSuperJump() {
        onSlime = true;
        verticalSpeedLimit *= 5;
        jumpLimit *= 5;
        jumpSpeed *= 5;
    }

    private void deactivateSuperJump() {
        onSlime = false;
        verticalSpeedLimit /= 5;
        jumpLimit /= 5;
        jumpSpeed /= 5;
    }

    private void jump() {
        minY = lockedElement.getY() - jumpLimit;
        lockedElement.setSpeedY(jumpSpeed);
        lockedElement.setY(lockedElement.getY() - 10); // TODO : collision avoiding distance :(
        if (onSlime)
            deactivateSuperJump();
    }

    private void updateTime() {
        timeElapsed += 10; // 100 fps --> 10 milliseconds per frame
    }

    private int calculateRisk() {
        return (int) (currentGame.getCoinsEarned() * ((double) lockedElement.getX() /
                Config.getInstance().getProperty("MaxDist", Integer.class)));
    }

    private boolean inRange(Point point, DynamicElement element) {
        if (Math.min(Math.abs(point.x - element.getX()), Math.abs(point.y - element.getY())) < 300) {
            return Math.sqrt(Math.pow(point.x - element.getX(), 2) + Math.pow(point.y - element.getY(), 2)) < 300;
        }
        return false; // TODO : proximity element detections lacks
    }

    @Override
    public void check() {
        // TODO : ??? induced speedY limit
        lockedElement.setSpeedY(Math.max(-verticalSpeedLimit, lockedElement.getSpeedY()));
        lockedElement.setSpeedY(Math.min(horizontalSpeedLimit, lockedElement.getSpeedY()));
        updateTime();
        if (timeElapsed > 14000000) {
            killMario();
            return;
        }
        // jump limit
        if (!(inTouch[0] && jumping) && Math.abs(lockedElement.getY() - minY) <= 5) {
            lockedElement.setSpeedY(Math.max(0, lockedElement.getSpeedY()));
        }
        /*if (shield.isActive())
            shield.updateElementPosition();
        else
            EventQueue.getInstance().publish(new Event(EventType.GenerateElement, "0x0,RemoveShield"));*/
        inTouch[0] = false;
        saveReady = false;
        ArrayList<DynamicElement> cloned = new ArrayList<>(dynamics);
        int index = 0;
        int size = cloned.size();
        for (var element : cloned) {
            // gravity
            if (element.getBounds().BOTTOM > ViewPort.getInstance().getHeight() - 20) { // TODO : read the bottom bound from config
                element.setHidden(true);
                removalQueue.add(element);
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
            for (StaticElement element1 : statics) {
                if (element.collidesWith(element1)) {
                    if (element.isLockedCharacter() && !element1.isHidden()) // TODO : GET RID OF THE IsHidden CALL
                        switch (element1.getType()) {
                            case "FlagPole" -> {
                                calculateScore();
                                manager.saveProgress(); // TODO : test save/load extensively
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
                            case "Floor", "Pipe", "Stair", "Brick", "PowerUpBlock", "PipeExtension", "CoinedBlock",
                                    "FilledBlock", "CoinFilledBlock", "SlimeBlock" -> {
                                if (element.collidesHorizontally(element1)) {
                                    // horizontal collision
                                    if (element1.getX() >= element.getX()) {
                                        element.setSpeedX(Math.min(0, element.getSpeedX()));
                                        element.setX(element.getX() - 10);
                                    } else {
                                        element.setSpeedX((Math.max(0, element.getSpeedX())));
                                        element.setX(element.getX() + 10);
                                    }
                                } else {
                                    // vertical collision
                                    if (Math.abs(element.getBounds().TOP - element1.getBounds().BOTTOM) < 10) {
                                        element.setSpeedY(Math.max(0, element.getSpeedY()));
                                        switch (element1.getType()) {
                                            case "PowerUpBlock":
                                                if (!activatedBlocks.contains(element1)) {
                                                    activatedBlocks.add(element1);
                                                    EventQueue.getInstance().publish(new Event(EventType.GenerateElement,
                                                            element1.getX() + "x" + (element1.getY() - 18) + ",Item"));
                                                    element1.swapImage(1);
                                                }
                                                break;
                                            case "CoinedBlock":
                                                lockedElement.setY(lockedElement.getY() + 10);
                                                EventQueue.getInstance().publish(new Event(EventType.GenerateElement,
                                                        element1.getX() + "x" + element1.getY() + ",Coin"));
                                                EventQueue.getInstance().publish(new Event(EventType.GenerateElement,
                                                        element1.getX() + "x" + element1.getY() + ",Brick"));
                                                removalQueue.add(element1);
                                                break;
                                            case "CoinFilledBlock":
                                                lockedElement.setY(lockedElement.getY() + 10);
                                                if (CoinFilledBlocks.getInstance().use(element1)) {
                                                    currentGame.setCoinsEarned(currentGame.getCoinsEarned() + 1);
                                                    soundSystem.play("CoinEarned", false, false);
                                                } else {
                                                    EventQueue.getInstance().publish(new Event(EventType.GenerateElement,
                                                            element1.getX() + "x" + element1.getY() + ",FilledBlock"));
                                                    removalQueue.add(element1);
                                                }
                                                break;
                                            case "Brick":
                                                currentGame.setScore(currentGame.getScore() + 1);
                                                removalQueue.add(element1);
                                                break;
                                        }
                                    } else {
                                        if (element1.getType().equals("SlimeBlock")) {
                                            if (!onSlime) {
                                                activateSuperJump();
                                                if (element.getSpeedY() > 0)
                                                    jump();
                                            }
                                        } else if (onSlime)
                                            deactivateSuperJump();
                                        inTouch[0] = true;
                                        element.setSpeedY(Math.min(0, element.getSpeedY()));
                                    }
                                }
                            }
                            case "CheckPoint" -> {
                                if (element.isLockedCharacter())
                                    saveReady = true;
                            }
                        }
                    else if (element.getType().equals("Bullet"))
                        removalQueue.add(element);
                    else if (element.getType().equals("Bomb")) {
                        soundSystem.play("Explosion", false, false);
                        removalQueue.add(element);
                        // TODO : find nearby enemies
                        for (var tmp : dynamics) {
                            if (inRange(new Point(element1.getX(), element1.getY()), tmp)) {
                                if (tmp.isLockedCharacter())
                                    takeDamage();
                                else
                                    removalQueue.add(tmp);
                            }
                        }
                    }
                    else {
                        if (element.collidesHorizontally(element1)) {
                            element.setSpeedX(-element.getSpeedX());
                            if (element.getType().equals("Koopa"))
                                element.getManager().setMirrored(!element.getManager().isMirrored());
                        } else
                            element.setSpeedY(Math.min(0, -(int) ((0.6) * element.getSpeedY())));
                    }
                }
            }
            // collision with dynamics
            for (int j = index + 1; j < size; j++) {
                DynamicElement element1 = cloned.get(j);
                if (element.collidesWith(element1)) {
                    // mario
                    if (element.isLockedCharacter()) {
                        if (!element1.isHidden()) {
                            switch (element1.getType()) {
                                case "Coin":
                                    soundSystem.play("CoinEarned", false, false);
                                    currentGame.earnCoin();
                                    element1.setHidden(true);
                                    removalQueue.add(element1);
                                    break;
                                case "Star":
                                    earnPowerUp();
                                    shield.activate(15);
                                    currentGame.setScore(currentGame.getScore() + 40);
                                    element1.setHidden(true);
                                    removalQueue.add(element1);
                                    break;
                                case "Plant":
                                    if (!element1.isHidden()) {
                                        takeDamage();
                                        return;
                                    }
                                    break;
                                case "Goomba":
                                    if (!element1.isHidden()) {
                                        if (element.collidesHorizontally(element1) && !shield.isActive()) {
                                            // TODO : TEST PROPER DEATH FUNCTIONALITY WITH CRAPPY COLLISION DETECTION
                                            takeDamage();
                                            return;
                                        } else {
                                            element1.setHidden(true);
                                            removalQueue.add(element1);
                                            currentGame.killEnemy();
                                        }
                                    }
                                    break;
                                case "Mushroom":
                                    earnPowerUp();
                                    currentGame.setScore(currentGame.getScore() + 30);
                                    element1.setHidden(true);
                                    removalQueue.add(element1);
                                    break;
                                case "Flower":
                                    element1.setHidden(true);
                                    removalQueue.add(element1);
                                    currentGame.setScore(currentGame.getScore() + 10); // TODO : turn into a separate method
                                    earnPowerUp();
                                    break;
                                case "Koopa":
                                    if (!element1.getManager().isPaused()) {
                                        if (element.collidesHorizontally(element1)) {
                                            takeDamage();
                                        } else {
                                            boolean mirrored = lockedElement.getManager().isMirrored();
                                            element1.setSpeedX(mirrored ? -2 : 2);
                                            element1.setX(element1.getX() + (mirrored ? -5 : 5));
                                            element1.getManager().resetState();
                                        }
                                    } else {
                                        currentGame.setScore(currentGame.getScore() + 3);
                                        currentGame.killEnemy();
                                        removalQueue.add(element1);
                                    }
                                    break;
                                case "Bomb", "Bird":
                                    takeDamage();
                                    break;
                            }
                        }
                    }
                    // generated elements
                    else if (element1.getType().equals("GoldenRing")) {
                        if (element.getType().equals("Goomba") || element.getType().equals("Koopa") ||
                                element.getType().equals("Spiny")) {
                            element.setHidden(true);
                            removalQueue.add(element);
                            soundSystem.play("Explosion", false, false);
                        }
                    } else if (element1.getType().equals("PipeSword")) {
                        if (element.getType().equals("Goomba") || element.getType().equals("Plant"))
                            removalQueue.add(element);
                        removalQueue.add(element1);
                    } else if (element1.getType().equals("Bomb")) {
                        if (element.getType().equals("Goomba") || element.getType().equals("Koopa") ||
                                element.getType().equals("Spiny") || element.getType().equals("Plant")) {
                            removalQueue.add(element);
                            soundSystem.play("Explosion", false, false);
                        }
                    } else if (element1.getType().equals("Bullet")) {
                        if (element.getType().equals("Goomba") || element.getType().equals("Plant") ||
                                element.getType().equals("Spiny") || element.getType().equals("Bird")) {
                            removalQueue.add(element);
                            removalQueue.add(element1);
                            currentGame.killEnemy();
                        } else if (!element.getType().equals("GoldenRing"))
                            removalQueue.add(element1); // TODO : this whole collision checking is order dependent make it not so
                    } else if (element.getType().equals("Koopa") && (element1.getType().equals("Goomba")) ||
                            element1.getType().equals("Spiny") && element.getManager().isPaused())
                        removalQueue.add(element1);
                    else if (element1.getType().equals("Koopa") && (element.
                            getType().equals("Goomba") || element.getType().equals("Spiny")) && element1.getManager().isPaused())
                        removalQueue.add(element);
                    else {
                        // invert elements
                        if (element.getManager() != null)
                            element.getManager().setMirrored(!element.getManager().isMirrored());
                        if (element1.getManager() != null)
                            element1.getManager().setMirrored(!element1.getManager().isMirrored());
                        element.setSpeedX(-element.getSpeedX());
                        element1.setSpeedX(-element1.getSpeedX());
                    }
                }
            }
            // PowerUp removal
            if (!ViewPort.getInstance().inView(element)) {
                if (element.getType().equals("Star") || element.getType().equals("Mushroom"))
                    removalQueue.add(element);
            }
            index++;
        }
        for (var element : removalQueue)
            LevelEditor.getInstance().removeElement(element);
        removalQueue.clear(); // TODO : get rid of the set hidden function call
        /*if (soundSystem.isPaused())
            soundSystem.play("Background", true, true);*/
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        mid = ViewPort.getInstance().getWidth() / 2;
        g.setFont(new Font("default", Font.BOLD, 16));
        g.drawString("TIME : " + timeElapsed, mid - 300, 100);
        g.drawString("COINS : " + currentGame.getCoinsEarned(), mid + 100, 100);
        g.drawString("LIVES : " + currentGame.getLives(), mid + 300, 100);
        g.drawString("SCORE : " + currentGame.getScore(), mid + 500, 100);
        g.drawString("WORLD : " + currentGame.getLevel(), mid - 500, 100);
        if (saveReady)
            g.drawString("HIT X TO ENTER THE SAVE MENU", mid - 100, 200);
        if (shield.isActive())
            g.drawString("SHIELD : " + shield.getLeft() + "%", mid - 100, 300);
    }

    @Override
    public void handleKeyPress(int keyCode) {
        if (keyCode == 88) { // specific handling of the checkpoint trigger
            if (saveReady)
                EventQueue.getInstance().publish(new Event(EventType.KeyToggled, keyCode + ",Press"));
        } else if (keyCode == 32) {
            if (marioState == 2 && inTouch[0])
                EventQueue.getInstance().publish(new Event(EventType.KeyToggled, keyCode + ",Press"));
        } else {
            switch (keyCode) {
                case 38 -> {
                    jumping = true;
                    if (inTouch[0])
                        jump();
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
}
