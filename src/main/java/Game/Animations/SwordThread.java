package Game.Animations;

import Core.Objects.DynamicElement;
import Core.Objects.ElementManager;
import Core.Render.ViewPort;

public class SwordThread extends ElementManager {
    public SwordThread(DynamicElement element) {
        super(element, () -> {
            try  {
                boolean mirrored = ViewPort.getInstance().getLockedElement().getManager().isMirrored();
                element.swapImage((mirrored ) ? 1 : 0);
                element.setSpeedX(mirrored ? -2 : 2);
                Thread.sleep(2000);
                element.swapImage(mirrored ? 0 : 1);
                element.setSpeedX(-element.getSpeedX());
                Thread.sleep(2000);
            } catch (Exception ignored) {}
        });
    }
}
