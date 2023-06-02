package Game.Model;



import java.util.HashMap;
import java.util.Map;

public class Mario { // TODO : singleton mario element is a horrible design change it !
    private final static Mario instance = new Mario();

    private Mario() {
        marioVariants.put(0, MarioState.MiniMario);
        marioVariants.put(1, MarioState.MegaMario);
        marioVariants.put(2, MarioState.FireMario);
    }

    Map<Integer, MarioState> marioVariants = new HashMap<>();

    public  MarioState getMarioState(int level) {
        return marioVariants.getOrDefault(level, null);
    }


    public static Mario getInstance() {
        return instance;
    }

    public enum MarioState {
        MiniMario,
        MegaMario,
        FireMario
    }
}
