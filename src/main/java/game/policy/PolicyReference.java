package game.policy;

import core.objects.StaticElement;
import game.model.GameStat;
import persistence.Config;

import java.util.*;

public class PolicyReference {
    private final static PolicyReference instance = new PolicyReference();
    private PolicyReference() {
        allBlocks = new HashSet<>(List.of(Config.getInstance().getProperty("AllBlocks").split(",")));
    }

    public static PolicyReference getInstance() {return instance;}

    public int jumpLimit = 160;
    public int jumpSpeed = 12;
    public int verticalSpeedLimit = 5;
    public int horizontalSpeedLimit = 5;
    public boolean onSlime;
    public boolean saveReady;
    public int minY;
    public boolean jumping;
    public boolean onGround;
    public int marioState;
    public final HashSet<StaticElement> activatedBlocks = new HashSet<>();
    public final HashSet<StaticElement> removalList = new HashSet<>();
    public GameStat currentGame;
    public Set<String> allBlocks;
    public Set<Integer> registeredKeys = new HashSet<>();
    public boolean crouching;

    public final Map<String, Timer> timers = new HashMap<>();
}
