package Game.Plugins.ElementManagers;

import Core.Objects.DynamicElement;
import Core.Objects.ElementManager;
import Core.Render.ViewPort;
import Game.Plugins.Sprint;


public class SpinyThread extends ElementManager {
    public SpinyThread(DynamicElement element) {
        super(element, new CompoundAnimation(element));
    }

    @Override
    public void setMirrored(boolean mirrored) {
        super.setMirrored(mirrored);
        Sprint.setMirrored(element, mirrored);
    }

    private static class CompoundAnimation implements Runnable {
        private final DynamicElement element;
        private final Runnable secondary;
        public CompoundAnimation(DynamicElement element) {
            this.element = element;
            secondary = Sprint.getSprint(element, 100);
        }
        @Override
        public void run() {
            if (isinRange() && isLevel()) {
                boolean inRight = ViewPort.getInstance().getLockedElement().getX() - element.getX() > 0;
                element.getManager().setMirrored(!inRight);
                if (element.getSpeedX() > 0) {
                    if (inRight)
                        element.setSpeedX(element.getSpeedX() + 1);
                    else
                        element.setSpeedX(0);
                } else {
                    if (inRight)
                        element.setSpeedX(0);
                    else
                        element.setSpeedX(element.getSpeedX() - 1);
                }
            } else {
                boolean mirrored = element.getSpeedX() < 0;
                element.getManager().setMirrored(mirrored);
                element.setSpeedX(mirrored ? -2 : 2);
            }
            secondary.run();
        }

        private boolean isinRange() {
            return Math.abs(ViewPort.getInstance().getLockedElement().getX() - element.getX()) <= 400;
        }

        private boolean isLevel() {
            return Math.abs(element.getBounds().BOTTOM - ViewPort.getInstance().getLockedElement().getBounds().BOTTOM) < 5;
        }
    }
}
