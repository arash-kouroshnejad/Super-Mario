package Control;

import Core.Render.Routine;

public class LogicLoop extends Routine {
    public LogicLoop(int FPS) {
        super(FPS, GameManager.getInstance().getGameLogic()::check);
    }
}
