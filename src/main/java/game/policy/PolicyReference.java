package game.policy;

import control.GameManager;
import core.objects.DynamicElement;
import core.objects.ElementManager;
import core.objects.Layers;
import core.objects.StaticElement;
import game.BowserLogic;
import game.MarioLogic;
import game.animations.bowser.BowserThread;
import game.animations.enemies.BirdThread;
import game.animations.enemies.KoopaThread;
import game.animations.enemies.PlantThread;
import game.animations.enemies.SpinyThread;
import game.animations.items.CoinThread;
import game.animations.mario.MarioThread;
import game.model.GameStat;
import game.model.ShieldTimer;
import game.model.TimedBlock;
import game.plugins.SoundQueue;
import persistence.Config;

import java.util.*;

public class PolicyReference {
    private final static PolicyReference instance = new PolicyReference();
    private PolicyReference() {
        allBlocks = new HashSet<>(List.of(Config.getInstance().getProperty("AllBlocks").split(",")));
    }
    public static PolicyReference getInstance() {return instance;}

    public void init() {
        dynamics = Layers.getInstance().getALL_LAYERS().get(1).getDynamicElements();
        statics = Layers.getInstance().getALL_LAYERS().get(2).getStaticElements();
        currentGame = GameManager.getInstance().getCurrentGame();
        registerElements();
        if (currentGame.getLevel() == 3) {
            registerBlocks();
        }
        marioLogic = GameManager.getInstance().getGameLogic();
        bowserLogic = GameManager.getInstance().getBowserLogic();
        soundSystem = marioLogic.getSoundSystem();
    }

    private void registerElements() {
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
                case "Bowser" -> {
                    BowserThread bt = new BowserThread(de);
                    de.setManager(bt);
                    GameManager.getInstance().getBowserLogic().init();
                    bt.start();
                    animations.add(bt);
                    // Bar.getBar("HPBar");
                }
            }
        }
    }

    private void registerBlocks() {
        Config c = Config.getInstance();
        Set<String> types = new HashSet<>(List.of(c.getProperty("TimedBlocks").split(",")));
        hoveringBlocks = types;
        for (var block : statics)
            if (types.contains(block.getType()))
                TimedBlock.addBlock(block, 2000);
    }

    public int jumpLimit = 90;
    public int jumpSpeed = -8;
    public int verticalSpeedLimit = 5;
    public int horizontalSpeedLimit = 5;
    public boolean onSlime;
    public boolean saveReady;
    public int minY;
    public boolean jumping;
    public boolean onGround;
    public int marioState;
    public final HashSet<StaticElement> activatedBlocks = new HashSet<>();
    public GameStat currentGame;
    public Set<String> allBlocks;
    public Set<Integer> registeredKeys = new HashSet<>();
    public boolean crouching;
    public final ShieldTimer shield = ShieldTimer.getInstance();
    public final Map<String, Timer> timers = new HashMap<>();
    public List<DynamicElement> dynamics;
    public List<StaticElement> statics;
    public MarioLogic marioLogic;
    public BowserLogic bowserLogic;
    public SoundQueue soundSystem;
    public Set<String> hoveringBlocks;
    public final List<ElementManager> animations = new ArrayList<>();
}
