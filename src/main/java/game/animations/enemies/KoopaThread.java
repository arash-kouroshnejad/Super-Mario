package game.animations.enemies;

import core.objects.DynamicElement;
import core.objects.ElementManager;
import game.animations.Sprint;
import persistence.Config;

public class KoopaThread extends ElementManager {
    public KoopaThread(DynamicElement element) {
        super(element, Sprint.getSprint(element, 100));
    }
    public void setMirrored(boolean mirrored) {
        super.setMirrored(mirrored);
        Sprint.setMirrored(element, mirrored);
    }
    public void resetState() {
        setAnimation(new KoopaInShellAnimation(element));
    }

    private record KoopaInShellAnimation(DynamicElement element) implements Runnable {
        @Override
            public void run() {
                try {
                    element.getManager().pause();
                    element.swapImage(Config.getInstance().getProperty("KoopaShell", Integer.class));
                    element.setHeight(element.getHeight() - 5); // TODO : fix this !
                    Thread.sleep(500);
                    element.setSpeedX(0);
                    Thread.sleep(3000);
                    element.getManager().setAnimation(Sprint.getSprint(element, 100));
                    element.getManager().setMirrored(false);
                    element.setSpeedX(1);
                    element.setHeight(element.getHeight() + 5);
                    element.setY(element().getY() - 5);
                    element.getManager().restart();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
}
