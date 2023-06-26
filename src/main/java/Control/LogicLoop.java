package Control;

import Core.Util.Routine;

public class LogicLoop extends Routine {
    public LogicLoop(int FPS) {
        super(FPS, GameManager.getInstance().getGameLogic()::check);
    }

}
