package Game.Animations.BowserAnimations;

import Core.Editor.LevelEditor;
import Core.Objects.DynamicElement;
import Core.Render.ViewPort;
import Game.Animations.AbstractAnimation;
import Persistence.Config;

public class BowserLogic implements Runnable{
    private static final BowserLogic instance = new BowserLogic();

    private BowserLogic() {}

    public static BowserLogic getInstance() {return instance;}

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

    public void init() {
        int layer = Config.getInstance().getProperty("DynamicsLayer", Integer.class);
        element = LevelEditor.getInstance().getDynamicElement("Bowser", layer, 0).orElseThrow();
        closingAnimation = new Closing(element);
        fireBallAttack = new FireBallAttack(element);
        grabAttack = new GrabAttack(element);
        jumpAttack = new JumpAttack(element);
        randomMovement = new RandomMovement(element);
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
            jumpAttack.run();

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
}
