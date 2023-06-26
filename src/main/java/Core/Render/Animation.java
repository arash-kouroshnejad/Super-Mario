package Core.Render;


import Core.Util.Routine;

public class Animation extends Routine {
    public Animation(int FPS) {
        super(FPS, ViewPort.getInstance()::update);
    }
}
