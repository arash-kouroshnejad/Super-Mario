package game;

import control.GameManager;
import core.editor.LevelEditor;
import core.objects.DynamicElement;
import core.render.ViewPort;
import game.animations.AbstractAnimation;
import game.animations.bowser.*;
import game.policy.policies.Keys.STRANGLE;
import game.policy.PolicyStack;
import persistence.Config;

import java.util.Timer;
import java.util.TimerTask;

public class BowserLogic implements Runnable{

    private DynamicElement element;

    private AbstractAnimation closingAnimation;

    private AbstractAnimation fireBallAttack;

    private AbstractAnimation grabAttack;

    private AbstractAnimation jumpAttack;

    private AbstractAnimation randomMovement;

    private boolean closing;

    private boolean inFireBallAttack;

    private boolean inGrabAttack;

    private boolean inJump;

    private boolean interruptedGrabAttack;

    private Timer timer;

    private MarioLogic logic;

    public void init() {
        int layer = Config.getInstance().getProperty("DynamicsLayer", Integer.class);
        element = LevelEditor.getInstance().getDynamicElement("Bowser", layer, 0).orElseThrow();
        closingAnimation = new Closing(element);
        fireBallAttack = new FireBallAttack(element);
        grabAttack = new GrabAttack(element);
        jumpAttack = new JumpAttack(element);
        randomMovement = new RandomMovement(element);
        logic = GameManager.getInstance().getGameLogic();
    }

    @Override
    public void run() {
        if (interruptedGrabAttack) {
            try {
                grabAttack.reset();
                interruptedGrabAttack = false;
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        else if (toClose())
            closingAnimation.run();

        else if (toThrowFireBall())
            fireBallAttack.run();

        else if (toGrab())
            grabAttack.run();

        else if (inJump)
            jumpAttack.run(); // todo fix condition

        else
          randomMovement.run();
    }

    private boolean toClose() {
        int dist = element.getX() - ViewPort.getInstance().getLockedElement().getX();
        closing = Math.abs(dist) >= 500 || (closing && Math.abs(dist) >= 300);
        return closing;
    }

    private boolean toThrowFireBall() {
        int dist = Math.abs(element.getX() - ViewPort.getInstance().getLockedElement().getX());
        inFireBallAttack = (dist >= 200) && (dist <= 300);
        return inFireBallAttack;
    }

    private boolean toGrab() {
        int dist = Math.abs(element.getX() - ViewPort.getInstance().getLockedElement().getX());
        inGrabAttack = dist <= 100;
        return inGrabAttack;
    }

    public void Jump() {
        inJump = true;
    }

    public void killAttack() {interruptedGrabAttack = true;}

    public void captureMario() {
        var mario = ViewPort.getInstance().getLockedElement();
        mario.setSpeedX(0);
        mario.getManager().restart();
        mario.setHidden(true);
        var keys = PolicyStack.getInstance().getKeyPolicies();
        keys.add(0, new STRANGLE());
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                thwartMario();
                logic.takeDamage();
                grabAttack.reset();
                resetKeys();
            }
        }, 5000);
    }

    public void releaseMario() {
        grabAttack.reset();
        timer.cancel();
        resetKeys();
        thwartMario();
        var mario = ViewPort.getInstance().getLockedElement();
        mario.setHidden(false);
    }

    private void thwartMario() {
        var mario = ViewPort.getInstance().getLockedElement();
        int x = element.getX();
        int deployX;
        if (x <= 400 || x >= 900)
            deployX = 500;
        else {
            boolean mirrored = element.getManager().isMirrored();
            deployX = x + ((mirrored) ? -1 : 1) * 200;
        }
        mario.setX(deployX);
    }

    private void resetKeys() {
        var keys = PolicyStack.getInstance().getKeyPolicies();
        if (keys.get(0) instanceof STRANGLE)
            keys.remove(0);
    }

    protected void takeDamage() {

        int x = element.getX();
        int deployX;
        if (x <= 200) {
            deployX = x + 30;
            element.setSpeedX(2);
        }
        else if (x >= 1000) {
            deployX = x - 30;
            element.setSpeedX(-2);
        }
        else {
            deployX = x + 30;
            element.setSpeedX(1);
        }
        element.setX(deployX);
    }
}
