package game.model;



import java.util.HashMap;
import java.util.Map;

public class Mario { // TODO : singleton mario element is a horrible design change it !
    private final static Mario instance = new Mario();

    private Mario() {
        marioVariants.put(0, MarioState.MiniMario);
        marioVariants.put(1, MarioState.MegaMario);
        marioVariants.put(2, MarioState.FireMario);
        sword = new Sword();
    }

    Map<Integer, MarioState> marioVariants = new HashMap<>();
    private final Sword sword;

    public  MarioState getMarioState(int level) {
        return marioVariants.getOrDefault(level, null);
    }


    public static Mario getInstance() {
        return instance;
    }

    public Sword getSword() {
        return sword;
    }

    public enum MarioState {
        MiniMario,
        MegaMario,
        FireMario
    }
}
