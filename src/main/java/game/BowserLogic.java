package game;

import control.GameManager;
import core.editor.LevelEditor;
import core.objects.DynamicElement;
import core.render.ViewPort;
import game.animations.AbstractAnimation;
import game.animations.bowser.*;
import game.model.BowserState;
import game.policy.policies.Keys.STRANGLE;
import game.policy.PolicyStack;
import game.util.events.Event;
import game.util.events.EventQueue;
import game.util.events.EventType;
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

    private AbstractAnimation transition;

    private AbstractAnimation nukeAttack;

    private boolean closing;

    private boolean inFireBallAttack;

    private boolean inGrabAttack;

    private boolean inJump;

    private Timer timer;

    private MarioLogic logic;

    private BowserState bowserState;

    private boolean inPhase2;

    private long timeOnGround;

    private boolean[] attacksUsed = new boolean[3];

    public void init() {
        int layer = Config.getInstance().getProperty("DynamicsLayer", Integer.class);
        element = LevelEditor.getInstance().getDynamicElement("Bowser", layer, 0).orElseThrow();
        closingAnimation = new Closing(element);
        fireBallAttack = new FireBallAttack(element);
        grabAttack = new GrabAttack(element);
        jumpAttack = new JumpAttack(element);
        randomMovement = new RandomMovement(element);
        transition = new Transition(element);
        nukeAttack = new NukeAttack(element);
        logic = GameManager.getInstance().getGameLogic();
        bowserState = new BowserState(20);
    }

    @Override
    public void run() {
        if (toThrowNuke())
            throwNuke();

        if (bowserState.getHP() <= 10 && !inPhase2)
            transition.run();

        if (toJump())
            jump();

        else if (toClose())
            closingAnimation.run();

        else if (toThrowFireBall())
            throwFireBall();

        else if (toGrab())
            grab();

        else
          randomMovement.run();
    }

    private boolean toClose() {
        int dist = element.getX() - ViewPort.getInstance().getLockedElement().getX();
        closing = Math.abs(dist) >= 500 || (closing && Math.abs(dist) >= 300);
        return closing;
    }

    private void jump() {
        attacksUsed[0] = true;
        jumpAttack.run();
    }

    private void throwFireBall() {
        attacksUsed[1] = true;
        fireBallAttack.run();
    }

    private void throwNuke() {
        attacksUsed = new boolean[3];
        nukeAttack.run();
    }

    private void grab() {
        attacksUsed[2] = true;
        grabAttack.run();
    }

    private boolean toThrowNuke() {
        if (inPhase2) {
            for (var bool : attacksUsed)
                if (!bool)
                    return false;
            return true;
        }
        return false;
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

    private boolean toJump() {
        if (timeOnGround >= 4000 || inJump) {
            resetTimer();
            inJump = false;
            return true;
        }
        else  {
            return false;
        }
    }

    public void captureMario() {
        var mario = ViewPort.getInstance().getLockedElement();
        mario.setSpeedX(0);
        mario.getManager().resetState();
        mario.setHidden(true);
        PolicyStack.getInstance().disableKeys();
        var keys = PolicyStack.getInstance().getKeyPolicies();
        keys.add(new STRANGLE());
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                thwartMario();
                logic.takeDamage();
                PolicyStack.getInstance().resetKeys();
                grabAttack.reset();
            }
        }, 5000);
    }

    public void releaseMario() {
        timer.cancel();
        PolicyStack.getInstance().resetKeys();
        thwartMario();
        var mario = ViewPort.getInstance().getLockedElement();
        mario.setHidden(false);
        grabAttack.reset();
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

    private void takeDamage(DamageType type) {
        System.out.print(bowserState.getHP());
        if (type.equals(DamageType.SEVERE))
            bowserState.takeDamage(3);
        else if (type.equals(DamageType.MINOR))
            bowserState.takeDamage(1);
        System.out.println(">> " + bowserState.getHP());
        int x = element.getX();
        int dist = 80;
        if (x <= 200) {
            element.setX(x + dist);
            element.setSpeedX(2);
        } else if (x >= 1000) {
            element.setX(x - dist);
            element.setSpeedX(-2);
        } else {/*
            if (type.equals(DamageType.Hit)) {
                boolean onRight = ViewPort.getInstance().getLockedElement().getX() > element.getX();
                element.setX(x + (onRight ? -1 : 1) * dist);
                element.setSpeedX((onRight ? -1 : 1));
            } else if (type.equals(DamageType.JumpAttack)) {
                element.setX(x + dist);
                element.setSpeedX(1);
            } else {
                throw new RuntimeException();
            }*/
            boolean onRight = ViewPort.getInstance().getLockedElement().getX() > element.getX();
            element.setX(x + (onRight ? -1 : 1) * dist);
            element.setSpeedX((onRight ? -1 : 1));
        }
    }

    public void enablePhase2() {
        var marioLogic = GameManager.getInstance().getGameLogic();
        marioLogic.marioState = 2;
        var mario = ViewPort.getInstance().getLockedElement();
        EventQueue.getInstance().publish(new Event(EventType.GenerateElement,
                mario.getX() + "x" + (mario.getY() - 20) + "," + "FireMario"));
        inPhase2 = true;
    }

    public void accumulateTimer(long timeOnGround) {
        this.timeOnGround += timeOnGround;
    }

    public void resetTimer() {
        timeOnGround = 0;
    }

    public void takeBullet() {
        var mario = ViewPort.getInstance().getLockedElement();
        double dist = Math.abs(mario.getX() - element.getX()); // todo : distance at the time of shooting
        if (dist >= 350) {
            inJump = true;
        }
        else {
            double rand = Math.random();
            boolean toJump = rand >= (1 - dist / 350);
            if (toJump)
                inJump = true;
            else
                takeDamage(DamageType.MINOR);
        }
    }

    public void takeCut() {
        takeDamage(DamageType.SEVERE);
    }

    public void takeHit() {
        takeDamage(DamageType.SEVERE);
    }

    private enum DamageType {
        SEVERE,
        MINOR
    }
}
