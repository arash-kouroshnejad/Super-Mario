package game.model;

import core.render.ViewPort;
import game.animations.mario.SwordAnimation;

import java.util.Timer;
import java.util.TimerTask;

public class Sword {
    private Timer timer;
    public Timer generateTimer() {
        var timer = new Timer("SwordTimer");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                var animation = new SwordAnimation(ViewPort.getInstance().getLockedElement());
                animation.run();
            }
        }, 2000);
        this.timer = timer;
        return timer;
    }

    public void cancel() {
        timer.cancel();
    }
}
