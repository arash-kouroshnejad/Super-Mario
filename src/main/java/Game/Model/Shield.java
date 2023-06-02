package Game.Model;

public class Shield {
    private static Shield instance = new Shield();
    private Shield() {}

    public static Shield getInstance() {
        return instance;
    }
    private long activationTime;
    private long duration;

    public void activate (long duration) {
        this.activationTime = System.currentTimeMillis();
        this.duration = duration;
    }

    public boolean isActive() {
        return (System.currentTimeMillis() - activationTime < 1000 * duration);
    }

    public int getLeft () {
        if (isActive())
            return (int) (100 - (System.currentTimeMillis() - activationTime) / (10 * duration)); // TODO : use logic time instead
        return 0;
    }

    public void deactivate() {
        duration = 0;
    }
}
