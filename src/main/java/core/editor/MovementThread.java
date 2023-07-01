package core.editor;

import core.render.ViewPort;
import core.util.Routine;

public class MovementThread extends Routine {
    public MovementThread() {
        super(50, () -> {
            var element = ViewPort.getInstance().getLockedElement();
            if (element != null)
                element.move();
        });
    }
}
