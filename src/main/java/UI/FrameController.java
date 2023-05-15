package UI;

public abstract class FrameController {
    protected final Navigable frame;

    public FrameController(Navigable frame) {
        this.frame = frame;
        frame.setController(this);
    }

    public void show() {
        frame.setVisible(true);
    }
    public void hide() {
        frame.setVisible(false);
    }
    public abstract void select(String selection);
}
