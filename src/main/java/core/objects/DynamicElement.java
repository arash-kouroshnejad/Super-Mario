package core.objects;


public class DynamicElement extends StaticElement {
    int speedX, speedY;

    private boolean lockedCharacter;

    private transient ElementManager manager;

    public void setLockedCharacter() {
        lockedCharacter = true;
    }

    public boolean isLockedCharacter() {
        return lockedCharacter;
    }

    public DynamicElement(int x, int y, int width, int height, int speedX, int speedY, String type) {
        super(x, y, width, height, type);
        this.speedX = speedX;
        this.speedY = speedY;
    }

    public int getSpeedX() {
        return speedX;
    }

    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    public int getSpeedY() {
        return speedY;
    }

    public void setSpeedY(int speedY) {
        this.speedY = speedY;
    }

    public void move() {
        x += speedX;
        y += speedY;
    }

    public ElementManager getManager() {
        return manager;
    }

    public void setManager(ElementManager manager) {
        this.manager = manager;
    }
}
