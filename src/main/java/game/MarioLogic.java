package game;

import control.GameManager;
import core.editor.LevelEditor;
import core.objects.*;
import core.render.GameEngine;
import core.render.ViewPort;
import core.util.Loader;
import core.util.Logic;
import core.util.Routine;
import game.animations.bowser.BowserThread;
import game.animations.enemies.BirdThread;
import game.animations.enemies.KoopaThread;
import game.animations.enemies.PlantThread;
import game.animations.enemies.SpinyThread;
import game.animations.mario.MarioThread;
import game.animations.items.CoinThread;
import game.model.*;
import game.model.Mario;
import game.plugins.Correction;
import game.plugins.Gravity;
import game.plugins.SoundQueue;
import game.policy.PolicyReference;
import game.policy.PolicyStack;
import game.policy.policies.objects.dynamics.*;
import game.policy.policies.objects.statics.*;
import game.util.events.Event;
import game.util.events.EventQueue;
import game.util.events.EventType;
import persistence.Config;

import java.awt.*;
import java.util.*;
import java.util.List;


public class MarioLogic extends Logic {

    private final GameManager manager = GameManager.getInstance();
    private final SoundQueue soundSystem = SoundQueue.getInstance();
    private final ShieldTimer shield = ShieldTimer.getInstance();
    private final HashMap<String, String[]> modalTypes = new HashMap<>();
    private final ArrayList<StaticElement> removalQueue = new ArrayList<>();
    Gravity gThread = new Gravity();
    int mid;
    private List<DynamicElement> dynamics;
    private List<StaticElement> statics;
    private boolean[] inTouch; // networked marios jump control ?
    private boolean jumping;
    private int minY;
    private long timeElapsed; // in milliseconds
    private GameStat currentGame;
    private int checkPointsSaved;
    private int jumpLimit = 160;
    private int jumpSpeed = 6 * UP;
    private int verticalSpeedLimit = 10;
    private int horizontalSpeedLimit = 5;
    private Routine correction;
    private final PolicyStack stack = PolicyStack.getInstance();
    private final PolicyReference policyReference = PolicyReference.getInstance();

    public void init(Loader loader) {
        soundSystem.init(this);
        soundSystem.play("Background", true, true);
        getModalTypes();
        Layer layer = Layers.getInstance().getALL_LAYERS().get(1);
        dynamics = layer.getDynamicElements();
        currentGame = manager.getCurrentGame();
        lockedElement = ViewPort.getInstance().getLockedElement();
        layer = Layers.getInstance().getALL_LAYERS().get(2); // TODO : read from config
        statics = layer.getStaticElements();
        replaceCharacter(loader);
        gThread.setElements(dynamics);
        gThread.apply();
        inTouch = new boolean[dynamics.size()];
        EventQueue.getInstance().publish(new Event(EventType.GenerateElement, "0x0,HPBar"));
        dropPowerUp();
        timeElapsed = currentGame.getTimeElapsed();
        GameEngine.getInstance().enableCustomPainting();
        mid = ViewPort.getInstance().getWidth() / 2;
        policyReference.activatedBlocks.clear();
        correction = new Correction();
        correction.start();
        registerPolicies();
        policyReference.init();
    } // TODO : clean and segregate this method !

    private void registerPolicies() {
        stack.dynamicPolicies.clear();
        stack.d2SPolicies.clear();
        var dynamicPolicies = stack.dynamicPolicies;
        dynamicPolicies.add(new game.policy.policies.objects.dynamics.Mario());
        dynamicPolicies.add(new game.policy.policies.objects.dynamics.Bomb());
        dynamicPolicies.add(new PipeSword());
        dynamicPolicies.add(new Bullet());
        dynamicPolicies.add(new GoldenRing());
        dynamicPolicies.add(new KoopaInShell());
        dynamicPolicies.add(new General());
        var d2sPolicies = stack.d2SPolicies;
        d2sPolicies.add(new Brick());
        d2sPolicies.add(new PowerUpBlock());
        d2sPolicies.add(new CoinedBlock());
        d2sPolicies.add(new CoinFilledBlock());
        d2sPolicies.add(new BlockPolicy());
        d2sPolicies.add(new Flag());
        d2sPolicies.add(new Gateway());
        d2sPolicies.add(new CheckPoint());
        d2sPolicies.add(new Castle());
    }

    private void replaceCharacter(Loader loader) {
        DynamicElement character = LevelEditor.getInstance().getDynamicElement("MiniMario", 1,-1).orElseThrow();
        character = new DynamicElement(character.getX(), character.getY() + loader.getDimension(character.getType()).height
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
        character.setHeight(loader.getDimension(character.getType()).height);
    }

    @Override
    public void stop() {
        pauseManagers();
        gThread.pause();
        soundSystem.pause();
    }

    protected void pauseManagers() {
        for (var dynamicElement : dynamics)
            if (dynamicElement.getManager() != null)
                dynamicElement.getManager().pause();
    }

    protected void resumeManagers() {
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

    public void activateSuperJump() {
        PolicyReference.getInstance().onSlime = true;
        verticalSpeedLimit *= 5;
        jumpLimit *= 5;
        jumpSpeed *= 5;
    }

    public void deactivateSuperJump() {
        PolicyReference.getInstance().onSlime = false;
        verticalSpeedLimit /= 5;
        jumpLimit /= 5;
        jumpSpeed /= 5;
    }

    public void jump() {
        policyReference.minY = lockedElement.getY() - policyReference.jumpLimit;
        lockedElement.setSpeedY(policyReference.jumpSpeed);
        lockedElement.setY(lockedElement.getY() - 10); // TODO : collision avoiding distance :(
        if (policyReference.onSlime)
            policyReference.marioLogic.deactivateSuperJump();
        policyReference.bowserLogic.resetTimer();
    }

    @Override
    public void reset() {
        for (ElementManager manager : policyReference.animations) {
            manager.kill();
        }
        policyReference.animations.clear();
        gThread.remove();
        gThread = new Gravity();
        // Bar.resetAllBars();
        correction.kill();
        if (shield.isActive())
            shield.deactivate();
        GameManager.getInstance().resetGame();
        // setLockedElement(ViewPort.getInstance().getLockedElement());
        timeElapsed = 0;
    }

    public void killMario() {
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

    public void calculateScore() {
        int points = 10 * currentGame.getCoinsEarned();
        points += 20 * currentGame.getLives();
        points += 15 * currentGame.getKillCount();
        updateTime();
        points += (80000 - currentGame.getTimeElapsed()) / 1000;
        currentGame.setScore(currentGame.getScore() + points);
    }

    public void earnPowerUp() {
        int lastState = policyReference.marioState;
        policyReference.marioState = Math.min(2, ++policyReference.marioState);
        if (policyReference.marioState != lastState)
            EventQueue.getInstance().publish(new Event(EventType.GenerateElement,
                    lockedElement.getX() + "x" + lockedElement.getY() + "," +
                            Mario.getInstance().getMarioState(policyReference.marioState).name()));
    }

    protected void dropPowerUp() {
        policyReference.marioState = 0;
        EventQueue.getInstance().publish(new Event(EventType.GenerateElement,
                lockedElement.getX() + "x" + (lockedElement.getY() - 20) + ",MiniMario"));
    }

    public void takeDamage() {
        if (policyReference.marioState > 0)
            dropPowerUp();
        else {
            currentGame.setScore(Math.max(0, currentGame.getScore() - 20));
            killMario();
        }
    }

    private void updateTime() {
        timeElapsed += 10; // 100 fps --> 10 milliseconds per frame
    }

    protected int calculateRisk() {
        return (int) (currentGame.getCoinsEarned() * ((double) lockedElement.getX() /
                Config.getInstance().getProperty("MaxDist", Integer.class)));
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
        policyReference.onGround = false;
        policyReference.saveReady = false; // todo : reset saveReady on each iteration (on ground)
        int index = 0;
        ArrayList<DynamicElement> cloned = new ArrayList<>(dynamics);
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
                    for (var policy : stack.d2SPolicies)
                        if (policy.isEnforceable(element, element1)) {
                            policy.enforce(element, element1);
                            break;
                        }
                }
            }
            // collision with dynamics
            index++;
            for (var iter = cloned.listIterator(index); iter.hasNext() &&
                    index + 1 < cloned.size(); ) {
                DynamicElement element1 = iter.next();
                if (element.collidesWith(element1)) {
                    for (var policy : stack.dynamicPolicies)
                        if (policy.isEnforceable(element, element1)) {
                            policy.enforce(element, element1);
                            break;
                        }
                    /*else if (element.getType().equals("Koopa") && (element1.getType().equals("Goomba")) ||
                            element1.getType().equals("Spiny"))
                        if (element.getManager().isPaused()) // todo : debug
                            removalQueue.add(element1);*/
                }
            }
            // PowerUp removal
            if (!ViewPort.getInstance().inView(element) && element.getType().equals("Star") ||
                    element.getType().equals("Mushroom"))
                removalQueue.add(element);
        }
        /*if (soundSystem.isPaused())
            soundSystem.play("Background", true, true);*/
        for (var element : dynamics)
            element.move(); // TODO : enable movements in editor mode
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
        if (policyReference.saveReady)
            g.drawString("HIT X TO ENTER THE SAVE MENU", mid - 100, 200);
        /*if (shield.isActive())
            g.drawString("Shield", Bar.getBar("Shield").getTilePosition().x, Bar.getBar("Shield").getTilePosition().y);
        g.drawString("Bowser, the King Of koopa", Bar.getBar("HPBar").getTilePosition().x,
                Bar.getBar("HPBar").getTilePosition().y);*/
    }

    @Override
    public void handleKeyPress(int keyCode) {
        EventQueue.getInstance().publish(new Event(EventType.KeyToggled, keyCode + ",Press"));
    }

    @Override
    public void handleKeyRelease(int keyCode) {
        if (keyCode == 38 || keyCode == 40)
            jumping = false; // todo move into policy reference
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
        for (var manager : policyReference.animations)
            manager.pause();
    }

    @Override
    public void resumeElementManagers() {
        for (var manager : policyReference.animations)
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

    public SoundQueue getSoundSystem() {
        return soundSystem;
    }
}
