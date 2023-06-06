package Core.Render;


public class Animation extends Routine {
    public Animation(int FPS) {
        super(FPS, ViewPort.getInstance()::update);
    }
}
