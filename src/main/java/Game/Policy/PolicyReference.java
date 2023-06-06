package Game.Policy;

import Core.Objects.StaticElement;
import Game.Model.GameStat;
import Persistence.Config;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
}
